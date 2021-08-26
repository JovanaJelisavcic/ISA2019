package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Counseling;
import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.DrugReservation;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.UserInfo;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/patient")
public class PatientProfileController {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	JwtUtils jwtUtils;
	
	@JsonView(Views.SimpleUser.class)
	@GetMapping("/myprofile")
	@PreAuthorize("hasAuthority('PATIENT')")
	public Patient myProfile(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return  patientRepo.findById(username).get();
	}
	
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UserInfo user) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient newOne = patientRepo.findById(username).get();
		if(!user.getEmail().equals(username)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		newOne.changeUserInfo(user);
		patientRepo.save(newOne);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/addAllergies")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> addAllergies(@RequestHeader("Authorization") String token, @RequestBody List<String> codes) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		if(codes.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		logger.info(codes.get(0));
		Patient patient = patientRepo.findById(username).get();
		List<Drug> drugs = getDrugsFromCodes(codes);
		if(drugs==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		patient.addAllergies(drugs);
		patientRepo.save(patient);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	@JsonView(Views.VerySimpleDrug.class)
	@GetMapping("/getNonAllergicDrugs")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<Drug>> getDrugsForAllergies(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Drug> allergies = patient.getAllergies();
		List<Drug> nonAllergens =  drugRepo.findAll();
		nonAllergens.removeAll(allergies);
		return new ResponseEntity<>(nonAllergens,HttpStatus.OK);
		
	}

	@JsonView(Views.DermappointDetailedList.class)	
	@GetMapping("/futureDermAppointments")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<DermAppointment>> getfuturedermapoint(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<DermAppointment> future = patient.getDermappoints();
		future.removeIf(a-> a.isDone());
		if(future.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(future,HttpStatus.OK);
		
	}
	
	@JsonView(Views.DermappointDetailedList.class)	
	@GetMapping("/pastDermAppointments")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<DermAppointment>> getpastdermapoint(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<DermAppointment> past = patient.getDermappoints();
		past.removeIf(a-> !a.isDone());
		if(past.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(past,HttpStatus.OK);
		
	}
	@JsonView(Views.ReservedDrugsList.class)	
	@GetMapping("/reservedDrugs")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<DrugReservation>> reservedDrugs(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<DrugReservation> drugs = patient.getDrugsReserved();
		if(drugs.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(drugs,HttpStatus.OK);
		
	}
	
	
	
	@JsonView(Views.CounselingList.class)	
	@GetMapping("/futureCounselings")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<Counseling>> getfuturecounsel(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Counseling> future = patient.getCounselings();
		future.removeIf(a-> a.getEndTime().isBefore(LocalDateTime.now()));
		if(future.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(future,HttpStatus.OK);
		
	}
	
	@JsonView(Views.CounselingList.class)	
	@GetMapping("/pastCounselings")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<List<Counseling>> pastCounselings(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Counseling> past = patient.getCounselings();
		past.removeIf(a-> a.getDateTime().isAfter(LocalDateTime.now()));
		if(past.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(past,HttpStatus.OK);
		
	}
	
	@JsonView(Views.SimpleFarmacy.class)	
	@GetMapping("/subscriptions")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> subscriptions(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		List<Farmacy> subs = patient.getFarmaciesSubs();
		if(subs.isEmpty()) new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(subs,HttpStatus.OK);
		
	}

	private List<Drug> getDrugsFromCodes(List<String> codes) {
		List<Drug> result = new ArrayList<Drug>();
		for(String code : codes) {
			Optional<Drug> drug = drugRepo.findById(code);
			logger.info(drug.get().toString());
			if(drug.isPresent())
			result.add(drug.get());
		}
		return result;
	}
}
