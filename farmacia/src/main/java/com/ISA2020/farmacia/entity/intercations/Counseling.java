package com.ISA2020.farmacia.entity.intercations;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Counseling {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.CounselingList.class)	
    private Long counselingId;
	@JsonView(Views.CounselingList.class)	
	@Column(nullable=false)
 	private float price;
 	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
 	@JsonView(Views.CounselingList.class)
 	private Pharmacist pharma;
 	@JsonView(Views.CounselingList.class)
	@Column(nullable=false)
    private LocalDateTime dateTime;
 	@JsonView(Views.CounselingList.class)
	@Column(nullable=false)
    private LocalDateTime endTime;	
 	private boolean canceled;
 	private boolean showUp;
 	public Counseling() {}
	public Counseling(float price, Pharmacist pharma, LocalDateTime dateTime, LocalDateTime endTime, boolean canceled) {
		super();
		this.price = price;
		this.pharma = pharma;
		this.dateTime = dateTime;
		this.endTime = endTime;
		this.canceled = canceled;
	}
	public Long getCounselingId() {
		return counselingId;
	}
	public void setCounselingId(Long counselingId) {
		this.counselingId = counselingId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Pharmacist getPharma() {
		return pharma;
	}
	public void setPharma(Pharmacist pharma) {
		this.pharma = pharma;
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
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	public boolean isShowUp() {
		return showUp;
	}
	public void setShowUp(boolean showUp) {
		this.showUp = showUp;
	} 	
	


}
