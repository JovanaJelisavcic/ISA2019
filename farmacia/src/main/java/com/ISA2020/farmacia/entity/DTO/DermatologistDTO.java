package com.ISA2020.farmacia.entity.DTO;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DermatologistDTO {

	private String email;
	@DateTimeFormat(style = "HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	private LocalTime worksFrom;
	@DateTimeFormat(style = "HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	private LocalTime worksTo;
	
	public DermatologistDTO() {}
	

	public DermatologistDTO(String email, LocalTime worksFrom, LocalTime worksTo) {
		super();
		this.email = email;
		this.worksFrom = worksFrom;
		this.worksTo = worksTo;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
