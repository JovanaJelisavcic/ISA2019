package com.ISA2020.farmacia.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.users.Dermatologist;


@Entity
public class DermAppointment {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 	private float price;
	 	@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "email")
	 	private Dermatologist derma;
	 	@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "farmacy_id")
	    private Farmacy farmacy;
	    private LocalDateTime dateTime;
	    private LocalDateTime endTime;

	    public DermAppointment() {}
	    

		public DermAppointment(float price, Dermatologist derma, Farmacy farmacy, LocalDateTime dateTime,
				LocalDateTime endTime) {
			super();
			this.price = price;
			this.derma = derma;
			this.farmacy = farmacy;
			this.dateTime = dateTime;
			this.endTime = endTime;
		}
		public long getDurationInMinutes() {
			return dateTime.until( endTime, ChronoUnit.MINUTES );
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public Dermatologist getDerma() {
			return derma;
		}
		public void setDerma(Dermatologist derma) {
			this.derma = derma;
		}
		public Farmacy getFarmacy() {
			return farmacy;
		}
		public void setFarmacy(Farmacy farmacy) {
			this.farmacy = farmacy;
		}
		public LocalDateTime getDateTime() {
			return dateTime;
		}
		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}
		public LocalDateTime getEndTime() {
			return endTime;
		}
		public void setEndTime(LocalDateTime endTime) {
			this.endTime = endTime;
		}
	
	    
}
