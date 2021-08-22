package com.ISA2020.farmacia.entity;

import java.time.LocalDateTime;
import java.util.Map;

public class PurchaseOrderDTO {
	
	private  Map<String, Integer> drugsToPurchase; // lekovi plus kolicina
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
	
	 

}
