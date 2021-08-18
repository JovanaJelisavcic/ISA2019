package com.ISA2020.farmacia.controller.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.repository.FarmacyRepository;



@RestController
@RequestMapping("/search")
public class SearchController {
	@Autowired
	FarmacyRepository farmacyRepo;
	
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
	
	
}
