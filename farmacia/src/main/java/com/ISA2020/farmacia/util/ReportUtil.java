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
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.CounselingRepository;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.DrugReservationRespository;
import com.ISA2020.farmacia.repository.PharmacistRepository;
import com.ISA2020.farmacia.repository.PriceRepository;
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
	DrugReservationRespository drugReserveRepo;
	@Autowired
	CounselingRepository counselRepo;
	@Autowired
	PriceRepository priceRepo;
	
	


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
	          
	            
	            
 //drug monthly
	        
	            
	            XYSeriesCollection  monthDrugData = generateMonthDrugDataSet(farmacyid.getId());

		           

	            // set up the chart
	            JFreeChart monthDrugChart = ChartFactory.createXYLineChart(
	            		 "Drug Pickups in a month",
	 	                "Month",
	 	                "Num Of Rsrv",
	 	               monthDrugData, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );
	          
	            // trick to change the default font of the chart
	            monthDrugChart.setTitle(new TextTitle("Drug Pickups in a month",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            monthDrugChart.setBackgroundPaint(Color.white);
	            monthDrugChart.setBorderPaint(Color.black);
	            monthDrugChart.setBorderStroke(new BasicStroke(1));
	            monthDrugChart.setBorderVisible(true);


	            

	          // get the direct pdf content
	        //    PdfContentByte dcQE = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tpMD = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2MD = 
	            		new PdfGraphics2D(tpMD, width, height);
	    
	            Rectangle2D r2DMD = new Rectangle2D.Double(0, 0, width, height);
	            monthDrugChart.draw(g2MD, r2DMD, null);
	            g2MD.dispose();

	            
	            Image MDImage = Image.getInstance(tpMD);
	            doc.add(MDImage);
	            
	            
	            
	            
 //drug year
	        
	            
	            XYSeriesCollection  yearDrugData = generateYearDrugDataSet(farmacyid.getId());

		           

	            // set up the chart
	            JFreeChart yearDrugChart = ChartFactory.createXYLineChart(
	            		 "Drug Pickups in a year",
	 	                "Month",
	 	                "Num Of Rsrv",
	 	               yearDrugData, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );
	          
	            // trick to change the default font of the chart
	            yearDrugChart.setTitle(new TextTitle("Drug Pickups in a year",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            yearDrugChart.setBackgroundPaint(Color.white);
	            yearDrugChart.setBorderPaint(Color.black);
	            yearDrugChart.setBorderStroke(new BasicStroke(1));
	            yearDrugChart.setBorderVisible(true);


	            

	          // get the direct pdf content
	        //    PdfContentByte dcQE = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tpYD = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2YD = 
	            		new PdfGraphics2D(tpYD, width, height);
	    
	            Rectangle2D r2DYD = new Rectangle2D.Double(0, 0, width, height);
	            yearDrugChart.draw(g2YD, r2DYD, null);
	            g2YD.dispose();

	            
	            Image YDImage = Image.getInstance(tpYD);
	            doc.add(YDImage);
	            
//income monthly
	        
	            
	            XYSeriesCollection  monthIncomeData = generateMonthIncomeDataSet(farmacyid.getId());

		           

	            // set up the chart
	            JFreeChart monthIncomeChart = ChartFactory.createXYLineChart(
	            		 "Income in a month",
	 	                "Day",
	 	                "Income",
	 	               monthIncomeData, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );
	          
	            // trick to change the default font of the chart
	            monthIncomeChart.setTitle(new TextTitle("Income in a month",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            monthIncomeChart.setBackgroundPaint(Color.white);
	            monthIncomeChart.setBorderPaint(Color.black);
	            monthIncomeChart.setBorderStroke(new BasicStroke(1));
	            monthIncomeChart.setBorderVisible(true);


	            

	          // get the direct pdf content
	        //    PdfContentByte dcQE = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tpMI = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2MI = 
	            		new PdfGraphics2D(tpMI, width, height);
	    
	            Rectangle2D r2DMI = new Rectangle2D.Double(0, 0, width, height);
	            monthIncomeChart.draw(g2MI, r2DMI, null);
	            g2MI.dispose();

	            
	            Image MIImage = Image.getInstance(tpMI);
	            doc.add(MIImage);
	            
	            
//income yearly
	        
	            
	            XYSeriesCollection  yearIncomeData = generateYearIncomeDataSet(farmacyid.getId());

		           

	            // set up the chart
	            JFreeChart yearIncomeChart = ChartFactory.createXYLineChart(
	            		 "Income in a year",
	 	                "Month",
	 	                "Income",
	 	               yearIncomeData, // data
	                    PlotOrientation.VERTICAL, // orientation
	                    true, // include legend
	                    true, // tooltips
	                    false // urls
	                    );
	          
	            // trick to change the default font of the chart
	            yearIncomeChart.setTitle(new TextTitle("Income in a year",
	                    new java.awt.Font("Serif", Font.BOLD, 12)));
	            yearIncomeChart.setBackgroundPaint(Color.white);
	            yearIncomeChart.setBorderPaint(Color.black);
	            yearIncomeChart.setBorderStroke(new BasicStroke(1));
	            yearIncomeChart.setBorderVisible(true);


	            

	          // get the direct pdf content
	        //    PdfContentByte dcQE = docWriter.getDirectContent();

	            // get a pdf template from the direct content
	            PdfTemplate tpYI = dc.createTemplate(width, height);

	            // create an AWT renderer from the pdf template
	            Graphics2D g2YI = 
	            		new PdfGraphics2D(tpYI, width, height);
	    
	            Rectangle2D r2DYI = new Rectangle2D.Double(0, 0, width, height);
	            yearIncomeChart.draw(g2YI, r2DYI, null);
	            g2YI.dispose();

	            
	            Image YIImage = Image.getInstance(tpYI);
	            doc.add(YIImage);
	            
	            
	            
	            

	            doc.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return new ByteArrayInputStream(out.toByteArray());
		
	    }




	private XYSeriesCollection generateYearIncomeDataSet(String id) {
		var series = new XYSeries(LocalDate.now().getYear());
		List<Pharmacist> pharmas = pharmaRepo.findByFarmacyId(id);
		
		List<String> emails = new ArrayList<>();
		for(Pharmacist p : pharmas) {
			emails.add(p.getEmail());
		}
		
		
		for(int i =1; i <= 12; i++) {
			float drugsPrice = 0;
			List<DrugReservation> drugs = drugReserveRepo.findIncomeByMonth(id, i);
			for(DrugReservation dr : drugs) {
				drugsPrice= drugsPrice + priceRepo.findPrice(dr.getDrug().getCode(), id, dr.getPickUp()).getPrice();
			}
        series.add(i, (appointRepo.incomeByMonth(i, id) + counselRepo.incomeByMonth(i, emails )+drugsPrice) );
		}
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}




	private XYSeriesCollection generateMonthIncomeDataSet(String id) {
		var series = new XYSeries(""+LocalDate.now().getMonth().toString()+ ""+LocalDate.now().getYear());
		List<Pharmacist> pharmas = pharmaRepo.findByFarmacyId(id);
		List<String> emails = new ArrayList<>();
		for(Pharmacist p : pharmas) {
			emails.add(p.getEmail());
		}
		
		
		for(int i =1; i <= LocalDate.now().lengthOfMonth(); i++) {
			float drugsPrice = 0;
			List<DrugReservation> drugs = drugReserveRepo.findIncomeByDay(id, i);
			for(DrugReservation dr : drugs) {
				drugsPrice= drugsPrice +priceRepo.findPrice(dr.getDrug().getCode(), id, dr.getPickUp()).getPrice();
			}
        series.add(i, (appointRepo.incomeByDay(i, id) + counselRepo.incomeByDay(i, emails )+drugsPrice) );
		}
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}




	private XYSeriesCollection generateYearDrugDataSet(String id) {
		var series = new XYSeries(LocalDate.now().getYear());
		
		for(int i =1; i <= 12; i++)
        series.add(i, drugReserveRepo.countThisMonth( i, id));
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
	}




	private XYSeriesCollection generateMonthDrugDataSet(String id) {
		var series = new XYSeries(""+LocalDate.now().getMonth().toString()+ ""+LocalDate.now().getYear());
		
		for(int i =1; i <= LocalDate.now().lengthOfMonth(); i++)
        series.add(i, drugReserveRepo.countByDay(i, id));
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
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

