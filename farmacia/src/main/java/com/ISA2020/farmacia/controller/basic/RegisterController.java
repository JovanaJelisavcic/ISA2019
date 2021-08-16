package com.ISA2020.farmacia.controller.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.security.JWTRequest;
import com.ISA2020.farmacia.security.JWTResponse;
import com.ISA2020.farmacia.security.JWTUtility;
import com.ISA2020.farmacia.security.UserService;





@RestController
public class RegisterController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/login")
	public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest ) throws Exception {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch(BadCredentialsException e) {
			throw new Exception("Invalid_credentials", e);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
		
		final String token = jwtUtility.generateToken(userDetails);
		return new JWTResponse(token);
	}
	@PostMapping("/try")
	public String authtry() throws Exception {
		return "jes";
	}
}
