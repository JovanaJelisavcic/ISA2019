package com.ISA2020.farmacia.entity.users;



import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.ISA2020.farmacia.entity.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@MappedSuperclass
public class UserInfo {
	
	@Id
	@Column(nullable=false, unique =true,
		        updatable=false)
	@JsonView(Views.Simple.class)
	private String email;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String name;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String surname;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String adress;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String city;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String state;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String phoneNum;
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInfo() {}

	public UserInfo(String email, String name, String surname, String adress, String city, String state, String phoneNum, String password) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.adress = adress;
		this.city = city;
		this.state = state;
		this.phoneNum = phoneNum;
		this.password=password;
	}
	public UserInfo(UserInfo user) {
		super();
		this.email = user.email;
		this.name = user.name;
		this.surname = user.surname;
		this.adress = user.adress;
		this.city = user.city;
		this.state = user.state;
		this.phoneNum = user.phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	

}
