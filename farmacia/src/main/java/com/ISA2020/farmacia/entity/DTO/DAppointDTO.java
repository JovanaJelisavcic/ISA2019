package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class DAppointDTO {
 	
	@NotNull(message="Price is mandatory")
 	private float price;
 	private String derma;
 	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
    private LocalDateTime dateTime;
 	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
    private LocalDateTime endTime;
 	
 	  @AssertTrue public boolean isValidRange() {
 		    return dateTime.isBefore(endTime);
 		  }
    
    public DAppointDTO() {}

	public DAppointDTO(float price, String derma, LocalDateTime dateTime, LocalDateTime endTime) {
		super();
		this.price = price;
		this.derma = derma;
		this.dateTime = dateTime;
		this.endTime = endTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDerma() {
		return derma;
	}

	public void setDerma(String derma) {
		this.derma = derma;
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
