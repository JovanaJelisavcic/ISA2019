package com.ISA2020.farmacia.entity.intercations;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Views;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class DrugReservation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.ReservedDrugsList.class)
    private Long reservationId;
 	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
 	@JsonView(Views.ReservedDrugsList.class)
    private Drug drug;
 	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmacy_id")
 	@JsonView(Views.ReservedDrugsList.class)
    private Farmacy farmacy;
 	@JsonView(Views.ReservedDrugsList.class)
    private LocalDate pickUp;
 	private boolean showUp;
    public DrugReservation() {}
	public DrugReservation(Drug drug, Farmacy farmacy, LocalDate pickUp) {
		super();
		this.drug = drug;
		this.farmacy = farmacy;
		this.pickUp = pickUp;
	}
	public Long getReservationId() {
		return reservationId;
	}
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
	public Drug getDrug() {
		return drug;
	}
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	public Farmacy getFarmacy() {
		return farmacy;
	}
	public void setFarmacy(Farmacy farmacy) {
		this.farmacy = farmacy;
	}
	public LocalDate getPickUp() {
		return pickUp;
	}
	public void setPickUp(LocalDate pickUp) {
		this.pickUp = pickUp;
	}
	public boolean isShowUp() {
		return showUp;
	}
	public void setShowUp(boolean showUp) {
		this.showUp = showUp;
	}

    
    
}
