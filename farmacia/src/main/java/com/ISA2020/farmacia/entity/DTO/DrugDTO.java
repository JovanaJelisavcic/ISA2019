package com.ISA2020.farmacia.entity.DTO;

import java.util.List;

import com.ISA2020.farmacia.entity.basic.DrugForm;
import com.ISA2020.farmacia.entity.basic.DrugType;

public class DrugDTO {
	
	private String code;
	private String name;
	private DrugType drugType;
	private String contraindications;
	private String composition;
	private String prescriptionMetrics;
	private List<String> replacementCodes;
	private DrugForm form;
	private String manufacturer;
	private boolean receptNeeded;
	private String  notes;
	public DrugDTO() {}
	public DrugDTO(String code, String name, DrugType drugType, String contraindications, String composition,
			String prescriptionMetrics, List<String> replacementCodes, DrugForm form, String manufacturer,
			boolean receptNeeded, String notes) {
		super();
		this.code = code;
		this.name = name;
		this.drugType = drugType;
		this.contraindications = contraindications;
		this.composition = composition;
		this.prescriptionMetrics = prescriptionMetrics;
		this.replacementCodes = replacementCodes;
		this.form = form;
		this.manufacturer = manufacturer;
		this.receptNeeded = receptNeeded;
		this.notes = notes;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DrugType getDrugType() {
		return drugType;
	}
	public void setDrugType(DrugType drugType) {
		this.drugType = drugType;
	}
	public String getContraindications() {
		return contraindications;
	}
	public void setContraindications(String contraindications) {
		this.contraindications = contraindications;
	}
	public String getComposition() {
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
	}
	public String getPrescriptionMetrics() {
		return prescriptionMetrics;
	}
	public void setPrescriptionMetrics(String prescriptionMetrics) {
		this.prescriptionMetrics = prescriptionMetrics;
	}
	public List<String> getReplacementCodes() {
		return replacementCodes;
	}
	public void setReplacementCodes(List<String> replacementCodes) {
		this.replacementCodes = replacementCodes;
	}
	public DrugForm getForm() {
		return form;
	}
	public void setForm(DrugForm form) {
		this.form = form;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public boolean isReceptNeeded() {
		return receptNeeded;
	}
	public void setReceptNeeded(boolean receptNeeded) {
		this.receptNeeded = receptNeeded;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
