package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.ISA2020.farmacia.entity.Counseling;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.DTO.CounselingDTO;
import com.ISA2020.farmacia.entity.DTO.PeriodDTO;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.CounselingRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.repository.PharmacistRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.MailUtil;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/counseling")
public class CounselingController {

	@Autowired
	FarmacyRepository farmacyRepo;
	
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	CounselingRepository counselingRepo;
	@Autowired
	PharmacistRepository pharmaRepo;
	@Autowired
	MailUtil mailUtil;
	
	@JsonView(Views.VerySimpleFarmacy.class)
	@GetMapping("/farmacies")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> freeFarmacies( @RequestBody PeriodDTO period )  {	
		List<Farmacy> farmacies =  farmacyRepo.findAll();
		List<Farmacy> available = getAvaiableFarmacies(farmacies, period.getDateTime(),period.getEndTime());
		if(available.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(available, HttpStatus.OK);
	}

	@JsonView(Views.VerySimpleUser.class)
	@GetMapping("/pharmacists/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> freePharmas( @RequestBody PeriodDTO period, @PathVariable String id)  {	
		Optional<Farmacy> farmacy =  farmacyRepo.findById(id);
		if(farmacy.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		List<Pharmacist> pharmacists =  farmacy.get().getPharmacists();
		List<Pharmacist> available = getAvaiablePharmas(pharmacists, period.getDateTime(), period.getEndTime());
		if(available.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(available, HttpStatus.OK);
	}
	
	@PostMapping("/reserve")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> freePharmas(@RequestHeader("Authorization") String token, @RequestBody CounselingDTO counselingDTO) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException  {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		Optional<Pharmacist> pharma = pharmaRepo.findById(counselingDTO.getPharmacist());
		if(pharma.isEmpty() ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(!(pharma.get().getWorksFrom().isBefore(counselingDTO.getDateTime().toLocalTime()) && pharma.get().getWorksTo().isAfter(counselingDTO.getEndTime().toLocalTime()))
				 || !pharma.get().isPharmacistAvailableThen(counselingDTO.getDateTime(), counselingDTO.getEndTime()) || !pharma.get().isNotOnVacation(counselingDTO.getDateTime(), counselingDTO.getEndTime()) )
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Counseling counseling = new Counseling();
		counseling.setPharma(pharma.get());
		counseling.setDateTime(counselingDTO.getDateTime());
		counseling.setEndTime(counselingDTO.getEndTime());
		if(!patient.addCounseling(counseling)) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		counselingRepo.save(counseling);
		patientRepo.save(patient);
		mailUtil.confirmCounselingReservation(patient.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	
	@PostMapping("/cancel/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> cancel(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException  {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		
		Optional<Counseling> counseling = counselingRepo.findById(id);
		if(counseling.isEmpty() || !counseling.get().getDateTime().minusDays(1).isAfter(LocalDateTime.now()) || !patient.getCounselings().contains(counseling.get()) ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		counseling.get().setCanceled(true);
		counselingRepo.save(counseling.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}


	private List<Pharmacist> getAvaiablePharmas(List<Pharmacist> pharmacists, LocalDateTime from,
			LocalDateTime to) {
		List<Pharmacist> result = new ArrayList<>();
		for(Pharmacist fa : pharmacists) {
				if((fa.getWorksFrom().isBefore(from.toLocalTime()) && fa.getWorksTo().isAfter(to.toLocalTime()))
						 && fa.isPharmacistAvailableThen(from, to) && fa.isNotOnVacation(from, to) ) {
					result.add(fa);
					break;
				}

		}

		return result;
	}

	private List<Farmacy> getAvaiableFarmacies(List<Farmacy> farmacies, LocalDateTime from, LocalDateTime to) {
		List<Farmacy> result = new ArrayList<>();
		for(Farmacy fa : farmacies) {
			List<Pharmacist> pharmacists = fa.getPharmacists();
			for(Pharmacist ph : pharmacists) {
				if((ph.getWorksFrom().isBefore(from.toLocalTime()) && ph.getWorksTo().isAfter(to.toLocalTime()))
						&& !result.contains(fa) && ph.isPharmacistAvailableThen(from, to) && ph.isNotOnVacation(from, to) ) {
					result.add(fa);
					break;
				}
			}
			
			
			
		}

		return result;
	}

}
