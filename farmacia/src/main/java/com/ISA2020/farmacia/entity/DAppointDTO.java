package com.ISA2020.farmacia.entity;

import java.time.LocalDateTime;

public class DAppointDTO {
 	

 	private float price;
 	private String derma;
    private LocalDateTime dateTime;
    private LocalDateTime endTime;
    
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
