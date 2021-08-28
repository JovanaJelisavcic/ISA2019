package com.ISA2020.farmacia.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.controller.basic.RegisterController;
import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.RatingSubjectType;
import com.ISA2020.farmacia.entity.intercations.ComplaintSubjectType;
import com.ISA2020.farmacia.entity.intercations.Counseling;
import com.ISA2020.farmacia.entity.intercations.DermAppointment;
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.Pharmacist;
@Component
public class SubjectUtils {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);
	public boolean checkPossible(Patient patient, ComplaintSubjectType subject, String subjectId) {
		switch(subject) {
		  case DERMATOLOGIST:
		    if(getOnlyPossibleDermas(patient).stream().anyMatch(o -> o.getEmail().equals(subjectId)))
		    	return true; 
		    return false;
		  case PHARMACIST:
			  if(getOnlyPossiblePharmas(patient).stream().anyMatch(o -> o.getEmail().equals(subjectId)))
			    	return true; 
			    return false;
		  case FARMACY:
			  if(getOnlyPossibleFarmacies(patient).stream().anyMatch(o -> o.getId().equals(subjectId)))
			    	return true; 
			    return false;
		  default:
		   	return false;
		}
	}


	public List<Pharmacist> getOnlyPossiblePharmas(Patient patient) {
		List<Pharmacist> result = new ArrayList<>();

		for(Counseling c : patient.getCounselings()) {
			if(!c.isCanceled() && c.isShowUp()  && !result.contains(c.getPharma()))
				result.add(c.getPharma());
		}
		return result;
	}

	public List<Dermatologist> getOnlyPossibleDermas(Patient patient) {
			List<Dermatologist> result = new ArrayList<>();

		for(DermAppointment d : patient.getDermappoints()) {
			if(d.isReserved() && d.isDone() && !result.contains(d.getDerma()))
				result.add(d.getDerma());
		}
		return result;
	}

	public List<Farmacy> getOnlyPossibleFarmacies(Patient patient) {
		List<Farmacy> result = new ArrayList<>();
		
		for(Counseling c : patient.getCounselings()) {
			if(!c.isCanceled() && c.isShowUp() && !result.contains(c.getPharma().getFarmacy())) {
				result.add(c.getPharma().getFarmacy());
				logger.info("id apoteke preko counseling je : "+ c.getPharma().getFarmacy().getId());
			}
		}
		
		for(DermAppointment d : patient.getDermappoints()) {
			if(d.isReserved() && d.isDone() && !result.contains(d.getFarmacy())) {
				result.add(d.getFarmacy());
				logger.info("id apoteke preko dermapointa je : "+ d.getFarmacy().getId());
			}
		}
		
		for(DrugReservation r : patient.getDrugsReserved()) {
			if(r.isShowUp() && !result.contains(r.getFarmacy())) {
				logger.info("id apoteke preko drug reserv je : "+ r.getFarmacy().getId());
				result.add(r.getFarmacy());
			}
		}
		
		return result;
		
	}
	private List<Drug> getOnlyPossibleDrugs(Patient patient) {
		List<Drug> result = new ArrayList<>();
		for(DrugReservation r : patient.getDrugsReserved()) {
			if(r.isShowUp() && !result.contains(r.getDrug()))
				result.add(r.getDrug());
		}
		
		return result;
	}


	public boolean checkPossible(Patient patient, RatingSubjectType subject, String subjectId) {
		switch(subject) {
		  case DERMATOLOGIST:
		    if(getOnlyPossibleDermas(patient).stream().anyMatch(o -> o.getEmail().equals(subjectId)))
		    	return true; 
		    return false;
		  case PHARMACIST:
			  if(getOnlyPossiblePharmas(patient).stream().anyMatch(o -> o.getEmail().equals(subjectId)))
			    	return true; 
			    return false;
		  case FARMACY:
			  if(getOnlyPossibleFarmacies(patient).stream().anyMatch(o -> o.getId().equals(subjectId)))
			    	return true; 
			    return false;
		  case DRUG:
			  if(getOnlyPossibleDrugs(patient).stream().anyMatch(o -> o.getCode().equals(subjectId)))
			    	return true; 
			    return false;
		  default:
		   	return false;
		}
	}


	
	
	
}
