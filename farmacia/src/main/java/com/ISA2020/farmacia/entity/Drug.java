package com.ISA2020.farmacia.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
public class Drug {

	@Id
	@JsonView(Views.Simple.class)
	private String code;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String name;
	@Enumerated(EnumType.STRING)
	@JsonView(Views.Simple.class)
	private DrugType type;
	@Column(nullable=false, length = 2000)
	@JsonView(Views.Simple.class)
	private String contraindications;
	@Column(nullable=false, length = 2000)
	@JsonView(Views.Simple.class)
	private String composition;
	@Column(nullable=false)
	@JsonView(Views.Simple.class)
	private String prescriptionMetrics;
	@ElementCollection(targetClass=Drug.class)
	@CollectionTable(joinColumns = @JoinColumn(name = "code"))
	@JsonView(Views.VeryDetailed.class)
	private List<Drug> replacement;
	
	@ManyToMany
	@JoinTable(
			  name = "drug_farmacies", 
			  joinColumns = @JoinColumn(name = "code"), 
			  inverseJoinColumns = @JoinColumn(name = "farmacy_id"))
	@JsonView(Views.Detailed.class)
	private List<Farmacy> farmacies;
	
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

	public List<Drug> getReplacement() {
		return replacement;
	}

	public void setReplacement(List<Drug> replacement) {
		this.replacement = replacement;
	}

	public List<Farmacy> getFarmacies() {
		return farmacies;
	}

	public void setFarmacies(List<Farmacy> farmacies) {
		this.farmacies = farmacies;
	}

	public boolean addFarmacy(Farmacy farmacy) {
		if(farmacies.contains(farmacy)) return false;
		else {
			farmacies.add(farmacy);
			return true;
		}
	}

	public boolean deleteFarmacy(Farmacy farmacy) {
		if(!farmacies.contains(farmacy)) return false;
		else {
			farmacies.remove(farmacy);
			return true;
		}
	}

	
	
}
