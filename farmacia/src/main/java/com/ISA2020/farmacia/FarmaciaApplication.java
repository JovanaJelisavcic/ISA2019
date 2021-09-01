package com.ISA2020.farmacia;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FarmaciaApplication {
	
	public static void main(String[] args) {
		 Properties p=System.getProperties();  
		p.setProperty("counselingPoints", "5");
		p.setProperty("appointmentPoints", "5");
		
		p.setProperty("regularUser", "15");
		p.setProperty("silverUser", "25");
		p.setProperty("goldUser", "50");
		
		
		p.setProperty("regularDiscount", "5");
		p.setProperty("silverDiscount", "10");
		p.setProperty("goldDiscount", "15");
		SpringApplication.run(FarmaciaApplication.class, args);
	}

}
