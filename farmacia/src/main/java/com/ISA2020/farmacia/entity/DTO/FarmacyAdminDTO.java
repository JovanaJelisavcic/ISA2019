package com.ISA2020.farmacia.entity.DTO;

import com.ISA2020.farmacia.entity.users.UserInfo;

public class FarmacyAdminDTO extends UserInfo{

	private String farmacyId;
	

	public FarmacyAdminDTO() {}
	
	public FarmacyAdminDTO(String farmacy) {
		super();
		this.farmacyId = farmacy;
		
	}

	public String getFarmacyId() {
		return farmacyId;
	}

	public void setFarmacyId(String farmacyId) {
		this.farmacyId = farmacyId;
	}
	
}
