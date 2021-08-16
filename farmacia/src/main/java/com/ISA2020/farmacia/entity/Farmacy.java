package com.ISA2020.farmacia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Farmacy {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String farmacyId;
	@Column(nullable=false)
	private String name;
	 @Column(nullable=false)
	private String adress;
	private float stars;
	
	public Farmacy() {}
	
	public Farmacy(String name, String adress, float stars) {
		super();
		this.name = name;
		this.adress = adress;
		this.stars = stars;
	}
	public String getId() {
		return farmacyId;
	}
	public void setId(String id) {
		this.farmacyId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	
	
	
}
