package com.ISA2020.farmacia.entity.basic;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ResetDate {

	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 private LocalDate lastReset;
	 @Column(nullable=true)
	 private Long randomField;
	 public ResetDate() {}
	public ResetDate( LocalDate standsFrom) {
		super();
		this.lastReset = standsFrom;

	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getStandsFrom() {
		return lastReset;
	}
	public void setStandsFrom(LocalDate standsFrom) {
		this.lastReset = standsFrom;
	}
	public Long getRandomField() {
		return randomField;
	}
	public void setRandomField(Long randomField) {
		this.randomField = randomField;
	}
	
	 
	 
}
