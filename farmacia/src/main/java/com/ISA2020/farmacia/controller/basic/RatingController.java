package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Rating;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.repository.PharmacistRepository;
import com.ISA2020.farmacia.repository.RatingRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.SubjectUtils;

@RestController
@RequestMapping("/rating")
public class RatingController {
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DermatologistRepository dermaRepo;
	@Autowired
	PharmacistRepository pharmaRepo;
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	SubjectUtils subjectUtils;
	
	
	@PostMapping("/rate")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> rateSomething(@RequestHeader("Authorization") String token,@Valid @RequestBody Rating rating) throws UnsupportedEncodingException, MessagingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Patient patient = patientRepo.findById(username).get();
		if(!subjectUtils.checkPossible(patient,rating.getSubject(), rating.getSubjectId())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<Rating> optional = ratingRepository.findThisRating(patient.getEmail(), rating.getSubjectId());
	if(optional.isPresent()) {
		optional.get().setRatingStars(rating.getRatingStars());
		ratingRepository.save(optional.get());
		changeSubjectRating(rating);
		return ResponseEntity.ok().build();	
	}else {
		rating.setPatient(patient);
		ratingRepository.save(rating);
		changeSubjectRating(rating);
		return ResponseEntity.ok().build();	
	}
		
	}


	private void changeSubjectRating(Rating rating) {
		switch(rating.getSubject()) {
		  case DERMATOLOGIST:
			  Dermatologist derma = dermaRepo.getById(rating.getSubjectId());
			  derma.setStars(ratingRepository.getSubjectStars(rating.getSubjectId()));
			  dermaRepo.save(derma);
			  break;
		  case PHARMACIST:
			  Pharmacist pharma = pharmaRepo.getById(rating.getSubjectId());
			  pharma.setStars(ratingRepository.getSubjectStars(rating.getSubjectId()));
			  pharmaRepo.save(pharma);
			  break;
		  case FARMACY:
			  Farmacy farmacy = farmacyRepo.getById(rating.getSubjectId());
			  farmacy.setStars(ratingRepository.getSubjectStars(rating.getSubjectId()));
			  farmacyRepo.save(farmacy);
			  break;
		  case DRUG:
			  Drug drug  = drugRepo.getById(rating.getSubjectId());
			  drug.setStars(ratingRepository.getSubjectStars(rating.getSubjectId()));
			  drugRepo.save(drug);
			  break;
		}
		
	}
}
