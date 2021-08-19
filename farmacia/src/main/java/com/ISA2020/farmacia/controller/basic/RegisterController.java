package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.UserInfo;
import com.ISA2020.farmacia.repository.PatientRepository;
import com.ISA2020.farmacia.security.ERole;
import com.ISA2020.farmacia.security.JwtResponse;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.security.PasswordChangeRequest;
import com.ISA2020.farmacia.security.Role;
import com.ISA2020.farmacia.security.RoleRepository;
import com.ISA2020.farmacia.security.User;
import com.ISA2020.farmacia.security.UserDetailsImpl;
import com.ISA2020.farmacia.security.UserDetailsServiceImpl;
import com.ISA2020.farmacia.security.UserRepository;





@RestController
@RequestMapping("/register")
public class RegisterController {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);
	@Autowired
	AuthenticationManager authenticationManager;

	 @Autowired
	 private UserDetailsServiceImpl service;
	@Autowired
	PatientRepository patientRepo;

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;


	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) throws UnsupportedEncodingException, URISyntaxException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		logger.info(loginRequest.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(jwt,
												 loginRequest.getUsername(), roles));
	}
	
	
	@GetMapping("/all")
	public String allAccess() {
		return "signup_form";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAuthority('PATIENT') or hasAuthority('FARMACY_ADMIN') or hasAuthority('SYS_ADMIN')")
	public String userAccess() {
		return "any user";
	}

	@GetMapping("/farm")
	@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
	public String farmAccess() {
		return "farmacy admin";
	}
	@GetMapping("/patient")
	@PreAuthorize("hasAuthority('PATIENT')")
	public String patientAccess() {
		return "farmacy admin";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('SYS_ADMIN')")
	public String adminAccess() {
		return "Admin";
	}

	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody UserInfo signUpRequest, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		if (userRepository.existsByUsername(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest().build();
		}

		Patient otherCheck =  patientRepo.save(new Patient(signUpRequest));
		if(otherCheck!=null) {
			logger.info(signUpRequest.getPassword());
			User user = new User(signUpRequest.getEmail(), 
					 signUpRequest.getPassword());
				Set<Role> userRole = new HashSet<>();
					userRole.add(roleRepository.findByName(ERole.PATIENT));
					user.setRoles(userRole);
			service.register(user, getSiteURL(request));
			return ResponseEntity.ok().build();
		}else return ResponseEntity.badRequest().build();
			
	}
	
	 private String getSiteURL(HttpServletRequest request) {
	        String siteURL = request.getRequestURL().toString();
	        return siteURL.replace(request.getServletPath(), "");
	    }  
	 
	 @GetMapping("/verify")
	 public String verifyUser(@Param("code") String code) {
	     if (service.verify(code)) {
	         return "verify_success";
	     } else {
	         return "verify_fail";
	     }
	 }
	 
	 @PostMapping("/changePassword")
	 @PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChange) throws URISyntaxException {
		 UserDetails user = service.loadUserByUsername(
			      SecurityContextHolder.getContext().getAuthentication().getName());
			    
			    if (!service.checkIfValidOldPassword(user, passwordChange.getOldPassword())) {
			       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			    }
			    service.changeUserPassword(user, passwordChange.getPassword());
			    HttpHeaders headers = new HttpHeaders();
				headers.setLocation(new URI("/signin"));   
				return new ResponseEntity<>(headers,HttpStatus.OK);
				
		}
}
