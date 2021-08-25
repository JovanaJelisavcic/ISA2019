package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;

public class CounselingDTO {

 	
 	private String pharmacist;
    private LocalDateTime dateTime;
    private LocalDateTime endTime;
    public CounselingDTO() {}
	public CounselingDTO(String pharmacist, LocalDateTime dateTime, LocalDateTime endTime) {
		super();
		this.pharmacist = pharmacist;
		this.dateTime = dateTime;
		this.endTime = endTime;
	}
	public String getPharmacist() {
		return pharmacist;
	}
	public void setPharmacist(String pharmacist) {
		this.pharmacist = pharmacist;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}	

    
}
