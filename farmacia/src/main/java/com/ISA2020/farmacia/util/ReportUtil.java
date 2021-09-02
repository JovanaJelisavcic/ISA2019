package com.ISA2020.farmacia.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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
	
	


	private  XYSeriesCollection  generateEcaminationMonthlyDataSet(String farmacyId) {
		var series = new XYSeries(""+LocalDate.now().getMonth().toString()+ ""+LocalDate.now().getYear());
		List<Pharmacist> pharmas = pharmaRepo.findByFarmacyId(farmacyId);
		List<String> emails = new ArrayList<>();
		for(Pharmacist p : pharmas) {
			emails.add(p.getEmail());
		}
		for(int i =1; i <= LocalDate.now().lengthOfMonth(); i++)
        series.add(i, counselRepo.countThisDay( i, emails)+ appointRepo.countThisDay(farmacyId, i));
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}
	
	
	       

	public ByteArrayInputStream generateReport(Farmacy farmacyid, String filename) throws FileNotFoundException {
		  ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
	            Document doc = new Document(PageSize.A4);
	          
	            PdfWriter docWriter = PdfWriter.getInstance(doc,
	                    out);


	            doc.open();
	            
	            
	            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
	        	Paragraph chunk = new Paragraph("Report for Pharmacy "+farmacyid.getName(), font);
	        	chunk.add(new Paragraph(" "));
	        	chunk.add("Average rating of pharmacy : " + farmacyid.getStars());
	        	chunk.add(new Paragraph(" "));
	        	chunk.add("Average rating of each Pharmacist : ");
	        	List<Pharmacist> pharmas = farmacyid.getPharmacists();
	        	for(Pharmacist p : pharmas) {
	        		chunk.add(new Paragraph(" "));
	        		chunk.add("*"+p.getName() + " " + p.getSurname() +" : "+p.getStars());	
	        	}
	        	List<WorkingHours> dermasH = workingHoursRepository.findAllByFarmacyId(farmacyid.getId());
	        	List<Dermatologist> dermas = new ArrayList<>();
	        	for(WorkingHours h : dermasH) {
	        		dermas.add(h.getDermatologist());
	        	}
	        	chunk.add(new Paragraph(" "));
	        	chunk.add("Average rating of each Dermatologist : ");
	        	for(Dermatologist d : dermas) {
	        		chunk.add(new Paragraph(" "));
	        		chunk.add("*"+d.getName() +  " " + d.getSurname() + " : "+ d.getStars());
	        	}
	            doc.add(chunk);
	            
	            
	            //mesecno
	        	XYSeriesCollection  dataSet = generateEcaminationMonthlyDataSet(farmacyid.getId());

	           

	            // set up the chart
	            JFreeChart chart = ChartFactory.createXYLineChart(
	            		 "Examinations in a month",
	 	                "Day",
	 	                "Num Of Exams",
	 	               dataSet, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );

	            // trick to change the default font of the chart
	            chart.setTitle(new TextTitle("Examinations in a month",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            chart.setBackgroundPaint(Color.white);
	            chart.setBorderPaint(Color.black);
	            chart.setBorderStroke(new BasicStroke(1));
	            chart.setBorderVisible(true);

	            int width = 260;
	            int height = 250;


	            // get the direct pdf content
	            PdfContentByte dc = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tp = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2 = 
	            		new PdfGraphics2D(tp, width, height);
	    
	            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
	            chart.draw(g2, r2D, null);
	            g2.dispose();

	           
	            Image chartImage = Image.getInstance(tp);
	            doc.add(chartImage);
	        
	            
	            
	            //kvartal examinations
	        
	            
	            XYSeriesCollection  anualExaminations = generateEcaminationAnualDataSet(farmacyid.getId());

		           

	            // set up the chart
	            JFreeChart anualChart = ChartFactory.createXYLineChart(
	            		 "Examinations in a year",
	 	                "Month",
	 	                "Num Of Exams",
	 	               anualExaminations, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );
	          
	            // trick to change the default font of the chart
	            anualChart.setTitle(new TextTitle("Examinations in a year",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            anualChart.setBackgroundPaint(Color.white);
	            anualChart.setBorderPaint(Color.black);
	            anualChart.setBorderStroke(new BasicStroke(1));
	            anualChart.setBorderVisible(true);


	            

	          // get the direct pdf content
	        //    PdfContentByte dcQE = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tpQE = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2QE = 
	            		new PdfGraphics2D(tpQE, width, height);
	    
	            Rectangle2D r2DQE = new Rectangle2D.Double(0, 0, width, height);
	            anualChart.draw(g2QE, r2DQE, null);
	            g2QE.dispose();

	            
	            Image anualImage = Image.getInstance(tpQE);
	            doc.add(anualImage);
	          
	            
	            
	            

	            doc.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return new ByteArrayInputStream(out.toByteArray());
		
	    }




	private XYSeriesCollection generateEcaminationAnualDataSet(String id) {
		var series = new XYSeries(LocalDate.now().getYear());
		List<Pharmacist> pharmas = pharmaRepo.findByFarmacyId(id);
		List<String> emails = new ArrayList<>();
		for(Pharmacist p : pharmas) {
			emails.add(p.getEmail());
		}
		for(int i =1; i <= 12; i++)
        series.add(i, counselRepo.countThisMonth( i, emails)+ appointRepo.countThisMonth(id, i));
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}




	

	}

