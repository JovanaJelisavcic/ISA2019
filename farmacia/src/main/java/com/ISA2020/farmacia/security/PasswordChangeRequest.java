package com.ISA2020.farmacia.security;

public class PasswordChangeRequest {
	private String oldPassword;
	private String password;
	public PasswordChangeRequest() {}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}

}
