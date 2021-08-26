package com.ISA2020.farmacia.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

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
			if(!c.isCanceled() && c.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(c.getPharma()))
				result.add(c.getPharma());
		}
		return result;
	}

	public List<Dermatologist> getOnlyPossibleDermas(Patient patient) {
			List<Dermatologist> result = new ArrayList<>();

		for(DermAppointment d : patient.getDermappoints()) {
			if(d.isReserved() && d.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(d.getDerma()))
				result.add(d.getDerma());
		}
		return result;
	}

	public List<Farmacy> getOnlyPossibleFarmacies(Patient patient) {
		List<Farmacy> result = new ArrayList<>();
		
		for(Counseling c : patient.getCounselings()) {
			if(!c.isCanceled() && c.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(c.getPharma().getFarmacy()))
				result.add(c.getPharma().getFarmacy());
		}
		
		for(DermAppointment d : patient.getDermappoints()) {
			if(d.isReserved() && d.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(d.getFarmacy()))
				result.add(d.getFarmacy());
		}
		
		for(DrugReservation r : patient.getDrugsReserved()) {
			if(r.getPickUp().isBefore(LocalDate.now()) && !result.contains(r.getFarmacy()))
				result.add(r.getFarmacy());
		}
		
		return result;
		
	}
	private List<Drug> getOnlyPossibleDrugs(Patient patient) {
		List<Drug> result = new ArrayList<>();
		for(DrugReservation r : patient.getDrugsReserved()) {
			if(r.getPickUp().isBefore(LocalDate.now()) && !result.contains(r.getDrug()))
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
