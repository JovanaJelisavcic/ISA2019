package com.ISA2020.farmacia.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Offer;
import com.ISA2020.farmacia.entity.Promotion;
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

}
