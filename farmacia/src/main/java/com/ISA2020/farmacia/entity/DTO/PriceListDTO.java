package com.ISA2020.farmacia.entity.DTO;

import java.util.List;

import com.ISA2020.farmacia.entity.DermAppointment;
import com.ISA2020.farmacia.entity.Price;
import com.ISA2020.farmacia.entity.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class PriceListDTO {
	
	@JsonView(Views.PricesList.class)
	List<Price> drugPrices;
	@JsonView(Views.PricesList.class)
	List<DermAppointment> dermappointPrices;
	
	public PriceListDTO() {}
	public PriceListDTO(List<Price> drugPrices, List<DermAppointment> dermappointPrices) {
		super();
		this.drugPrices = drugPrices;
		this.dermappointPrices = dermappointPrices;
	}
	public List<Price> getDrugPrices() {
		return drugPrices;
	}
	public void setDrugPrices(List<Price> drugPrices) {
		this.drugPrices = drugPrices;
	}
	public List<DermAppointment> getDermappointPrices() {
		return dermappointPrices;
	}
	public void setDermappointPrices(List<DermAppointment> dermappointPrices) {
		this.dermappointPrices = dermappointPrices;
	}
	
	
}
