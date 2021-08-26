package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Complaint;
import com.ISA2020.farmacia.entity.ComplaintSubjectType;
import com.ISA2020.farmacia.entity.Counseling;
import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.DrugReservation;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.ComplaintRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.MailUtil;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {

	@Autowired
	FarmacyRepository farmacyRepository;
	@Autowired
	ComplaintRepository complaintRepository;
	@Autowired
	MailUtil mailUtil;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	JwtUtils jwtUtils;
	
	@JsonView(Views.ComplaintsList.class)
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> getComplaints() {	
		return new ResponseEntity<>(complaintRepository.findAll(), HttpStatus.OK);
		
		
	}
	
	@PostMapping("/respond/{vid}")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> respondComplaint(@PathVariable Long vid, @RequestBody String response) throws UnsupportedEncodingException, MessagingException {	
		Optional<Complaint> complaint = complaintRepository.findById(vid);
		if(complaint.isEmpty()) return ResponseEntity.notFound().build();
		if(complaint.get().isResponded()) return ResponseEntity.badRequest().build();
		complaint.get().setResponded(true);
		String[] splited = response.split(":");
		int first =splited[1].indexOf("\"");
		int last =splited[1].lastIndexOf("\"");
		String respondValue =splited[1].substring(first+1, last);
		complaint.get().setResponseText(respondValue);
		complaintRepository.save(complaint.get());
		mailUtil.sendComplaintResponse(complaint.get());
		return ResponseEntity.ok().build();	
		
	}
	
	@PostMapping("/complain")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> respondComplaint(@RequestHeader("Authorization") String token,@RequestBody Complaint complaint) throws UnsupportedEncodingException, MessagingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		if(!checkPossible(patient,complaint.getSubject(), complaint.getSubjectId())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Complaint newOne = new Complaint();
		newOne.setComplaintText(complaint.getComplaintText());
		newOne.setResponded(false);
		newOne.setSubject(complaint.getSubject());
		newOne.setSubjectId(complaint.getSubjectId());
		newOne.setPatient(patient);
		complaintRepository.save(newOne);
		return ResponseEntity.ok().build();	
		
	}
	
	
	@JsonView(Views.VerySimpleFarmacy.class)
	@GetMapping("/farmacies")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> getFamacies(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Farmacy> possible = getOnlyPossibleFarmacies(patient);
		if(possible.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<>(possible, HttpStatus.OK);
	}
	
	@JsonView(Views.VerySimpleUser.class)
	@GetMapping("/dermas")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> getDermas(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Dermatologist> possible = getOnlyPossibleDermas(patient);
		if(possible.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<>(possible, HttpStatus.OK);
	}
	
	@JsonView(Views.VerySimpleUser.class)
	@GetMapping("/pharmas")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> getPharmas(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Pharmacist> possible = getOnlyPossiblePharmas(patient);
		if(possible.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<>(possible, HttpStatus.OK);
	}
	
	
	private boolean checkPossible(Patient patient, ComplaintSubjectType subject, String subjectId) {
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


	private List<Pharmacist> getOnlyPossiblePharmas(Patient patient) {
		List<Pharmacist> result = new ArrayList<>();

		for(Counseling c : patient.getCounselings()) {
			if(!c.isCanceled() && c.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(c.getPharma()))
				result.add(c.getPharma());
		}
		return result;
	}

	private List<Dermatologist> getOnlyPossibleDermas(Patient patient) {
			List<Dermatologist> result = new ArrayList<>();

		for(DermAppointment d : patient.getDermappoints()) {
			if(d.isReserved() && d.getEndTime().isBefore(LocalDateTime.now()) && !result.contains(d.getDerma()))
				result.add(d.getDerma());
		}
		return result;
	}

	private List<Farmacy> getOnlyPossibleFarmacies(Patient patient) {
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
	
	
	
	
	
}
