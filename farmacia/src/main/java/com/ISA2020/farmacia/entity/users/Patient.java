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

	public void changeUserInfo(UserInfo user) {
		if(!user.getAdress().equals(super.getAdress()) && user.getAdress()!=null)
			super.setAdress(user.getAdress());
		if(!user.getCity().equals(super.getCity()) && user.getCity()!=null)
			super.setCity(user.getCity());
		if(!user.getName().equals(super.getName()) && user.getName()!=null)
			super.setName(user.getName());
		if(!user.getPhoneNum().equals(super.getPhoneNum()) && user.getPhoneNum()!=null)
			super.setPhoneNum(user.getPhoneNum());
		if(!user.getState().equals(super.getState()) && user.getState()!=null)
			super.setState(user.getState());
		if(!user.getSurname().equals(super.getSurname()) && user.getSurname()!=null)
			super.setSurname(user.getSurname());
		
	}

	

}
