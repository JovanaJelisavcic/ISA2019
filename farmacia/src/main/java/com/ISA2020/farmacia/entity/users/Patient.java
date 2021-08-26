package com.ISA2020.farmacia.entity.users;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.intercations.Counseling;
import com.ISA2020.farmacia.entity.intercations.DermAppointment;
import com.ISA2020.farmacia.entity.intercations.DrugReservation;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Patient extends UserInfo {

	@ElementCollection(targetClass=Drug.class)
	@CollectionTable(joinColumns = @JoinColumn(name = "email"))
	@JsonView(Views.SimpleDrug.class)
    private List<Drug> allergies;
	@OneToMany
	@JoinTable(
			  name = "farmacies_subs", 
			  joinColumns = @JoinColumn(name = "email"), 
			  inverseJoinColumns = @JoinColumn(name = "farmacy_id"))
	private List<Farmacy> farmaciesSubs; 
	@ManyToMany
	@JoinTable(
			  name = "dermapoint_reserved", 
			  joinColumns = @JoinColumn(name = "email"), 
			  inverseJoinColumns = @JoinColumn(name = "id"))
	private List<DermAppointment> dermappoints; 
	
	@ManyToMany
	@JoinTable(
			  name = "drugs_reserved", 
			  joinColumns = @JoinColumn(name = "email"), 
			  inverseJoinColumns = @JoinColumn(name = "reservation_id"))
	private List<DrugReservation> drugsReserved; 
	@ManyToMany
	@JoinTable(
			  name = "patient_counseling", 
			  joinColumns = @JoinColumn(name = "email"), 
			  inverseJoinColumns = @JoinColumn(name = "counseling_id"))
	private List<Counseling> counselings; 
	
	private int penalties;
	
	public List<Counseling> getCounselings() {
		return counselings;
	}

	public void setCounselings(List<Counseling> counselings) {
		this.counselings = counselings;
	}

	public List<Farmacy> getFarmaciesSubs() {
		return farmaciesSubs;
	}

	public void setFarmaciesSubs(List<Farmacy> farmaciesSubs) {
		this.farmaciesSubs = farmaciesSubs;
	}

	public Patient() {}
	
	public Patient(UserInfo user) {
		super(user);
	}

	public Patient(List<Drug> allergies) {
		super();
		this.allergies = allergies;
	}

	public List<DrugReservation> getDrugsReserved() {
		return drugsReserved;
	}

	public void setDrugsReserved(List<DrugReservation> drugsReserved) {
		this.drugsReserved = drugsReserved;
	}

	public List<DermAppointment> getDermappoints() {
		return dermappoints;
	}

	public void setDermappoints(List<DermAppointment> dermappoints) {
		this.dermappoints = dermappoints;
	}

	public List<Drug> getAllergies() {
		return allergies;
	}
	public void setAllergies(List<Drug> allergies) {
		this.allergies = allergies;
	}
	public void addAllergies(List<Drug> drugs) {
		this.allergies.addAll(drugs);
	}

	public void changeUserInfo(UserInfo farmacyadmin) {
		if(!farmacyadmin.getAdress().equals(super.getAdress()))
			super.setAdress(farmacyadmin.getAdress());
		if(!farmacyadmin.getCity().equals(super.getCity()))
			super.setCity(farmacyadmin.getCity());
		if(!farmacyadmin.getName().equals(super.getName()))
			super.setName(farmacyadmin.getName());
		if(!farmacyadmin.getPhoneNum().equals(super.getPhoneNum()))
			super.setPhoneNum(farmacyadmin.getPhoneNum());
		if(!farmacyadmin.getState().equals(super.getState()))
			super.setState(farmacyadmin.getState());
		if(!farmacyadmin.getSurname().equals(super.getSurname()))
			super.setSurname(farmacyadmin.getSurname());
		
	}

	public void addDermapointReservation(DermAppointment dermAppointment) {
		dermappoints.add(dermAppointment);
	}

	public void removeDermapointReservation(DermAppointment dermAppointment) {
		dermappoints.remove(dermAppointment);
		
	}

	public void addDrugReservation(DrugReservation drugReserve) {
	drugsReserved.add(drugReserve);
		
	}

	public void removeDrugReservation(DrugReservation drugReservation) {
		drugsReserved.remove(drugReservation);
		
	}

	public boolean addCounseling(Counseling counseling) {

		if(counselings.stream().anyMatch(o -> o.isCanceled() && o.getDateTime().equals(counseling.getDateTime()) &&o.getEndTime().equals(counseling.getEndTime()) &&
				o.getPharma().equals(counseling.getPharma())	
				)) {
			return false;
		}else {			counseling.setCanceled(false);
		counselings.add(counseling);
		return true;}
		
	}

	public int getPenalties() {
		return penalties;
	}

	public void setPenalties(int penalties) {
		this.penalties = penalties;
	}


	

}
