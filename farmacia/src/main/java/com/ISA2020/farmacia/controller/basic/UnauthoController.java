package com.ISA2020.farmacia.controller.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.intercations.Counseling;
import com.ISA2020.farmacia.entity.intercations.DermAppointment;
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.repository.CounselingRepository;
import com.ISA2020.farmacia.repository.DermappointRepository;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.DrugReservationRespository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.PatientRepository;
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
	DrugReservationRespository drugReservRepo;
	@Autowired 
	CounselingRepository counslRepo;
	@Autowired 
	DermappointRepository dermapRepo;
	@Autowired 
	PatientRepository patientRepo;
	
	
	
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
	
	@PostMapping("mock/drugPickedUp/{id}")
	public void drugPickedUp(@PathVariable Long id) {
			DrugReservation d =drugReservRepo.getById(id);
			d.setShowUp(true);
			drugReservRepo.save(d);
	}
	@PostMapping("mock/drugNotPicked/{id}/{eamil}")
	public void drugNotPicked(@PathVariable Long id, @PathVariable String email) {
		DrugReservation d =drugReservRepo.getById(id);
		d.setShowUp(false);
		Patient patient = patientRepo.getById(email);
		patient.setPenalties(patient.getPenalties()+1);
		patientRepo.save(patient);
		drugReservRepo.save(d);
	}
	@PostMapping("mock/counslCame/{id}")
	public void counslCame(@PathVariable Long id) {
		Counseling d =counslRepo.getById(id);
		d.setShowUp(true);
		counslRepo.save(d);
	}
	@PostMapping("mock/counsNcome/{id}/{email}")
	public void counsNcome(@PathVariable Long id, @PathVariable String email) {
		Counseling d =counslRepo.getById(id);
		d.setShowUp(false);
		Patient patient = patientRepo.getById(email);
		patient.setPenalties(patient.getPenalties()+1);
		patientRepo.save(patient);
		counslRepo.save(d);
	}
	@PostMapping("mock/dermapCame/{id}")
	public void dermapCame(@PathVariable Long id) {
		DermAppointment d =dermapRepo.getById(id);
		d.setDone(true);
		dermapRepo.save(d);
	}
	@PostMapping("mock/dermapNcome/{id}/{email}")
	public void dermapNcome(@PathVariable Long id, @PathVariable String email) {
		DermAppointment d =dermapRepo.getById(id);
		d.setDone(false);
		Patient patient = patientRepo.getById(email);
		patient.setPenalties(patient.getPenalties()+1);
		patientRepo.save(patient);
		dermapRepo.save(d);
	}
	

	
}
