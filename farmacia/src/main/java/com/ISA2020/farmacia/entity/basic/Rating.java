package com.ISA2020.farmacia.entity.basic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.ISA2020.farmacia.entity.users.Patient;
@Entity
public class Rating {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ratingId;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
	private Patient patient;
	@NotNull(message="Rating is mandatory")
	 @Min(value=1, message="Rating has to be between 1 and 5")
	@Max(value=5, message="Rating has to be between 1 and 5")
	private int ratingStars;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private RatingSubjectType subject;
	@Column(nullable=false)
	private String subjectId;
	public Rating() {}
	public Rating(Patient patient, int ratingStars, RatingSubjectType subject, String subjectId) {
		super();
		this.patient = patient;
		this.ratingStars = ratingStars;
		this.subject = subject;
		this.subjectId = subjectId;
	}
	public Long getRatingId() {
		return ratingId;
	}
	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public int getRatingStars() {
		return ratingStars;
	}
	public void setRatingStars(int ratingStars) {
		this.ratingStars = ratingStars;
	}
	public RatingSubjectType getSubject() {
		return subject;
	}
	public void setSubject(RatingSubjectType subject) {
		this.subject = subject;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	
}
