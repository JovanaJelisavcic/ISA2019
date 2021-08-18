package com.ISA2020.farmacia.controller.basic;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.repository.FarmacyRepository;

@RestController
@RequestMapping("/retrieve")
public class RetrieveController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RetrieveController.class);
	
	@Autowired 
	FarmacyRepository farmRepo;

	@GetMapping("/farmacies")
	public List<Farmacy> getForUnautho() {
		return  farmRepo.getFiveHighestRated();
	}
	
}
