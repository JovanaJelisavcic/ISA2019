package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.DAppointDTO;
import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.security.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/dermappoint")
public class DermAppointmentController {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	@Autowired
	DermappointRepository dermappointRepo;
	@Autowired
	DermatologistRepository dermaRepo;
	
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyPrices(@RequestHeader("Authorization") String token, @RequestBody DAppointDTO dappDTO) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Dermatologist> derm = dermaRepo.findById(dappDTO.getDerma());
		if(derm.isEmpty() || !derm.get().checkIfworksIn(farmacy.getId())) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		DermAppointment appoint = new DermAppointment(dappDTO.getPrice(), derm.get(),farmacy, dappDTO.getDateTime(),
				dappDTO.getEndTime());
		dermappointRepo.save(appoint);
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
}
