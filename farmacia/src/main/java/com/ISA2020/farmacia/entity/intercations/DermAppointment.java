package com.ISA2020.farmacia.entity.intercations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
public class DermAppointment {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 	@JsonView(Views.DermappointList.class)	
	    private Long id;
	 	@JsonView(Views.VerySimplePrice.class)
		@Column(nullable=false)
	 	private float price;
	 	@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "email")
	 	@JsonView(Views.VerySimpleUser.class)
	 	private Dermatologist derma;
	 	@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "farmacy_id")
	 	@JsonView(Views.VerySimpleFarmacy.class)
	    private Farmacy farmacy;
	 	@JsonView(Views.VerySimplePrice.class)
		@Column(nullable=false)
	    private LocalDateTime dateTime;
	 	@JsonView(Views.VerySimplePrice.class)
		@Column(nullable=false)
	    private LocalDateTime endTime;
	 	@JsonView(Views.DermappointList.class)	 	
	 	private boolean reserved;
	 	@JsonView(Views.DermappointDetailedList.class)	 	
	 	private boolean done;

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
		
		public boolean isDone() {
			return done;
		}


		public void setDone(boolean done) {
			this.done = done;
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


		public boolean isReserved() {
			return reserved;
		}


		public void setReserved(boolean reserved) {
			this.reserved = reserved;
		}
	
	    
}
