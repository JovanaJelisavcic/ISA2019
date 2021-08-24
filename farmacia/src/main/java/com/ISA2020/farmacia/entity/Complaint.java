package com.ISA2020.farmacia.entity;

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
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Complaint {
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@JsonView(Views.ComplaintsList.class)
		private Long complaintId;
		@OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "email", referencedColumnName = "email")
		@JsonView(Views.SimpleUser.class)
		Patient patient;
		@JsonView(Views.ComplaintsList.class)
		String complaintText;
		@JsonView(Views.ComplaintsList.class)
		String responseText;
		@JsonView(Views.ComplaintsList.class)
		boolean responded;
		@Enumerated(EnumType.STRING)
		@JsonView(Views.ComplaintsList.class)
		ComplaintSubjectType subject;
		@JsonView(Views.ComplaintsList.class)
		String subjectId;
		
		public Complaint() {}
		public Complaint(Patient patient, String complaintText, String responseText, boolean responded,
				ComplaintSubjectType subject, String subjectId) {
			super();
			this.patient = patient;
			this.complaintText = complaintText;
			this.responseText = responseText;
			this.responded = responded;
			this.subject = subject;
			this.subjectId = subjectId;
		}
		public Long getComplaintId() {
			return complaintId;
		}
		public void setComplaintId(Long complaintId) {
			this.complaintId = complaintId;
		}
		public Patient getPatient() {
			return patient;
		}
		public void setPatient(Patient patient) {
			this.patient = patient;
		}
		public String getComplaintText() {
			return complaintText;
		}
		public void setComplaintText(String complaintText) {
			this.complaintText = complaintText;
		}
		public String getResponseText() {
			return responseText;
		}
		public void setResponseText(String responseText) {
			this.responseText = responseText;
		}
		public boolean isResponded() {
			return responded;
		}
		public void setResponded(boolean responded) {
			this.responded = responded;
		}
		public ComplaintSubjectType getSubject() {
			return subject;
		}
		public void setSubject(ComplaintSubjectType subject) {
			this.subject = subject;
		}
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		
		
		
		
		
}
