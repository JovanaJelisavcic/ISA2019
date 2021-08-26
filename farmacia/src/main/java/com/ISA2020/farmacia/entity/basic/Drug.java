package com.ISA2020.farmacia.entity.basic;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.ISA2020.farmacia.entity.DTO.DrugDTO;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
public class Drug {

	

	@Id
	@JsonView(Views.VerySimpleDrug.class)
	private String code;
	@Column(nullable=false)
	@JsonView(Views.VerySimpleDrug.class)
	private String name;
	@Enumerated(EnumType.STRING)
	@JsonView(Views.SimpleDrug.class)
	private DrugType drugType;
	@Column(nullable=false, length = 2000)
	@JsonView(Views.SimpleDrug.class)
	private String contraindications;
	@Column(nullable=false, length = 2000)
	@JsonView(Views.SimpleDrug.class)
	private String composition;
	@Column(nullable=false)
	@JsonView(Views.SimpleDrug.class)
	private String prescriptionMetrics;
	@ManyToMany
	@CollectionTable(joinColumns = @JoinColumn(name = "code"))
	@JsonView(Views.VeryDetailedDrug.class)
	private List<Drug> replacement;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	@JsonView(Views.SimpleDrug.class)
	private DrugForm form;
	@Column(nullable=false)
	@JsonView(Views.SimpleDrug.class)
	private String manufacturer;
	@Column(nullable=false)
	@JsonView(Views.SimpleDrug.class)
	private boolean receptNeeded;
	@JsonView(Views.SimpleDrug.class)
	private String  notes;
	@JsonView(Views.SimpleDrug.class)
	private float  stars;
	
	@ManyToMany
	@JoinTable(
			  name = "drug_farmacies", 
			  joinColumns = @JoinColumn(name = "code"), 
			  inverseJoinColumns = @JoinColumn(name = "farmacy_id"))
	@JsonView(Views.SemiDetailedFarmacy.class)
	private List<Farmacy> farmacies;
	
	public Drug() {}

	public Drug(String code, String name, DrugType type, String contraindications, String composition,
			String prescriptionMetrics) {
		super();
		this.code = code;
		this.name = name;
		this.drugType = type;
		this.contraindications = contraindications;
		this.composition = composition;
		this.prescriptionMetrics = prescriptionMetrics;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public DrugForm getForm() {
		return form;
	}

	public void setForm(DrugForm form) {
		this.form = form;
	}

	public Drug(String key) {
		   
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
		return drugType;
	}

	public void setType(DrugType type) {
		this.drugType = type;
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

//return "Drug [code=" + code + ", name=" + name + ", type=" + type + ", contraindications=" + contraindications
//	+ ", composition=" + composition + ", prescriptionMetrics=" + prescriptionMetrics + ", replacement="
//	+ replacement + ", farmacies=" + farmacies + "]";
	@Override
	public String toString() {
		return "Drug [code=" + code + ", name=" + name +"]";
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

	public void setFromDTO(DrugDTO drug) {
		code=drug.getCode();
		name=drug.getName();
		drugType=drug.getDrugType();
		contraindications=drug.getContraindications();
		composition=drug.getComposition();
		prescriptionMetrics=drug.getPrescriptionMetrics();
		form = drug.getForm();
		manufacturer= drug.getManufacturer();
		receptNeeded=drug.isReceptNeeded();
		notes=drug.getNotes();
		
	}
	
}
