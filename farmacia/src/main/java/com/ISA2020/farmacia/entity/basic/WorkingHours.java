package com.ISA2020.farmacia.entity.basic;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkingHours {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long wAid;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "farmacy_id", nullable=false)
	@JsonView(Views.VerySimpleFarmacy.class)
	private Farmacy farmacyId;
	@Column(nullable=false)
	@DateTimeFormat(style = "HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	LocalTime worksFrom;
	@Column(nullable=false)
	@DateTimeFormat(style = "HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	LocalTime worksTo;
	@ManyToOne(fetch = FetchType.LAZY)
	private Dermatologist dermatologist;
	
	public WorkingHours() {}
	
	public WorkingHours(Farmacy farmacy, LocalTime worksFrom, LocalTime worksTo, Dermatologist dermat) {
		super();
		this.farmacyId = farmacy;
		this.worksFrom = worksFrom;
		this.worksTo = worksTo;
		this.dermatologist=dermat;
	}
	
	public Long getwAid() {
		return wAid;
	}

	public void setwAid(Long wAid) {
		this.wAid = wAid;
	}

	public Farmacy getFarmacy() {
		return farmacyId;
	}
	public void setFarmacy(Farmacy farmacy) {
		this.farmacyId = farmacy;
	}
	public LocalTime getWorksFrom() {
		return worksFrom;
	}
	public void setWorksFrom(LocalTime worksFrom) {
		this.worksFrom = worksFrom;
	}
	public LocalTime getWorksTo() {
		return worksTo;
	}
	public void setWorksTo(LocalTime worksTo) {
		this.worksTo = worksTo;
	}

	public Dermatologist getDermatologist() {
		return dermatologist;
	}

	public void setDermatologist(Dermatologist dermatologist) {
		this.dermatologist = dermatologist;
	}
	
	
}
