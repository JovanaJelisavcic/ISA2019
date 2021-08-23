package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
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
import com.ISA2020.farmacia.entity.Promotion;
import com.ISA2020.farmacia.entity.VacationPharmacist;
import com.ISA2020.farmacia.entity.VacationStatus;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.entity.users.UserInfo;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PromotionRepository;
import com.ISA2020.farmacia.repository.VacationPharmacistRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.FilteringUtil;
import com.ISA2020.farmacia.util.MailUtil;
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
	@Autowired
	VacationPharmacistRepository vacationRepo;
	@Autowired
	FilteringUtil filteringUtil;
	   @Autowired
	   private PromotionRepository promotionRepo;
	   @Autowired
	   MailUtil mailUtil;

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
	
	
	@PostMapping("/promotion")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> postPromotion(@RequestHeader("Authorization") String token, @RequestBody Promotion promotion) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		promotion.setFarmacyId(farmacy);
		promotionRepo.save(promotion);
		mailUtil.sendPromotionEmails(farmacy, promotion);
		    
		return ResponseEntity.ok().build();
		
		
	}
	@JsonView(Views.VacationRequestsList.class)
	@GetMapping("/vacationRequests")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> getVacations(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		List<VacationPharmacist> vacations = vacationRepo.findAll();
		return new ResponseEntity<>(filteringUtil.filterVacationsPhFarmacy(vacations, farmacy.getId()), HttpStatus.OK);
		
		
	}
	
	

	@PostMapping("/acceptVacation/{vid}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> acceptVacation(@RequestHeader("Authorization") String token, @PathVariable Long vid) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<VacationPharmacist> vacation = vacationRepo.findById(vid);
		if(vacation.isEmpty()) return ResponseEntity.notFound().build();
		if(!vacation.get().getPharmacist().getFarmacy().getId().equals(farmacy.getId())) return ResponseEntity.badRequest().build();
		vacation.get().setStatus(VacationStatus.ACCEPTED);
		vacationRepo.save(vacation.get());
		mailUtil.sendConfirmVacationMail(vacation.get());
		return ResponseEntity.ok().build();
		
	}

	@PostMapping("/denyVacation/{vid}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> denyVacation(@RequestHeader("Authorization") String token, @PathVariable Long vid, @RequestBody String explanation) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException, MessagingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<VacationPharmacist> vacation = vacationRepo.findById(vid);
		if(vacation.isEmpty()) return ResponseEntity.notFound().build();
		if(!vacation.get().getPharmacist().getFarmacy().getId().equals(farmacy.getId())) return ResponseEntity.badRequest().build();
		vacation.get().setStatus(VacationStatus.DENIED);
		String[] splited = explanation.split(":");
		int first =splited[1].indexOf("\"");
		int last =splited[1].lastIndexOf("\"");
		String explanationValue =splited[1].substring(first+1, last);
		vacation.get().setExplanation(explanationValue);
		vacationRepo.save(vacation.get());
		mailUtil.sendDenyVacation(vacation.get());
		return ResponseEntity.ok().build();
		
		
	}
	
	
}
