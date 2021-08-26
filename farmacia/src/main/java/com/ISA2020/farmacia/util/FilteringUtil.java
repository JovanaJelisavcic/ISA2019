package com.ISA2020.farmacia.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.basic.Drug;
import com.ISA2020.farmacia.entity.basic.Farmacy;
import com.ISA2020.farmacia.entity.basic.Price;
import com.ISA2020.farmacia.entity.basic.VacationPharmacist;
import com.ISA2020.farmacia.entity.basic.WorkingHours;
import com.ISA2020.farmacia.entity.intercations.Offer;
import com.ISA2020.farmacia.entity.intercations.PurchaseOrder;
import com.ISA2020.farmacia.entity.users.Dermatologist;
import com.ISA2020.farmacia.entity.users.Pharmacist;
@Component
public class FilteringUtil {
	
	public FilteringUtil() {}
	
	public List<Drug> filterPricesAndFields(List<Drug> drugs) {
		List<Drug> result = new ArrayList<>();
		for(Drug dr :  drugs) {
			
			List<Farmacy> farm = (List<Farmacy>) dr.getFarmacies();
			for (Farmacy f : farm) {
				f.setAdress(null);
				f.setDescription(null);
				f.setStars(Float.NaN);
				List<Price> prices = (List<Price>) f.getPrices();
				for(Price p : prices) {
					if(!p.getDrug().getCode().equals(dr.getCode())) {
						prices.remove(p);
					}
				}
				f.setPrices( prices);
			}
			dr.setFarmacies( farm);
			result.add(dr);
		}
		return result;
	}
	public List<Dermatologist> filterDermas(List<Dermatologist> dermas, Farmacy farmacy) {
		List<Dermatologist> result = new ArrayList<>();
		for(Dermatologist d: dermas) {
			List<WorkingHours> wh = d.getWorkingHours();
			for(WorkingHours w: wh) {
				if(farmacy.getId().equals(w.getFarmacy().getId()))
					if(!result.contains(d)) result.add(d);
			}
		}
		return result;
	}

	public List<Pharmacist> filterAdress(List<Pharmacist> pharmacists) {
		for(Pharmacist p : pharmacists)
			p.getFarmacy().setAdress(null);
		return pharmacists;
	}

	

	public List<Dermatologist> filterAdressDerma(List<Dermatologist> resp) {
		for(Dermatologist p : resp)
			p.getWorkingHours().forEach(w-> w.getFarmacy().setAdress(null));
		return resp;
	}

	public List<PurchaseOrder> filterByFarmacyOrder(List<PurchaseOrder> allOrders, String id) {
		List<PurchaseOrder> result = new ArrayList<>();	
		for(PurchaseOrder order : allOrders) {
				if(order.getMaker().getFarmacy().getId().equals(id))
					result.add(order);
			}
		return result;
	}

	public Object filterByFarmacyOffer(List<Offer> allOffers, String id) {
		List<Offer> result = new ArrayList<>();	
		for(Offer offer : allOffers) {
				if(offer.getOrder().getMaker().getFarmacy().getId().equals(id))
					result.add(offer);
			}
		return result;
	}

	public List<VacationPharmacist> filterVacationsPhFarmacy(List<VacationPharmacist> vacations, String id) {
		List<VacationPharmacist> result = new ArrayList<>();
		for(VacationPharmacist vacation : vacations) {
			if(vacation.getPharmacist().getFarmacy().getId().equals(id))
				result.add(vacation);
		}
		return result;
	}

}
