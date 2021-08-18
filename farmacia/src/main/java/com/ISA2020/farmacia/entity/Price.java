package com.ISA2020.farmacia.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Price {
	
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	 @JsonView(Views.Simple.class)
	private float price;
	 @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity= Drug.class)
	  @JoinColumn(name = "code", nullable = false)
	private Drug drug;
	 @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity= Farmacy.class)
	  @JoinColumn(name = "farmacy_id", nullable = false)
	    private Farmacy farmacy;
	
	public Price() {}
	
	
	public Price(float price, Drug drug) {
		super();
		this.price = price;
		this.drug = drug;
	}


	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Drug getDrug() {
		return drug;
	}
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	
	
}
