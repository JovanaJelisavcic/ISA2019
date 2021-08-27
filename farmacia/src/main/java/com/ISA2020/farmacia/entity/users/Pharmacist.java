package com.ISA2020.farmacia.entity.users;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.VacationPharmacist;
import com.ISA2020.farmacia.entity.basic.Views;
import com.ISA2020.farmacia.entity.intercations.Counseling;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Pharmacist extends UserInfo {

	@OneToOne
	@JoinColumn(name = "farmacyId")
	@JsonView(Views.VerySimpleFarmacy.class)
	private Farmacy farmacy;
	@JsonView(Views.VerySimpleUser.class)
	float stars;
	@Column(nullable=false)
	@JsonView(Views.SimpleUser.class)
	LocalTime worksFrom;
	@JsonView(Views.SimpleUser.class)
	@Column(nullable=false)
	LocalTime worksTo;
	@OneToMany(mappedBy = "pharma", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, targetEntity = Counseling.class)
	private List<Counseling> counselings; 
	@OneToMany(mappedBy = "pharmacist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, targetEntity = VacationPharmacist.class)
	private List<VacationPharmacist> vacations; 
	public Pharmacist() {}

	public Pharmacist(String username, String name, String surname, Farmacy farmacy, float stars, LocalTime worksFrom,
			LocalTime worksTo) {
		super();
		this.farmacy = farmacy;
		this.stars = stars;
		this.worksFrom = worksFrom;
		this.worksTo = worksTo;
	}



	public List<VacationPharmacist> getVacations() {
		return vacations;
	}

	public void setVacations(List<VacationPharmacist> vacations) {
		this.vacations = vacations;
	}

	public List<Counseling> getCounselings() {
		return counselings;
	}

	public void setCounselings(List<Counseling> counselings) {
		this.counselings = counselings;
	}

	public Farmacy getFarmacy() {
		return farmacy;
	}

	public void setFarmacy(Farmacy farmacy) {
		this.farmacy = farmacy;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
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

	public boolean isPharmacistAvailableThen(LocalDateTime from, LocalDateTime to) {
		if(counselings.isEmpty()) return true;
		for(Counseling c : counselings) {
			if(!c.isCanceled())
			if(!(      (from.isBefore(c.getDateTime()) && to.isBefore(c.getDateTime()))    ||     (from.isAfter(c.getEndTime()) && to.isAfter(c.getEndTime()))  )  )
			return false;
		}
		return true;
	}

	public boolean isNotOnVacation(LocalDateTime from, LocalDateTime to) {
		if(vacations.isEmpty()) return true;
		for(VacationPharmacist v: vacations) {
			if(from.toLocalDate().isAfter(v.getBeginning()) && to.toLocalDate().isBefore(v.getEnding()))
				return false;
		}
		return true;
	}

	
}
