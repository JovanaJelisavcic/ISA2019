package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class CounselingDTO {

 	private String pharmacist;
 	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
    private LocalDateTime dateTime;
	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
    private LocalDateTime endTime;
    public CounselingDTO() {}
	public CounselingDTO(String pharmacist, LocalDateTime dateTime, LocalDateTime endTime) {
		super();
		this.pharmacist = pharmacist;
		this.dateTime = dateTime;
		this.endTime = endTime;
	}
	 @AssertTrue public boolean isValidRange() {
		    return dateTime.isBefore(endTime);
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
