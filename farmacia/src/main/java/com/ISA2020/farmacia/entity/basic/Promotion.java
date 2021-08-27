package com.ISA2020.farmacia.entity.basic;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ISA2020.farmacia.entity.basic.Views.PromotionList;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Promotion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(PromotionList.class)	
	private Long promoid;
	@JsonView(PromotionList.class)	
	private String promotionText;
	@JsonView(PromotionList.class)	
	private LocalDateTime startsFrom;
	@JsonView(PromotionList.class)	
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
