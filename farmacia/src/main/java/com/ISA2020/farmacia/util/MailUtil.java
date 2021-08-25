package com.ISA2020.farmacia.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.Complaint;
import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.DrugReservation;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Offer;
import com.ISA2020.farmacia.entity.Promotion;
import com.ISA2020.farmacia.entity.VacationDermatologist;
import com.ISA2020.farmacia.entity.VacationPharmacist;
import com.ISA2020.farmacia.entity.users.Patient;
@Component
public class MailUtil {
	
	
	
	
	   @Autowired
	    private JavaMailSender javaMailSender;
	   
	   String fromAddress = "jelisavcicjovana@gmail.com";
	    String senderName = "Farmacia";

	public void sendPromotionEmails(Farmacy farmacy, Promotion promotion) {
		
		for(Patient patient : farmacy.getSubscribedUsers()) {
			SimpleMailMessage msg = new SimpleMailMessage();
			 msg.setTo(patient.getEmail());
			 msg.setFrom(fromAddress);
			 msg.setSubject("Pharmacy Promotion");
			 msg.setText(promotion.getPromotionText());
			 javaMailSender.send(msg);
		}
		 
		
	}

	public void notifySuppliersAboutOffersStatus(List<Offer> otherOffers, Offer offer) {
	
		for(Offer failedOffer : otherOffers) {
			SimpleMailMessage msg = new SimpleMailMessage();
			 msg.setTo(failedOffer.getSupplier().getEmail());
			 msg.setFrom(fromAddress);
			 msg.setSubject("Pharmacy Supplies");
			 msg.setText("Sorry to inform you that your offer for our pharmacy wasn't accepted. Looking forward to working with you some other time!");
			 javaMailSender.send(msg);
			
		}
		SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(offer.getSupplier().getEmail());
		 msg.setFrom(fromAddress);
		 msg.setSubject("Pharmacy Supplies");
		 msg.setText("Glad to inform you that your offer for our pharmacy was accepted. Looking forward to working with you again!");
		 javaMailSender.send(msg);
		
	}

	public void sendConfirmVacationMail(VacationPharmacist vacationPharmacist) {
		SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(vacationPharmacist.getPharmacist().getEmail());
		 msg.setFrom(fromAddress);
		 msg.setSubject("Pharmacy Vacation");
		 msg.setText("Glad to inform you that your vacation request was accepted. Have fun!");
		 javaMailSender.send(msg);
		
		
	}

	public void sendDenyVacation(VacationPharmacist vacationPharmacist) throws MessagingException, UnsupportedEncodingException {
		  String toAddress = vacationPharmacist.getPharmacist().getEmail();
		    String senderName = "Farmacia";
		    String subject = "Answer to your vacation request";
		    String content = "Dear [[name]],<br>"
		            + "I am sorry to inform you that your vacation request has been denied for next reason:<br>"
		            + "[[reason]]"
		            + "<br>Thank you for understanding,<br>"
		            + "Farmacia.";
		     
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		     
		    helper.setFrom(fromAddress, senderName);
		    helper.setTo(toAddress);
		    helper.setSubject(subject);
		     
		    content = content.replace("[[name]]", vacationPharmacist.getPharmacist().getSurname());
		   
		     
		    content = content.replace("[[reason]]", vacationPharmacist.getExplanation());
		     
		    helper.setText(content, true);
		     
		    javaMailSender.send(message);
		
	}
	
	
	public void sendConfirmVacationMail(VacationDermatologist derma) {
		SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(derma.getDermatologist().getEmail());
		 msg.setFrom(fromAddress);
		 msg.setSubject("Pharmacy Vacation");
		 msg.setText("Glad to inform you that your vacation request was accepted. Have fun!");
		 javaMailSender.send(msg);
		
		
	}

	public void sendDenyVacation(VacationDermatologist derma) throws MessagingException, UnsupportedEncodingException {
		  String toAddress = derma.getDermatologist().getEmail();
		    String senderName = "Farmacia";
		    String subject = "Answer to your vacation request";
		    String content = "Dear [[name]],<br>"
		            + "I am sorry to inform you that your vacation request has been denied for next reason:<br>"
		            + "[[reason]]"
		            + "<br>Thank you for understanding,<br>"
		            + "Farmacia.";
		     
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		     
		    helper.setFrom(fromAddress, senderName);
		    helper.setTo(toAddress);
		    helper.setSubject(subject);
		     
		    content = content.replace("[[name]]", derma.getDermatologist().getSurname());
		   
		     
		    content = content.replace("[[reason]]", derma.getExplanation());
		     
		    helper.setText(content, true);
		     
		    javaMailSender.send(message);
		
	}

	public void sendComplaintResponse(Complaint complaint) throws MessagingException, UnsupportedEncodingException {
		 String toAddress = complaint.getPatient().getEmail();
		    String senderName = "Farmacia";
		    String subject = "Answer to your complaint";
		    String content = "Dear [[name]],<br>"
		            + "One of our admins responded to you with:<br>"
		            + "[[reason]]"
		            + "<br>Thank you for understanding,<br>"
		            + "Farmacia.";
		     
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		     
		    helper.setFrom(fromAddress, senderName);
		    helper.setTo(toAddress);
		    helper.setSubject(subject);
		     
		    content = content.replace("[[name]]", complaint.getPatient().getSurname());
		   
		     
		    content = content.replace("[[reason]]", complaint.getResponseText());
		     
		    helper.setText(content, true);
		     
		    javaMailSender.send(message);
		
	}

	public void confirmReservedDermapoint(DermAppointment dermAppointment, String email) {
		SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(email);
		 msg.setFrom(fromAddress);
		 msg.setSubject("Appointment with Dermatologist");
		 msg.setText("Successfully reserved an appointment with dermatologist!");
		 javaMailSender.send(msg);
		
	}

	public void confirmDrugReservation(DrugReservation drugReserve, String username) throws MessagingException, UnsupportedEncodingException {
		    String senderName = "Farmacia";
		    String subject = "Drug Reservation";
		    String content = "Dear patient,<br>"
		            + "Your reservation is successfull and  your unique reservation number is :<br>"
		            + "[[reason]]"
		            + "<br>Thank you for trusting us,<br>"
		            + "Farmacia.";
		     
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		     
		    helper.setFrom(fromAddress, senderName);
		    helper.setTo(username);
		    helper.setSubject(subject);
		   
		     
		    content = content.replace("[[reason]]", drugReserve.getReservationId().toString());
		     
		    helper.setText(content, true);
		     
		    javaMailSender.send(message);
	}

}
