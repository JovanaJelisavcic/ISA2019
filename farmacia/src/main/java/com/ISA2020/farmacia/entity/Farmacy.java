package com.ISA2020.farmacia.entity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;

import com.ISA2020.farmacia.entity.users.Patient;
import com.ISA2020.farmacia.entity.users.Pharmacist;
import com.ISA2020.farmacia.util.DrugDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Farmacy {

	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(Views.VerySimpleFarmacy.class)
	private String farmacyId;
	@Column(nullable=false)
	@JsonView(Views.VerySimpleFarmacy.class)
	private String name;
	 @Column(nullable=false)
	 @JsonView(Views.VerySimpleFarmacy.class)
	private String adress;
	 @JsonView(Views.VerySimpleFarmacy.class)
	 @Column(columnDefinition="Decimal(2,1)")
	 @JsonInclude(JsonInclude.Include.NON_NULL)
	private float stars;
	 @JsonView(Views.SimpleFarmacy.class)
	 @JsonInclude(JsonInclude.Include.NON_NULL)
	 private String description;
	 
	@OneToMany(mappedBy = "farmacy", fetch = FetchType.LAZY,
		            cascade = CascadeType.ALL, targetEntity = Price.class)
	@JsonView(Views.SemiDetailedFarmacy.class)
	 private List<Price> prices; 
	
	@OneToMany(mappedBy = "farmacy", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, targetEntity = Pharmacist.class)
	private List<Pharmacist> pharmacists; 
	 
	@ManyToMany(mappedBy = "farmacies")
	@JsonView(Views.VeryDetailedFarmacy.class)
	private List<Drug> drugs;

	@ManyToMany(mappedBy = "farmaciesSubs")
	@JsonView(Views.VeryDetailedFarmacy.class)
	private List<Patient> subscribedUsers;
	 
	@ElementCollection
	@CollectionTable(name="farmacy_drug_qty",
	    joinColumns=@JoinColumn(name="farmacy_id"))
	@MapKeyJoinColumn(name="code")
	@Column(name="qty")
	@JsonProperty("map")
	  @JsonDeserialize(keyUsing = DrugDeserializer.class)
	private  Map<Drug, Integer> drugsQuantities; 

	
	


	public Farmacy() {}
	
	public Farmacy(String name, String adress, float stars, String description) {
		super();
		this.name = name;
		this.adress = adress;
		this.stars = stars;
		this.description = description;
	}
	

	public List<Pharmacist> getPharmacists() {
		return pharmacists;
	}

	public void setPharmacists(List<Pharmacist> pharmacists) {
		this.pharmacists = pharmacists;
	}

	public Map<Drug, Integer> getDrugsQuantities() {
		return drugsQuantities;
	}

	public void setDrugsQuantities(Map<Drug, Integer> drugsQuantities) {
		this.drugsQuantities = drugsQuantities;
	}
	public String getId() {
		return farmacyId;
	}
	public void setId(String id) {
		this.farmacyId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}

	public List<Price> getPrices() {
		return prices;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<Drug> drugs) {
		this.drugs = drugs;
	}

	public boolean addPrice(Price price) {
		boolean check = true;
		for(Price p : prices) {
			if(p.getDrug().equals(price.getDrug())) {
			
			if(!((price.getStandsFrom().isBefore(p.getStandsFrom()) && price.getStandsUntill().isBefore(p.getStandsFrom()))||(price.getStandsFrom().isAfter(p.getStandsUntill())&&price.getStandsUntill().isAfter(p.getStandsUntill()))))
						{check=false;}
		
			}}
		if(check) {
		prices.add(price);
		return true;
		}
		else return false;
	}

	public List<Patient> getSubscribedUsers() {
		return subscribedUsers;
	}

	public void setSubscribedUsers(List<Patient> subscribedUsers) {
		this.subscribedUsers = subscribedUsers;
	}

	public void addQtys(Map<Drug, Integer> drugsPurchased) {
		 for (Entry<Drug, Integer> entry : drugsPurchased.entrySet()) {
			 if(drugsQuantities.keySet().contains(entry.getKey())) {
				 drugsQuantities.put(entry.getKey(), entry.getValue() + drugsQuantities.get(entry.getKey()));
				} else {
					drugsQuantities.put(entry.getKey(), entry.getValue());
				}
		    }
	}

	public void reservedDrugQty(Drug drug) {
		drugsQuantities.put(drug, drugsQuantities.get(drug) - 1);
		
	}

	public void reverseDrugQty(Drug drug) {
		drugsQuantities.put(drug, drugsQuantities.get(drug) + 1);
		
	}

	

	
	
	
	
}
