package com.ISA2020.farmacia.entity.DTO;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ISA2020.farmacia.entity.basic.DrugForm;
import com.ISA2020.farmacia.entity.basic.DrugType;

public class DrugDTO {
	
	@NotBlank(message="Code is mandatory")
	private String code;
	@NotBlank(message="Name is mandatory")
	private String name;
	@NotNull(message="Type is mandatory")
	private DrugType drugType;
	@NotBlank(message="Contradictions are mandatory")
	private String contraindications;
	@NotBlank(message="Composition is mandatory")
	private String composition;
	@NotBlank(message="Prescription metrics are mandatory")
	private String prescriptionMetrics;
	private List<String> replacementCodes;
	@NotNull(message="Form is mandatory")
	private DrugForm form;
	@NotBlank(message="Manufacturer is mandatory")
	private String manufacturer;
	private boolean receptNeeded;
	private String  notes;
	@NotNull(message="Points are mandatory")
	private int points;
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
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
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
