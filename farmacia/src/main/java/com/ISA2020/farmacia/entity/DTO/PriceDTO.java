package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDate;

public class PriceDTO {

	private float price;
	
	private String drug;

	    private String farmacy;
	 
	 private LocalDate standsFrom;
	 private LocalDate standsUntil;
	 public PriceDTO() {}
	public PriceDTO(float price, String drug, String farmacy, LocalDate standsFrom, LocalDate standsUntil) {
		super();
		this.price = price;
		this.drug = drug;
		this.farmacy = farmacy;
		this.standsFrom = standsFrom;
		this.standsUntil = standsUntil;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public String getFarmacy() {
		return farmacy;
	}
	public void setFarmacy(String farmacy) {
		this.farmacy = farmacy;
	}
	public LocalDate getStandsFrom() {
		return standsFrom;
	}
	public void setStandsFrom(LocalDate standsFrom) {
		this.standsFrom = standsFrom;
	}
	public LocalDate getStandsUntil() {
		return standsUntil;
	}
	public void setStandsUntil(LocalDate standsUntil) {
		this.standsUntil = standsUntil;
	}
	 
	 

}
