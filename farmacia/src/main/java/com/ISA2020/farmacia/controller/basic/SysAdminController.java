package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Farmacy;
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
import com.ISA2020.farmacia.security.ERole;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.security.Role;
import com.ISA2020.farmacia.security.RoleRepository;
import com.ISA2020.farmacia.security.User;
import com.ISA2020.farmacia.security.UserDetailsServiceImpl;
import com.ISA2020.farmacia.security.UserRepository;

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
	 private UserDetailsServiceImpl service;

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
}
