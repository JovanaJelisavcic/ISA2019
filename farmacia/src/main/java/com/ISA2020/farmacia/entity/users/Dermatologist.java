package com.ISA2020.farmacia.entity.users;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ISA2020.farmacia.entity.basic.VacationDermatologist;
import com.ISA2020.farmacia.entity.basic.VacationStatus;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.basic.WorkingHours;
import com.ISA2020.farmacia.entity.intercations.DermAppointment;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Dermatologist extends UserInfo {

	
	@OneToMany(
	        mappedBy = "dermatologist",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	@JsonView(Views.VerySimpleFarmacy.class)
	private List<WorkingHours> workingHours;
	
	@OneToMany(
	        mappedBy = "derma",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	@JsonView(Views.SemiDetailedUser.class)
	private List<DermAppointment> appointments;
	@JsonView(Views.VerySimpleUser.class)
	private float stars;
	@OneToMany(mappedBy="dermatologist")
	private List<VacationDermatologist> vacations;

	
	public Dermatologist() {}
	
	public Dermatologist(List<WorkingHours> workingHours, float stars) {
		super();
		this.workingHours = workingHours;
		this.stars=stars;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public List<WorkingHours> getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(List<WorkingHours> workingHours) {
		this.workingHours = workingHours;
	}
	public void addWorkingHours(WorkingHours workingHours) {
		this.workingHours.add(workingHours);
	}

	public boolean checkIfAvailable(LocalTime from, LocalTime to) {
		if(workingHours.isEmpty()) return true;
		for(WorkingHours wa : workingHours) {
			if(!((from.isBefore(wa.getWorksFrom()) && to.isBefore(wa.getWorksFrom())) || (from.isAfter(wa.getWorksTo()) && to.isAfter(wa.getWorksTo()))))
				return false;	
		}
		return true;
	}

	public boolean removeWorkingHours(String id) {
		if(workingHours.isEmpty()) return false;
		boolean check =workingHours.removeIf(p -> p.getFarmacy().getId().equals(id) && !hasAppointmentsInFarmacy(id));
		if(check) return true;
		else return false;
	}

	

	private boolean hasAppointmentsInFarmacy(String id) {
		if(appointments.isEmpty()) return false;
		if(appointments.stream().anyMatch(p->p.getFarmacy().getId().equals(id) && p.isReserved() && p.getDateTime().isAfter(LocalDateTime.now()) )) return true;
		return false;
	}

	public boolean checkIfInAndFree(LocalDateTime dateTime, LocalDateTime endTime, String farmacyId) {
		if(workingHours.isEmpty()) return false;
		for(WorkingHours wh : workingHours) {
			if(wh.getFarmacy().getId().equals(farmacyId)) {
				if(dateTime.toLocalTime().isAfter(wh.getWorksFrom()) && endTime.toLocalTime().isBefore(wh.getWorksTo())) 
					if(checkIfFree(farmacyId, dateTime, endTime) && !checkIfOnVacation(dateTime, endTime)) return true;
					else return false;	
			}
		}
		return false;
	}

	private boolean checkIfOnVacation(LocalDateTime dateTime, LocalDateTime endTime) {
		if(vacations.isEmpty()) return false;
		if(vacations.stream().anyMatch(p->dateTime.toLocalDate().isAfter(p.getBeginning()) && endTime.toLocalDate().isBefore(p.getEnding()) && p.getStatus().equals(VacationStatus.ACCEPTED))) return true;
		return false;
	}

	private boolean checkIfFree(String farmacyId,LocalDateTime dateTime, LocalDateTime endTime) {
		if(appointments.isEmpty()) return true;
		for(DermAppointment dapp : appointments) {
			if(dapp.getFarmacy().getId().equals(farmacyId))
				if(! (          (dateTime.isBefore(dapp.getDateTime()) && endTime.isBefore(dapp.getDateTime()))   ||   (      dateTime.isAfter(dapp.getEndTime()) && endTime.isAfter(dapp.getEndTime())     )   )   )
					return false;
		}
		return true;
	}


	public boolean checkIfworksIn(String id) {
		for(WorkingHours wh : workingHours) {
			if(wh.getFarmacy().getId().equals(id))
				return true;
		}
		return false;
	}

	public List<DermAppointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<DermAppointment> appointments) {
		this.appointments = appointments;
	}

	public List<VacationDermatologist> getVacations() {
		return vacations;
	}

	public void setVacations(List<VacationDermatologist> vacations) {
		this.vacations = vacations;
	}
	
}
