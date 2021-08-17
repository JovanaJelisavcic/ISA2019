package com.ISA2020.farmacia.entity.users;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import com.ISA2020.farmacia.entity.Drug;

@Entity
public class Patient extends UserInfo {

	@ElementCollection(targetClass=Drug.class)
	@CollectionTable(joinColumns = @JoinColumn(name = "email"))
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
	public void addAllergy(Drug allergy) {
		this.allergies.add(allergy);
	}
	

}
