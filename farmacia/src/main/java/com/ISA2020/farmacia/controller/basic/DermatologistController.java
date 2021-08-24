package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.WorkingHours;
import com.ISA2020.farmacia.entity.DTO.DermatologistDTO;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.WorkingHoursRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.FilteringUtil;
import com.fasterxml.jackson.annotation.JsonView;

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
	@Autowired 
	FilteringUtil filteringUtil;

	@JsonView(Views.DermaInfo.class)
	@GetMapping("/farmacys")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<?> myfarmacyDermatologists(@RequestHeader("Authorization") String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
		List<WorkingHours> list = wARepo.findAllByFarmacyId(farmacy.getId());
		List<Dermatologist> resp = new ArrayList<>();
		for(WorkingHours wh : list) {
			if(!resp.contains(wh.getDermatologist()))
				resp.add(wh.getDermatologist());
		}
 
		if(resp.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(filteringUtil.filterAdressDerma(resp), HttpStatus.OK);
	}
	
	@JsonView(Views.DermaInfo.class)
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> allDermatologists() {	
		List<Dermatologist> list = dermaRepo.findAll();
		if(list.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(filteringUtil.filterAdressDerma(list), HttpStatus.OK);
	}
	
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
	
	
	@JsonView(Views.DermaInfo.class)
	@GetMapping("/search/{parametar}")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public ResponseEntity<Object> searchFarmacyDerma(@RequestHeader("Authorization") String token,@PathVariable String parametar) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		 StringBuilder sb = new StringBuilder(parametar.concat("%"));
		 sb.insert(0,"%");
		 List<Dermatologist> dermas = dermaRepo.findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(sb.toString(),sb.toString());
		if(dermas.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
		Farmacy farmacy = farmAdminRepo.findById(username).get().getFarmacy();
		
		List<Dermatologist> filtered = filteringUtil.filterDermas(dermas, farmacy);
		if(filtered.isEmpty()) return ResponseEntity.notFound().build();
		return new ResponseEntity<Object>(filteringUtil.filterAdressDerma(filtered), HttpStatus.OK);		
		 
	}

	
	@JsonView(Views.VerySimpleUser.class)
	@GetMapping("/farmacys/{id}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<?> farmacyDermatologists( @PathVariable String id){	
		List<WorkingHours> list = wARepo.findAllByFarmacyId(id);
		List<Dermatologist> resp = new ArrayList<>();
		for(WorkingHours wh : list) {
			if(!resp.contains(wh.getDermatologist()))
				resp.add(wh.getDermatologist());
		}
 
		if(resp.isEmpty() ) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
}
