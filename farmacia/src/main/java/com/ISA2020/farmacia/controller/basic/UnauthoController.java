package com.ISA2020.farmacia.controller.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.util.FilteringUtil;
import com.fasterxml.jackson.annotation.JsonView;



@RestController
@RequestMapping("/unautho")
public class UnauthoController {
	
	
	@Autowired 
	FarmacyRepository farmRepo;
	@Autowired 
	DrugRepository drugRepo;
	@Autowired 
	FilteringUtil filteringUtil;
	
	@JsonView(Views.VerySimpleFarmacy.class)
	@GetMapping("/farmacies")
	public List<Farmacy> getFarmForUnautho() {
		return  farmRepo.getFiveHighestRated();
	}
	@JsonView(Views.DrugsUnautho.class)
	@GetMapping("/drugs")
	public List<Drug> getDrugForUnautho() {
		List<Drug> drugs =  drugRepo.getFiveRandom();
		return filteringUtil.filterPricesAndFields(drugs);
	}
	
}
