package com.ISA2020.farmacia.entity.DTO;

public class LoyaltyScaleDTO {

	private int regular;
	private int regularDiscount;
	private int gold;
	private int goldDiscount;
	private int silver;
	private int silverDiscount;
	
	public LoyaltyScaleDTO() {}
	public LoyaltyScaleDTO(int regular, int gold, int silver) {
		super();
		this.regular = regular;
		this.gold = gold;
		this.silver = silver;
	}
	public int getRegular() {
		return regular;
	}
	public void setRegular(int regular) {
		this.regular = regular;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getSilver() {
		return silver;
	}
	public void setSilver(int silver) {
		this.silver = silver;
	}
	public int getRegularDiscount() {
		return regularDiscount;
	}
	public void setRegularDiscount(int regularDiscount) {
		this.regularDiscount = regularDiscount;
	}
	public int getGoldDiscount() {
		return goldDiscount;
	}
	public void setGoldDiscount(int goldDiscount) {
		this.goldDiscount = goldDiscount;
	}
	public int getSilverDiscount() {
		return silverDiscount;
	}
	public void setSilverDiscount(int silverDiscount) {
		this.silverDiscount = silverDiscount;
	}
	
	
}
