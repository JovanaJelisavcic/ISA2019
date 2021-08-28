package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.DTO.DrugDTO;
import com.ISA2020.farmacia.entity.DTO.DrugReservationDTO;
import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.DrugReservationRespository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.MailUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/drug")
public class DrugController {

	@Autowired
	DrugRepository drugRepo;
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DrugReservationRespository reserveRepo;
	@Autowired
	MailUtil mailUtil;
	@Autowired
	JwtUtils jwtUtils;
	
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addDrug(@Valid @RequestBody DrugDTO drugDto) {	
		Drug drug = new Drug();
		drug.setFromDTO(drugDto);
		drug.setStars(0);
		List<Drug> replacement = new ArrayList<>();
		for(String code : drugDto.getReplacementCodes()) {
			Optional<Drug> drugRepl = drugRepo.findById(code);
			if(drugRepl.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			replacement.add(drugRepl.get());
		}
		drug.setReplacement(replacement);
		drugRepo.save(drug);
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/reserve")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> reserveDrug(@RequestHeader("Authorization") String token,@Valid @RequestBody DrugReservationDTO drugReservDto) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException, MessagingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.getById(username);
		if(patient.getPenalties()>=3) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		DrugReservation drugReserve = new DrugReservation();
		drugReserve.setPickUp(drugReservDto.getPickUp());
		Optional<Farmacy> farmacy = farmacyRepo.findById(drugReservDto.getFarmacy());
		if(farmacy.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<Drug> drug = drugRepo.findById(drugReservDto.getDrug());
		if(drug.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(farmacy.get().getDrugsQuantities().get(drug.get())<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		farmacy.get().reservedDrugQty(drug.get());
		farmacyRepo.save(farmacy.get());
		drugReserve.setDrug(drug.get());
		drugReserve.setFarmacy(farmacy.get());
		drugReserve.setShowUp(false);
		reserveRepo.save(drugReserve);
		patient.addDrugReservation(drugReserve);
		patientRepo.save(patient);
		mailUtil.confirmDrugReservation(drugReserve, username);
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/cancel/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> calcelReserve(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.getById(username);
		Optional<DrugReservation> reservation = reserveRepo.findById(id);
		if(!reservation.get().getPickUp().minusDays(1).isAfter(LocalDate.now()) && reservation.get().isShowUp()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(reservation.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(!patient.getDrugsReserved().contains(reservation.get())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		reservation.get().getFarmacy().reverseDrugQty(reservation.get().getDrug());
		farmacyRepo.save(reservation.get().getFarmacy());
		
		patient.removeDrugReservation(reservation.get());
		patientRepo.save(patient);
		reserveRepo.delete(reservation.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	
	
}
