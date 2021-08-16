package com.ISA2020.farmacia.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDetails registerNewUserAccount(Credentials userDto) throws Exception {
		
	    if (userRepo.findByUsername(userDto.getUsername())!= null) {
	        throw new Exception(
	          "There is an account with that username:" + userDto.getUsername());
	    }
	    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
	    userRepo.save(userDto);
	   return new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),new ArrayList<>());
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Credentials user = userRepo.findByUsername(username);
		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	public boolean checkIfValidOldPassword(UserDetails user, String oldPassword) {
		Credentials old =userRepo.findByUsername(user.getUsername());
		if(passwordEncoder.matches(oldPassword, old.getPassword())) return true;
		else return false;
		
	}

	public void changeUserPassword(UserDetails user, String password) {
		Credentials old =userRepo.findByUsername(user.getUsername());
		old.setPassword(passwordEncoder.encode(password));
		userRepo.save(old);
		
	}


}

