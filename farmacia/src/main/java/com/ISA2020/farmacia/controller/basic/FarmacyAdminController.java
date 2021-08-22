package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

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

import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Price;
import com.ISA2020.farmacia.entity.PriceDTO;
import com.ISA2020.farmacia.entity.PriceListDTO;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.entity.users.UserInfo;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/fadmin")
public class FarmacyAdminController {

	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	DermappointRepository dermappointRepo;

	@JsonView(Views.SimpleUser.class)
	@GetMapping("/profile")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public FarmacyAdmin myfarmProfile(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return  farmAdminRepo.findById(username).get();
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UserInfo farmacyadmin) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		FarmacyAdmin newOne = farmAdminRepo.findById(username).get();
		if(!farmacyadmin.getEmail().equals(username)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		newOne.changeUserInfo(farmacyadmin);
		farmAdminRepo.save(newOne);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping("/price")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyPrices(@RequestHeader("Authorization") String token, @RequestBody PriceDTO priceDTO) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Drug> drug = drugRepo.findById(priceDTO.getDrug());
		if(drug.isEmpty() || !farmacy.getDrugs().contains(drug.get())) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Price price = new Price(priceDTO.getPrice(),drug.get(), farmacy, priceDTO.getStandsFrom(), priceDTO.getStandsUntil());
		boolean check2 = farmacy.addPrice(price);
		if( !check2) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		farmacyRepo.save(farmacy);
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@JsonView(Views.PricesList.class)
	@GetMapping("/getFarmacyPrices")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public PriceListDTO allfarmacyPrices(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		List<Price> drugPrices = farmacy.getPrices();
		List<DermAppointment> appointPrices = dermappointRepo.findByFarmacyId(farmacy.getId());
		return new PriceListDTO(drugPrices,appointPrices);
		
		
	}

}
