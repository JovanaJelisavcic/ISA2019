package com.ISA2020.farmacia.controller;

import java.util.Properties;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.DTO.LoyaltyScaleDTO;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyProgramController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoyaltyProgramController.class);
	
	@PostMapping("/setCounselingPoints/{points}")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> setCounsel(@PathVariable int points)  {	
		logger.info("udje li tu");
		Properties p=System.getProperties();
		p.setProperty("counselingPoints", Integer.toString(points));
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/setAppointmentsPoints/{points}")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> setAppoint(@PathVariable int points) {	
		Properties p=System.getProperties();
		p.setProperty("appointmentPoints", Integer.toString(points));
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/setLoyaltyScale")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> setLogaltyScale(@RequestBody LoyaltyScaleDTO scale) {	
		Properties p=System.getProperties();
		if(!(scale.getRegular()<scale.getSilver()) || !(scale.getSilver()<scale.getGold())) return ResponseEntity.badRequest().build();
		p.setProperty("regularUser", Integer.toString(scale.getRegular()));
		p.setProperty("silverUser", Integer.toString(scale.getSilver()));
		p.setProperty("goldUser", Integer.toString(scale.getGold()));
		p.setProperty("regularDiscount", Integer.toString(scale.getRegularDiscount()));
		p.setProperty("silverDiscount", Integer.toString(scale.getSilverDiscount()));
		p.setProperty("goldDiscount", Integer.toString(scale.getGoldDiscount()));
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
}
