package com.ISA2020.farmacia.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Credentials {
	
	@Id
	private String username;
	 @Column(nullable=false)
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	

	public Credentials() {
		
	}

	public Credentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
