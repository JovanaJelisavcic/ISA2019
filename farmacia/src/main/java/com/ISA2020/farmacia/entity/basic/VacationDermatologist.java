package com.ISA2020.farmacia.entity.basic;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class VacationDermatologist {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(Views.VacationRequestsList.class)
	private Long vacationId;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
	@JsonView(Views.VacationRequestsList.class)
	private Dermatologist dermatologist;
	@JsonView(Views.VacationRequestsList.class)
	private LocalDate beginning;
	@JsonView(Views.VacationRequestsList.class)
	private LocalDate ending;
	@Enumerated(EnumType.STRING)
	@JsonView(Views.VacationRequestsList.class)
	private VacationStatus status;
	@JsonView(Views.VacationRequestsList.class)
	private String explanation;
	
	public VacationDermatologist() {}
	
	public VacationDermatologist(Dermatologist dermatologist, LocalDate beginning, LocalDate ending,
			VacationStatus status, String explanation) {
		super();
		this.dermatologist = dermatologist;
		this.beginning = beginning;
		this.ending = ending;
		this.status = status;
		this.explanation = explanation;
	}
	public Long getVacationId() {
		return vacationId;
	}
	public void setVacationId(Long vacationId) {
		this.vacationId = vacationId;
	}
	public Dermatologist getDermatologist() {
		return dermatologist;
	}
	public void setDermatologist(Dermatologist dermatologist) {
		this.dermatologist = dermatologist;
	}
	public LocalDate getBeginning() {
		return beginning;
	}
	public void setBeginning(LocalDate beginning) {
		this.beginning = beginning;
	}
	public LocalDate getEnding() {
		return ending;
	}
	public void setEnding(LocalDate ending) {
		this.ending = ending;
	}
	public VacationStatus getStatus() {
		return status;
	}
	public void setStatus(VacationStatus status) {
		this.status = status;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	
	

}
