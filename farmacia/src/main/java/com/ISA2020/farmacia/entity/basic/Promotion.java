package com.ISA2020.farmacia.entity.basic;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ISA2020.farmacia.entity.basic.Views.PromotionList;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Promotion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(PromotionList.class)	
	private Long promoid;
	@JsonView(PromotionList.class)	
	@NotBlank(message="Promotion Text is mandatory")
	private String promotionText;
	@JsonView(PromotionList.class)	
	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
	private LocalDateTime startsFrom;
	@JsonView(PromotionList.class)	
	@NotNull(message="Date is mandatory")
 	@Future(message="Date has to be in the future")
	private LocalDateTime expires;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "farmacy_id", nullable=false)
	@JsonView(PromotionList.class)	
	private Farmacy farmacyId;
	
	public Promotion() {}
	public Promotion(String promotionText, LocalDateTime startsFrom, LocalDateTime expires, Farmacy farmacyId) {
		super();
		this.promotionText = promotionText;
		this.startsFrom = startsFrom;
		this.expires = expires;
		this.farmacyId = farmacyId;
	}
	 @AssertTrue public boolean isValidRange() {
		    return startsFrom.isBefore(expires);
		  }
	public String getPromotionText() {
		return promotionText;
	}
	public void setPromotionText(String promotionText) {
		this.promotionText = promotionText;
	}
	public LocalDateTime getStartsFrom() {
		return startsFrom;
	}
	public void setStartsFrom(LocalDateTime startsFrom) {
		this.startsFrom = startsFrom;
	}
	public LocalDateTime getExpires() {
		return expires;
	}
	public void setExpires(LocalDateTime expires) {
		this.expires = expires;
	}
	public Farmacy getFarmacyId() {
		return farmacyId;
	}
	public void setFarmacyId(Farmacy farmacyId) {
		this.farmacyId = farmacyId;
	}
	public Long getPromoid() {
		return promoid;
	}
	public void setPromoid(Long promoid) {
		this.promoid = promoid;
	} 
	
	
	
	
}
