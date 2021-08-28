package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDate;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PriceDTO {
	@NotNull(message="Price is mandatory")
	private float price;
	@NotBlank(message="Drug cannot be blank")
	private String drug;
		@NotBlank(message="Farmacy cannot be blank")
	    private String farmacy;
		@NotNull(message="Date is mandatory")
	 	@Future(message="Date has to be in the future")
	 private LocalDate standsFrom;
		@NotNull(message="Date is mandatory")
	 	@Future(message="Date has to be in the future")
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
	 @AssertTrue public boolean isValidRange() {
		    return standsFrom.isBefore(standsUntil);
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
