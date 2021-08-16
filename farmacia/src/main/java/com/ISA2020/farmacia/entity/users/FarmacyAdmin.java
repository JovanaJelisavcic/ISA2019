package com.ISA2020.farmacia.entity.users;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.Farmacy;

@Entity
public class FarmacyAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "farmacyId")
	private Farmacy farmacy;
	private String description;
	
	public FarmacyAdmin() {}
	
	public FarmacyAdmin(Farmacy farmacy, String description) {
		super();
		this.farmacy = farmacy;
		this.description = description;
	}

	public Farmacy getFarmacy() {
		return farmacy;
	}
	public void setFarmacy(Farmacy farmacy) {
		this.farmacy = farmacy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
