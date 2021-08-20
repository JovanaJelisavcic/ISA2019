package com.ISA2020.farmacia.entity.users;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.WorkingHours;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Dermatologist extends UserInfo {

	
	@OneToMany(
	        mappedBy = "dermatologist",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	@JsonView(Views.VerySimple.class)
	private List<WorkingHours> workingHours;

	
	public Dermatologist() {}
	
	public Dermatologist(List<WorkingHours> workingHours) {
		super();
		this.workingHours = workingHours;
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
		workingHours.removeIf(p -> p.getFarmacy().getId().equals(id));
		return true;		
	}


	
}
