package com.ISA2020.farmacia.security;

import javax.validation.constraints.NotBlank;

import com.ISA2020.farmacia.util.FieldMatch;
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class PasswordChangeRequest {
	@NotBlank(message="Old password is mandatory")
	private String oldPassword;
	@NotBlank(message="New password is mandatory")
	private String password;
	@NotBlank(message="Confirmation password is mandatory")
	private String confirmPassword;
	public PasswordChangeRequest() {}
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
