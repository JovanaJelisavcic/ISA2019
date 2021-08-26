package com.ISA2020.farmacia.entity.basic;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.users.Patient;
@Entity
public class Rating {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ratingId;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
	private Patient patient;
	private float ratingStars;
	@Enumerated(EnumType.STRING)
	private RatingSubjectType subject;
	private String subjectId;
	public Rating() {}
	public Rating(Patient patient, float ratingStars, RatingSubjectType subject, String subjectId) {
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
	public float getRatingStars() {
		return ratingStars;
	}
	public void setRatingStars(float ratingStars) {
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
