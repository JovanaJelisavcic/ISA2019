package com.ISA2020.farmacia.entity.intercations;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.users.Supplier;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Offer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(Views.MyFarmacyOffersList.class)
	private Long offerid;
	@ManyToOne
	@JoinColumn(name="orderid", nullable=false)
	@JsonView(Views.MyFarmacyOffersList.class)
	private PurchaseOrder order;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email", referencedColumnName = "email")
	@JsonView(Views.MyFarmacyOffersList.class)
	private Supplier supplier;
	@Enumerated(EnumType.STRING)
	@JsonView(Views.MyFarmacyOffersList.class)
	private OfferStatus status;
	@JsonView(Views.MyFarmacyOffersList.class)
	private float price;
	@JsonView(Views.MyFarmacyOffersList.class)
	private LocalDateTime deadline;
	  
	  
	public Offer() {}


	public Offer( PurchaseOrder order, Supplier supplier, OfferStatus status, float price,
			LocalDateTime deadline) {
		super();
		this.order = order;
		this.supplier = supplier;
		this.status = status;
		this.price = price;
		this.deadline = deadline;
	}


	public Long getOfferid() {
		return offerid;
	}


	public void setOfferid(Long offerid) {
		this.offerid = offerid;
	}


	public PurchaseOrder getOrder() {
		return order;
	}


	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public OfferStatus getStatus() {
		return status;
	}


	public void setStatus(OfferStatus status) {
		this.status = status;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public LocalDateTime getDeadline() {
		return deadline;
	}


	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}
	
	
	  

}
