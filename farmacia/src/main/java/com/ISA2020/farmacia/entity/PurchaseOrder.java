package com.ISA2020.farmacia.entity;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;

import com.ISA2020.farmacia.entity.users.FarmacyAdmin;

@Entity
public class PurchaseOrder {
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long orderId;
		@ElementCollection
		@CollectionTable(name="item_qty",
		    joinColumns=@JoinColumn(name="order_id"))
		@MapKeyJoinColumn(name="code")
		@Column(name="qty")
		private  Map<Drug, Integer> drugsToPurchase; // lekovi plus kolicina
		 private LocalDateTime expiration;
		 @Enumerated(EnumType.STRING)
		 private OrderStatus status;
		 @OneToOne(cascade = CascadeType.ALL)
		    @JoinColumn(name = "email", referencedColumnName = "email")
		 private FarmacyAdmin maker;
		 
		 public PurchaseOrder() {}
		public PurchaseOrder(Long orderId, Map<Drug, Integer> drugsToPurchase, LocalDateTime expiration,
				OrderStatus status) {
			super();
			this.orderId = orderId;
			this.drugsToPurchase = drugsToPurchase;
			this.expiration = expiration;
			this.status = status;
		}
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public Map<Drug, Integer> getDrugsToPurchase() {
			return drugsToPurchase;
		}
		public void setDrugsToPurchase(Map<Drug, Integer> drugsToPurchase) {
			this.drugsToPurchase = drugsToPurchase;
		}
		public LocalDateTime getExpiration() {
			return expiration;
		}
		public void setExpiration(LocalDateTime expiration) {
			this.expiration = expiration;
		}
		public OrderStatus getStatus() {
			return status;
		}
		public void setStatus(OrderStatus status) {
			this.status = status;
		}
		public FarmacyAdmin getMaker() {
			return maker;
		}
		public void setMaker(FarmacyAdmin maker) {
			this.maker = maker;
		}
		
		
		 
		 
		 
		
}
