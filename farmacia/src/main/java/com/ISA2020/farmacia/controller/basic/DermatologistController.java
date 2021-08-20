package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.WorkingHours;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.DermatologistDTO;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.WorkingHoursRepository;
import com.ISA2020.farmacia.security.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/dermatologist")
public class DermatologistController {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	@Autowired
	DermatologistRepository dermaRepo;
	@Autowired
	WorkingHoursRepository wARepo;

	
	/*@JsonView(Views.Simple.class)
	@GetMapping("/farmacys")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyDermatologists(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		List<Pharmacist> list = pharmacistRepo.findByFarmacyId(farmacy.getId());
		if(list.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(list, HttpStatus.OK);
	}*/
	
	@PostMapping("/addToFarmacy")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> addDermatologist(@RequestHeader("Authorization") String token, @RequestBody DermatologistDTO dermaDTO) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		Optional<Dermatologist> derma = dermaRepo.findById(dermaDTO.getEmail());
		if(!dermaRepo.findById(dermaDTO.getEmail()).isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		WorkingHours wA = new WorkingHours();
		wA.setDermatologist(derma.get());
		wA.setFarmacy(farmacy);
		
		if(derma.get().checkIfAvailable(dermaDTO.getWorksFrom(), dermaDTO.getWorksTo())) {
			wA.setWorksFrom(dermaDTO.getWorksFrom());
			wA.setWorksTo(dermaDTO.getWorksTo());
			derma.get().addWorkingHours(wA);
			dermaRepo.save(derma.get());
			//wARepo.save(wA);
			return ResponseEntity.ok().build();
		}else return ResponseEntity.badRequest().build();
		 
	}
	
	@DeleteMapping("/delete/{email}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> deleteDerma(@RequestHeader("Authorization") String token, @PathVariable String email) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {		
		Optional<Dermatologist> derma = dermaRepo.findById(email);
		if(derma.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		boolean check = derma.get().removeWorkingHours(farmacy.getId());
		if(!check) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		dermaRepo.save(derma.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
}
