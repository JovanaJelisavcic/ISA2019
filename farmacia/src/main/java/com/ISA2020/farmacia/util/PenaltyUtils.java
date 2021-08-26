package com.ISA2020.farmacia.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.basic.ResetDate;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.repository.ResetDateRepository;

@Component
public class PenaltyUtils {
	

	@Autowired
	PatientRepository patientRepo;
	@Autowired
	ResetDateRepository datesRepo;
	@Scheduled(cron="0 0 0 1 1/1 *")
	public void resetIfDue() {

	    ResetDate resetDate = datesRepo.getById(Long.valueOf(1));
	    LocalDate lastResetDate = resetDate.getStandsFrom();
	    LocalDate nextResetDate = lastResetDate.plusMonths(1)
	                                .withDayOfMonth(1);
	    LocalDate today = LocalDate.now();

	    if (! nextResetDate.isAfter(today)) {
	    	resetDate.setStandsFrom(today);
	    	datesRepo.save(resetDate);
	        updatePenaltiesCounts();

	    }

	}
	private void updatePenaltiesCounts() {
		
		List<Patient> allthem = patientRepo.findAll();
		   
	    for (Patient thisOne : allthem) {
	    	thisOne.setPenalties(0);
	
	    }

	    patientRepo.saveAll(allthem);
	}
}
