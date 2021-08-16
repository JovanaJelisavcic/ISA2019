package com.ISA2020.farmacia.entity.users;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
	
	@Id
	@Column(nullable=false, unique =true,
		        updatable=false)
	private String email;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String surname;
	@Column(nullable=false)
	private String adress;
	@Column(nullable=false)
	private String city;
	@Column(nullable=false)
	private String state;
	@Column(nullable=false)
	private String phoneNum;
	
	public User() {}

	public User(String email, String name, String surname, String adress, String city, String state, String phoneNum) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.adress = adress;
		this.city = city;
		this.state = state;
		this.phoneNum = phoneNum;
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
