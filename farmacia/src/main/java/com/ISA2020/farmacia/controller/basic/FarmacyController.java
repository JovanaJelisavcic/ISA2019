package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/farmacy")
public class FarmacyController {
	
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	
	@JsonView(Views.Simple.class)
	@GetMapping("/profile")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public Farmacy myfarmProfile(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return  farmAdminRepo.findById(username).get().getFarmacy();
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody Farmacy farmacy) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		FarmacyAdmin admin = farmAdminRepo.findById(username).get();
		if(!admin.getFarmacy().getId().equals(farmacy.getId())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		farmacy.setStars(farmacyRepo.findById(admin.getFarmacy().getId()).get().getStars());
		farmacyRepo.save(farmacy);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
