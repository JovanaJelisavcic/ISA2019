package com.ISA2020.farmacia.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.WorkingHours;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.CounselingRepository;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.PharmacistRepository;
import com.ISA2020.farmacia.repository.WorkingHoursRepository;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ReportUtil {
	@Autowired
	DermappointRepository appointRepo;
	@Autowired
	PharmacistRepository pharmaRepo;
	@Autowired
	WorkingHoursRepository workingHoursRepository;
	@Autowired
	CounselingRepository counselRepo;
	Document document;
	
	public  JFreeChart generateCharts(String farmacyId, String fileName) {
		//examinations
		XYDataset  dataSet = generateEcaminationDataSet(farmacyId);
		JFreeChart chart = createChart(dataSet);
		writeChartToPDF(chart, 300, 200, fileName);
		return chart;
	}

	private  XYDataset  generateEcaminationDataSet(String farmacyId) {
		var series = new XYSeries(""+LocalDate.now().getMonth().toString()+ ""+LocalDate.now().getYear());
		List<Pharmacist> pharmas = pharmaRepo.findByFarmacyId(farmacyId);
		List<String> emails = new ArrayList<>();
		for(Pharmacist p : pharmas) {
			emails.add(p.getEmail());
		}
		for(int i =1; i <= LocalDate.now().lengthOfMonth(); i++)
        series.add(1, counselRepo.countThisMonth( i, emails)+ appointRepo.countThisMonth(farmacyId, i));
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}
	
	 private JFreeChart createChart(XYDataset dataset) {

	        JFreeChart chart = ChartFactory.createXYLineChart(
	                "Examinations in a month",
	                "Day",
	                "Num Of Exams",
	                dataset,
	                PlotOrientation.VERTICAL,
	                true,
	                true,
	                false
	        );

	       /* XYPlot plot = chart.getXYPlot();

	        var renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesPaint(0, Color.RED);
	        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

	        plot.setRenderer(renderer);
	        plot.setBackgroundPaint(Color.white);

	        plot.setRangeGridlinesVisible(true);
	        plot.setRangeGridlinePaint(Color.BLACK);

	        plot.setDomainGridlinesVisible(true);
	        plot.setDomainGridlinePaint(Color.BLACK);*/

	        chart.getLegend().setFrame(BlockBorder.NONE);

	        chart.setTitle(new TextTitle("Examinations in a month"
	                )
	        );

	        return chart;
	    }
	 
	 
	 
	 
	 public  void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
			PdfWriter writer = null;

			

			try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(
						fileName));
				document.open();
				PdfContentByte contentByte = writer.getDirectContent();
				PdfTemplate template = contentByte.createTemplate(width, height);
				Graphics2D g2 = new PdfGraphics2D(contentByte, 500, 500);
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
						height);

				chart.draw(g2, rectangle2d);
				
				g2.dispose();
				contentByte.addTemplate(template, 0, 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
			document.close();
		}

	public ByteArrayInputStream generateReport(Farmacy farmacyid, String filename) throws FileNotFoundException {
		   document = new Document();
		  ByteArrayOutputStream out = new ByteArrayOutputStream();

	        try {
	        	
	        	
	        	PdfWriter.getInstance(document, new FileOutputStream(filename));
	        	List<WorkingHours> dermasH = workingHoursRepository.findAllByFarmacyId(farmacyid.getId());
	        	List<Dermatologist> dermas = new ArrayList<>();
	        	for(WorkingHours h : dermasH) {
	        		dermas.add(h.getDermatologist());
	        	}
	        	document.open();
	        	Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
	        	Chunk chunk = new Chunk("Monthly Report for Pharmacy"+farmacyid.getName(), font);
	        	document.add(new Paragraph("\n"));
	        	chunk.append("Average rating of pharmacy : " + farmacyid.getStars());
	        	document.add(new Paragraph("\n"));
	        	chunk.append("Average rating of each Pharmacist : ");
	        	List<Pharmacist> pharmas = farmacyid.getPharmacists();
	        	for(Pharmacist p : pharmas) {
	        		document.add(new Paragraph("\n"));
	        		chunk.append(p.getName() + " " + p.getSurname() +" : "+p.getStars());	
	        	}
	        	document.add(new Paragraph("\n"));
	        	chunk.append("Average rating of each Dermatologist : ");
	        	for(Dermatologist d : dermas) {
	        		document.add(new Paragraph("\n"));
	        		chunk.append(d.getName() +  " " + d.getSurname() + " : "+ d.getStars());
	        	}
	        	document.add(chunk);
	        	
	        	document.close();

	            
	           // generateCharts(farmacyid.getId(), filename);

	        } catch (DocumentException ex) {

	        }

	        return new ByteArrayInputStream(out.toByteArray());
	}
}
