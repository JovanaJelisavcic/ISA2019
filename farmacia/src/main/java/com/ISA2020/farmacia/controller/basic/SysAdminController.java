package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.VacationDermatologist;
import com.ISA2020.farmacia.entity.VacationStatus;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.DTO.FarmacyAdminDTO;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.entity.users.Supplier;
import com.ISA2020.farmacia.entity.users.SysAdmin;
import com.ISA2020.farmacia.repository.DermatologistRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.SupplierRepository;
import com.ISA2020.farmacia.repository.SysAdminRepository;
import com.ISA2020.farmacia.repository.VacationDermaRepository;
import com.ISA2020.farmacia.security.ERole;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.security.Role;
import com.ISA2020.farmacia.security.RoleRepository;
import com.ISA2020.farmacia.security.User;
import com.ISA2020.farmacia.security.UserDetailsServiceImpl;
import com.ISA2020.farmacia.security.UserRepository;
import com.ISA2020.farmacia.util.MailUtil;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/sadmin")
public class SysAdminController {
	

	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	SysAdminRepository sysAdminRepo;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	SupplierRepository supplierRepo;
	
	@Autowired
	DermatologistRepository dermaRepository;
	@Autowired
	VacationDermaRepository vacationRepository;
	@Autowired
	private UserDetailsServiceImpl service;
	@Autowired
	MailUtil mailUtil;

	@PostMapping("/farmacy")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addFarmacy(@RequestBody Farmacy farmacy) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		farmacy.setStars(0);
		farmacyRepo.save(farmacy);
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/fadmin")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addFadmin( @RequestBody FarmacyAdminDTO farmacyAdmin) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		Optional<Farmacy> farmacy = farmacyRepo.findById(farmacyAdmin.getFarmacyId());
		if(farmacy.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (userRepository.existsByUsername(farmacyAdmin.getEmail())) {
			return ResponseEntity
					.badRequest().build();
		}
		FarmacyAdmin fadmin = new FarmacyAdmin();
		fadmin.setByUserInfo(farmacyAdmin);
		fadmin.setFarmacy(farmacy.get());
		farmAdminRepo.save(fadmin);
		
			User user = new User(farmacyAdmin.getEmail(), 
					 "initialPassword");
				Set<Role> userRole = new HashSet<>();
					userRole.add(roleRepository.findByName(ERole.FARMACY_ADMIN));
					user.setRoles(userRole);
			service.registerAdmin(user);
		
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	@PostMapping("/otherSadmin")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addSadmin( @RequestBody SysAdmin sysAdmin) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		if (userRepository.existsByUsername(sysAdmin.getEmail())) {
			return ResponseEntity
					.badRequest().build();
		}
		sysAdminRepo.save(sysAdmin);
		
			User user = new User(sysAdmin.getEmail(), 
					 "initialPassword");
				Set<Role> userRole = new HashSet<>();
					userRole.add(roleRepository.findByName(ERole.SYS_ADMIN));
					user.setRoles(userRole);
			service.registerAdmin(user);
		
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	

	@PostMapping("/addDermatologist")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addDermatologist( @RequestBody Dermatologist dermatologist) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		dermatologist.setStars(0);
		dermaRepository.save(dermatologist);
		
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	
	@PostMapping("/addSupplier")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> addSupplier( @RequestBody Supplier supplier) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		supplierRepo.save(supplier);
		
		return new ResponseEntity<>(HttpStatus.OK);
		 
	}
	
	
	
	@JsonView(Views.VacationRequestsList.class)
	@GetMapping("/vacationRequests")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> getVacations() throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		return new ResponseEntity<>(vacationRepository.findAll(), HttpStatus.OK);
		
		
	}
	
	

	@PostMapping("/acceptVacation/{vid}")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> acceptVacation(@PathVariable Long vid) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
		Optional<VacationDermatologist> vacation = vacationRepository.findById(vid);
		if(vacation.isEmpty()) return ResponseEntity.notFound().build();
		if(!vacation.get().getStatus().equals(VacationStatus.CREATED)) return ResponseEntity.badRequest().build();
		vacation.get().setStatus(VacationStatus.ACCEPTED);
		vacationRepository.save(vacation.get());
		mailUtil.sendConfirmVacationMail(vacation.get());
		return ResponseEntity.ok().build();
		
		
	}

	@PostMapping("/denyVacation/{vid}")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public ResponseEntity<?> denyVacation(@PathVariable Long vid, @RequestBody String explanation) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException, MessagingException {	
		Optional<VacationDermatologist> vacation = vacationRepository.findById(vid);
		if(vacation.isEmpty()) return ResponseEntity.notFound().build();
		if(!vacation.get().getStatus().equals(VacationStatus.CREATED)) return ResponseEntity.badRequest().build();
		vacation.get().setStatus(VacationStatus.DENIED);
		String[] splited = explanation.split(":");
		int first =splited[1].indexOf("\"");
		int last =splited[1].lastIndexOf("\"");
		String explanationValue =splited[1].substring(first+1, last);
		vacation.get().setExplanation(explanationValue);
		vacationRepository.save(vacation.get());
		mailUtil.sendDenyVacation(vacation.get());
		return ResponseEntity.ok().build();
		
		
	}
	
	
	
}
