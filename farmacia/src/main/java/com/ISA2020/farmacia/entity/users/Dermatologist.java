package com.ISA2020.farmacia.entity.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.Farmacy;

@Entity
public class Dermatologist extends User {

	@ManyToOne
	@JoinColumn(name = "farmacyId")
	private Farmacy farmacy;
	float stars;
	@Column(nullable=false)
	String worksFrom;
	@Column(nullable=false)
	String worksTo;
	
	public Dermatologist() {}

	public Dermatologist(Farmacy farmacy, float stars, String worksFrom, String worksTo) {
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
