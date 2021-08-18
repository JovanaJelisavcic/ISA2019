package com.ISA2020.farmacia.entity;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;


@Entity
public class Drug {

	@Id
	private String code;
	@Column(nullable=false)
	private String name;
	@Enumerated(EnumType.STRING)
	private DrugType type;
	@Column(nullable=false, length = 2000)
	private String contraindications;
	@Column(nullable=false, length = 2000)
	private String composition;
	@Column(nullable=false)
	private String prescriptionMetrics;
	@ElementCollection(targetClass=Drug.class)
	@CollectionTable(joinColumns = @JoinColumn(name = "code"))
	private Set<Drug> replacement;
	
	public Drug() {}

	public Drug(String code, String name, DrugType type, String contraindications, String composition,
			String prescriptionMetrics) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.contraindications = contraindications;
		this.composition = composition;
		this.prescriptionMetrics = prescriptionMetrics;
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

	public Set<Drug> getReplacement() {
		return replacement;
	}

	public void setReplacement(Set<Drug> replacement) {
		this.replacement = replacement;
	}

	
	
}
