package com.ISA2020.farmacia.util;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.basic.Price;
import com.ISA2020.farmacia.entity.intercations.Counseling;
import com.ISA2020.farmacia.entity.intercations.DermAppointment;
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.repository.PriceRepository;

@Component
public class LoyaltyUtils {
	@Autowired
	PriceRepository priceRepository;

	public float discountDrugPrice(DrugReservation d, Patient patient) {

		Price drugPrice = priceRepository.findPrice(d.getDrug().getCode(), d.getFarmacy().getId(), LocalDate.now());
		int discount = getPatientDiscount(patient);
		return calculateDiscountPrice(drugPrice.getPrice(), discount );
		
	}

	private float calculateDiscountPrice(float price, int discount) {
		
		return price - (price/100)*discount;
	}

	public int getPatientDiscount(Patient patient) {
		if(patient.getLoyalty()<Integer.valueOf(System.getProperty("regularUser")))
			return Integer.valueOf(System.getProperty("regularDiscount"));
		if(Integer.valueOf(System.getProperty("regularUser"))<=patient.getLoyalty() && patient.getLoyalty()<Integer.valueOf(System.getProperty("silverUser")))
			return Integer.valueOf(System.getProperty("silverDiscount"));
		if(Integer.valueOf(System.getProperty("silverUser"))<=patient.getLoyalty())
			return Integer.valueOf(System.getProperty("goldDiscount"));
		else return 0;
	}

	public float discountConsultPrice(Counseling d, Patient patient) {
		int discount = getPatientDiscount(patient);
		return calculateDiscountPrice(d.getPrice(), discount );
	}

	public float discountAppointPrice(DermAppointment d, Patient patient) {
		int discount = getPatientDiscount(patient);
		return calculateDiscountPrice(d.getPrice(), discount );
	}
	
	public String getUserType(int i) {
		if(i<Integer.valueOf(System.getProperty("regularUser")))
			return "REGULAR USER";
		if(Integer.valueOf(System.getProperty("regularUser"))<=i && i<Integer.valueOf(System.getProperty("silverUser")))
			return "SILVER USER";
		if(Integer.valueOf(System.getProperty("silverUser"))<=i)
			return "GOLD USER";
		else return "";
	}

}
