package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.repository.DrugRepository;
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
	DrugRepository drugRepo;
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
	
	@JsonView(Views.Simple.class)
	@GetMapping("/drugs")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public List<Drug> farmacyDrugs(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		return farmAdminRepo.findById(username).get().getFarmacy().getDrugs();
		 
	}
	
	
	@JsonView(Views.Simple.class)
	@GetMapping("/search/{parametar}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<Object> searchFarmacyDrugs(@RequestHeader("Authorization") String token,@PathVariable String parametar) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		 StringBuilder sb = new StringBuilder(parametar.concat("%"));
		 sb.insert(0,"%");
		 List<Drug> drugs = drugRepo.findByNameLikeIgnoreCaseOrCodeLikeIgnoreCase(sb.toString(),sb.toString());
		if(drugs.isEmpty() || drugs==null) {
			return ResponseEntity.notFound().build();
		}
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy = farmAdminRepo.findById(username).get().getFarmacy();
		
		drugs.removeIf(drug -> !drug.getFarmacies().contains(farmacy));
		return new ResponseEntity<Object>(drugs, HttpStatus.OK);
		
		
		
	
		 
		 
	}
	
	@PostMapping("/drug/{code}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyDrugs(@RequestHeader("Authorization") String token, @PathVariable String code) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Drug> drug = drugRepo.findById(code);
		if(drug.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		boolean check = drug.get().addFarmacy(farmacy);
		if(!check) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		drugRepo.save(drug.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	

	@DeleteMapping("/drug/{code}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> farmacyDrug(@RequestHeader("Authorization") String token, @PathVariable String code) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		Optional<Drug> drug = drugRepo.findById(code);
		if(drug.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		boolean check = drug.get().deleteFarmacy(farmacy);
		if(!check) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		drugRepo.save(drug.get());
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}

}
