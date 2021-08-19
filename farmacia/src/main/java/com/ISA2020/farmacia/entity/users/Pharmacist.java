package com.ISA2020.farmacia.entity.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Views;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Pharmacist extends UserInfo {

	@OneToOne
	@JoinColumn(name = "farmacyId")
	@JsonView(Views.VerySimple.class)
	private Farmacy farmacy;
	@JsonView(Views.VerySimple.class)
	float stars;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	String worksFrom;
	@JsonView(Views.Simple.class)
	@Column(nullable=false)
	String worksTo;
	
	public Pharmacist() {}

	public Pharmacist(String username, String name, String surname, Farmacy farmacy, float stars, String worksFrom,
			String worksTo) {
		super();
		this.farmacy = farmacy;
		this.stars = stars;
		this.worksFrom = worksFrom;
		this.worksTo = worksTo;
	}



	public Farmacy getFarmacy() {
		return farmacy;
	}

	public void setFarmacy(Farmacy farmacy) {
		this.farmacy = farmacy;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public String getWorksFrom() {
		return worksFrom;
	}

	public void setWorksFrom(String worksFrom) {
		this.worksFrom = worksFrom;
	}

	public String getWorksTo() {
		return worksTo;
	}

	public void setWorksTo(String worksTo) {
		this.worksTo = worksTo;
	}
	
}
