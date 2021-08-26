package com.ISA2020.farmacia.entity.users;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.DTO.FarmacyAdminDTO;
import com.ISA2020.farmacia.entity.basic.Farmacy;

@Entity
public class FarmacyAdmin extends UserInfo {

	@ManyToOne
	@JoinColumn(name = "farmacyId")
	private Farmacy farmacy;
	
	
	public FarmacyAdmin() {}
	
	public FarmacyAdmin(Farmacy farmacy) {
		super();
		this.farmacy = farmacy;
		
	}

	public Farmacy getFarmacy() {
		return farmacy;
	}
	public void setFarmacy(Farmacy farmacy) {
		this.farmacy = farmacy;
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
	
	public void setByUserInfo(FarmacyAdminDTO farmacyAdmin) {
		this.setEmail(farmacyAdmin.getEmail());
		this.setAdress(farmacyAdmin.getAdress());
		this.setCity(farmacyAdmin.getCity());
		this.setName(farmacyAdmin.getName());
		this.setPhoneNum(farmacyAdmin.getPhoneNum());
		this.setState(farmacyAdmin.getState());
		this.setSurname(farmacyAdmin.getSurname());
	}

	
	
}
