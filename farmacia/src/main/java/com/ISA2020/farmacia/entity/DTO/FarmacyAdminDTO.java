package com.ISA2020.farmacia.entity.DTO;

import javax.validation.constraints.NotBlank;

import com.ISA2020.farmacia.entity.users.UserInfo;

public class FarmacyAdminDTO extends UserInfo{

	@NotBlank(message= "Must has asigned farmacy")
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
