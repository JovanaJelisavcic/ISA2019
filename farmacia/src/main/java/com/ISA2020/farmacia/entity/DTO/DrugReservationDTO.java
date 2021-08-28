package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class DrugReservationDTO {


    private String drug;
 	
    private String farmacy;

    @NotNull(message="Pick up date is mandatory")
 	@Future(message="Pick up date has to be in the future")
    private LocalDate pickUp;

    public DrugReservationDTO() {}
	public DrugReservationDTO(String drug, String farmacy, LocalDate pickUp) {
		super();
		this.drug = drug;
		this.farmacy = farmacy;
		this.pickUp = pickUp;
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
	public LocalDate getPickUp() {
		return pickUp;
	}
	public void setPickUp(LocalDate pickUp) {
		this.pickUp = pickUp;
	}
    
    
}
