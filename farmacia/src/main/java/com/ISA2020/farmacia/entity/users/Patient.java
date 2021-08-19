package com.ISA2020.farmacia.entity.users;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Views;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Patient extends UserInfo {

	@ElementCollection(targetClass=Drug.class)
	@CollectionTable(joinColumns = @JoinColumn(name = "email"))
	@JsonView(Views.Simple.class)
    private List<Drug> allergies;
	
	public Patient() {}
	
	public Patient(UserInfo user) {
		super(user);
	}

	public Patient(List<Drug> allergies) {
		super();
		this.allergies = allergies;
	}

	public List<Drug> getAllergies() {
		return allergies;
	}
	public void setAllergies(List<Drug> allergies) {
		this.allergies = allergies;
	}
	public void addAllergies(List<Drug> drugs) {
		this.allergies.addAll(drugs);
	}

	public void changeUserInfo(UserInfo farmacyadmin) {
		if(!farmacyadmin.getAdress().equals(super.getAdress()))
			super.setAdress(farmacyadmin.getAdress());
		if(!farmacyadmin.getCity().equals(super.getCity()))
			super.setCity(farmacyadmin.getCity());
		if(!farmacyadmin.getName().equals(super.getName()))
			super.setName(farmacyadmin.getName());
		if(!farmacyadmin.getPhoneNum().equals(super.getPhoneNum()))
			super.setPhoneNum(farmacyadmin.getPhoneNum());
		if(!farmacyadmin.getState().equals(super.getState()))
			super.setState(farmacyadmin.getState());
		if(!farmacyadmin.getSurname().equals(super.getSurname()))
			super.setSurname(farmacyadmin.getSurname());
		
	}


	

}
