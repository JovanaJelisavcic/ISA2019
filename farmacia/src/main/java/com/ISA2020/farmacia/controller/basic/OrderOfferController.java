package com.ISA2020.farmacia.controller.basic;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Offer;
import com.ISA2020.farmacia.entity.OfferStatus;
import com.ISA2020.farmacia.entity.OrderStatus;
import com.ISA2020.farmacia.entity.PurchaseOrder;
import com.ISA2020.farmacia.entity.PurchaseOrderDTO;
import com.ISA2020.farmacia.entity.Views;
import com.ISA2020.farmacia.entity.users.FarmacyAdmin;
import com.ISA2020.farmacia.repository.DrugRepository;
import com.ISA2020.farmacia.repository.FarmacyAdminRepository;
import com.ISA2020.farmacia.repository.FarmacyRepository;
import com.ISA2020.farmacia.repository.OfferRepository;
import com.ISA2020.farmacia.repository.OrderRepository;
import com.ISA2020.farmacia.security.JwtUtils;
import com.ISA2020.farmacia.util.FilteringUtil;
import com.ISA2020.farmacia.util.MailUtil;
import com.fasterxml.jackson.annotation.JsonView;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestController
@RequestMapping("/orderOffer")
public class OrderOfferController {
	
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	FarmacyAdminRepository farmAdminRepo;
	@Autowired
	DrugRepository drugRepo;
	@Autowired
	FarmacyRepository farmacyRepo;
	@Autowired
	OrderRepository orderRepo;
	@Autowired
	OfferRepository offerRepo;
	@Autowired
	FilteringUtil filteringUtil;
	   @Autowired
	   MailUtil mailUtil;
	   
	   
	   
	   
	   
	   @PostMapping("/purchaseOrder")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> postOrder(@RequestHeader("Authorization") String token, @RequestBody PurchaseOrderDTO order) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			FarmacyAdmin farmacyAdmin =  farmAdminRepo.findById(username).get();
			Farmacy farmacy = farmacyAdmin.getFarmacy();
			PurchaseOrder finalOrder = new PurchaseOrder();
			finalOrder.setExpiration(order.getExpiration());
			finalOrder.setMaker(farmacyAdmin);
			finalOrder.setStatus(OrderStatus.WAITS_FOR_OFFER);
			Map<Drug, Integer> readyOrder = new HashMap<>();
			 for (Map.Entry<String, Integer> entry : order.getDrugsToPurchase().entrySet()) {
				 Optional<Drug> drug = drugRepo.findById(entry.getKey());
				 if(drug.isEmpty()) return  ResponseEntity.notFound().build();
			        if(!drug.get().getFarmacies().contains(farmacy)) {
			        	drug.get().addFarmacy(farmacy);
			        	drugRepo.save(drug.get());
			        }
			        readyOrder.put(drug.get(), entry.getValue());
			    }
			 finalOrder.setDrugsToPurchase(readyOrder);
			orderRepo.save(finalOrder);
			return ResponseEntity.ok().build();
			
			
		}
		@JsonView(Views.MyFarmacyOrdersList.class)
		@GetMapping("/allOrders")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String token)throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
			List<PurchaseOrder> allOrders = orderRepo.findAll();
			return new ResponseEntity<>(filteringUtil.filterByFarmacyOrder(allOrders, farmacy.getId()),HttpStatus.OK);
			
		}
		
		
		@PostMapping("/updateOrder")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> updateOrder(@RequestHeader("Authorization") String token, @RequestBody PurchaseOrderDTO order) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			FarmacyAdmin farmacyAdmin =  farmAdminRepo.findById(username).get();
			Farmacy farmacy = farmacyAdmin.getFarmacy();
			Optional<PurchaseOrder> oldOrder = orderRepo.findById(order.getId());
			if(oldOrder.isEmpty()) return ResponseEntity.notFound().build(); 
			if(!oldOrder.get().getStatus().equals(OrderStatus.WAITS_FOR_OFFER) || !oldOrder.get().getMaker().equals(farmacyAdmin)) return ResponseEntity.badRequest().build();
			oldOrder.get().setExpiration(order.getExpiration());
			Map<Drug, Integer> readyOrder = new HashMap<>();
			 for (Map.Entry<String, Integer> entry : order.getDrugsToPurchase().entrySet()) {
				 Optional<Drug> drug = drugRepo.findById(entry.getKey());
				 if(drug.isEmpty()) return  ResponseEntity.ok().build();
			        if(!drug.get().getFarmacies().contains(farmacy)) {
			        	drug.get().addFarmacy(farmacy);
			        	drugRepo.save(drug.get());
			        }
			        readyOrder.put(drug.get(), entry.getValue());
			    }
			 oldOrder.get().setDrugsToPurchase(readyOrder);
			orderRepo.save(oldOrder.get());
			return ResponseEntity.ok().build();
			
			
		}
		
		@DeleteMapping("/purchaseOrder/{id}")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> deleteOrder(@RequestHeader("Authorization") String token,@PathVariable Long id ) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {	
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			FarmacyAdmin farmacyAdmin =  farmAdminRepo.findById(username).get();
			Optional<PurchaseOrder> oldOrder = orderRepo.findById(id);
			if(oldOrder.isEmpty()) return ResponseEntity.notFound().build(); 
			if(!oldOrder.get().getStatus().equals(OrderStatus.WAITS_FOR_OFFER) || !oldOrder.get().getMaker().equals(farmacyAdmin)) return ResponseEntity.badRequest().build();
			orderRepo.delete(oldOrder.get());
			return ResponseEntity.ok().build();	
		}
		
		@JsonView(Views.MyFarmacyOffersList.class)
		@GetMapping("/allOffers")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> getAllOffers(@RequestHeader("Authorization") String token)throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			Farmacy farmacy =  farmAdminRepo.findById(username).get().getFarmacy();
			List<Offer> allOffers = offerRepo.findAll();
			return new ResponseEntity<>(filteringUtil.filterByFarmacyOffer(allOffers, farmacy.getId()),HttpStatus.OK);			
		}
		
		@PostMapping("/acceptOffer/{id}")
		@PreAuthorize("hasAuthority('FARMACY_ADMIN')")
		public ResponseEntity<?> acceptOffers(@RequestHeader("Authorization") String token, @PathVariable Long id)throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
			String username =jwtUtils.getUserNameFromJwtToken(token.substring(6, token.length()).strip());
			FarmacyAdmin farmacyAdmin =  farmAdminRepo.findById(username).get();
			Optional<Offer> offer = offerRepo.findById(id);
			if(offer.isEmpty()) return ResponseEntity.notFound().build();
			if(!offer.get().getOrder().getMaker().equals(farmacyAdmin)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(!offer.get().getOrder().getExpiration().isBefore(LocalDateTime.now())) return ResponseEntity.badRequest().build();
			offer.get().setStatus(OfferStatus.ACCEPTED);
			List<Offer> otherOffers = offerRepo.findAllByOrderId(offer.get().getOrder().getOrderId());
			otherOffers.removeIf(e-> e.getOfferid().equals(offer.get().getOfferid()));
			otherOffers.forEach(o-> o.setStatus(OfferStatus.NOT_ACCEPTED));
			PurchaseOrder order = offer.get().getOrder();
			order.setStatus(OrderStatus.DONE);
			orderRepo.save(order);
			offerRepo.save(offer.get());
			offerRepo.saveAll(otherOffers);
			mailUtil.notifySuppliersAboutOffersStatus(otherOffers, offer.get());
			farmacyAdmin.getFarmacy().addQtys(order.getDrugsToPurchase());
			farmacyRepo.save(farmacyAdmin.getFarmacy());
			return ResponseEntity.ok().build();
		}
		
		

}
