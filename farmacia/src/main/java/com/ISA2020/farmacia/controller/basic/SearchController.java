package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PharmacistRepository;
import com.ISA2020.farmacia.util.FilteringUtil;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;



@RestController
@RequestMapping("/search")
public class SearchController {
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	PharmacistRepository pharmacistRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	DermatologistRepository dermaRepo;
	@Autowired 
	FilteringUtil filteringUtil;
	
	@JsonView(Views.SimpleFarmacy.class)
	@GetMapping(value="/farmacy/{criteria}")
	public ResponseEntity<List<Farmacy>> searchFarmacyByNameOrPlace(@PathVariable String criteria){
		 StringBuilder sb = new StringBuilder(criteria.concat("%"));
		 sb.insert(0,"%");
		List<Farmacy> farmacies = farmacyRepo.findByAdressLikeIgnoreCaseOrNameLikeIgnoreCase(sb.toString(), sb.toString());
		if(farmacies.isEmpty() || farmacies==null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<List<Farmacy>>(farmacies, HttpStatus.OK);
	}
	
	@JsonView(Views.DrugsUnautho.class)
	@GetMapping(value="/drugs/{name}")
	public ResponseEntity<List<Drug>> searchDrugsByName(@PathVariable String name){
		 StringBuilder sb = new StringBuilder(name.concat("%"));
		 sb.insert(0,"%");
		List<Drug> drugs = drugRepo.findByNameLikeIgnoreCase(sb.toString());
		if(drugs.isEmpty() || drugs==null) {
			return ResponseEntity.notFound().build();
		}
		List<Drug> filtered= filteringUtil.filterPricesAndFields(drugs);
		if(filtered.isEmpty() || filtered==null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<List<Drug>>(drugs, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('PATIENT')")
	@JsonView(Views.SearchPharmacistsForPatient.class)
	@GetMapping(value="/pharmacists/{name}")
	public ResponseEntity<List<Pharmacist>> searchPharmacistsByName(@PathVariable String name){
		 StringBuilder sb = new StringBuilder(name.concat("%"));
		 sb.insert(0,"%");
		List<Pharmacist> pharmacists = pharmacistRepo.findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(sb.toString(), sb.toString());
		if(pharmacists.isEmpty() || pharmacists==null) {
			return ResponseEntity.notFound().build();
		}
		
		return new ResponseEntity<List<Pharmacist>>(filteringUtil.filterAdress(pharmacists), HttpStatus.OK);
	}

	@JsonView(Views.DermaInfo.class)
	@GetMapping("/dermatologist/{parametar}")
	@PreAuthorize("hasAuthority('PATIENT')")
	public ResponseEntity<Object> searchFarmacyDerma(@PathVariable String parametar) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		 StringBuilder sb = new StringBuilder(parametar.concat("%"));
		 sb.insert(0,"%");
		 List<Dermatologist> dermas = dermaRepo.findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(sb.toString(),sb.toString());
		if(dermas.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<Object>(filteringUtil.filterAdressDerma(dermas), HttpStatus.OK);		
		 
	}

	
	
}
