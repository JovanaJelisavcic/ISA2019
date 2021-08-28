package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class PurchaseOrderDTO {
	
	private Long id;
	@NotNull(message="At least one drug has to be added to Order")
	private  Map<String, Integer> drugsToPurchase; // lekovi plus kolicina
	@NotNull(message="Expiration date is mandatory")
 	@Future(message="Expiration date has to be in the future")
	 private LocalDateTime expiration;
	 
	 public PurchaseOrderDTO() {}
	public PurchaseOrderDTO(Map<String, Integer> drugsToPurchase, LocalDateTime expiration) {
		super();
		this.drugsToPurchase = drugsToPurchase;
		this.expiration = expiration;
	}
	public Map<String, Integer> getDrugsToPurchase() {
		return drugsToPurchase;
	}
	public void setDrugsToPurchase(Map<String, Integer> drugsToPurchase) {
		this.drugsToPurchase = drugsToPurchase;
	}
	public LocalDateTime getExpiration() {
		return expiration;
	}
	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	 

}
