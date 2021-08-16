package com.ISA2020.farmacia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Entity
public class Drug {

	@Id
	private String code;
	@Column(nullable=false)
	private String name;
	@Enumerated(EnumType.STRING)
	private DrugType type;
	@Column(nullable=false)
	private String contraindications;
	@Column(nullable=false)
	private String composition;
	@Column(nullable=false)
	private String prescriptionMetrics;
	private String replacement;
	
	public Drug() {}

	public Drug(String code, String name, DrugType type, String contraindications, String composition,
			String prescriptionMetrics, String replacement) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.contraindications = contraindications;
		this.composition = composition;
		this.prescriptionMetrics = prescriptionMetrics;
		this.replacement = replacement;
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

	public DrugType getType() {
		return type;
	}

	public void setType(DrugType type) {
		this.type = type;
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

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	
}
