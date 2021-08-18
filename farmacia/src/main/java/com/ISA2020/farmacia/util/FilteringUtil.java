package com.ISA2020.farmacia.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.Drug;
import com.ISA2020.farmacia.entity.Farmacy;
import com.ISA2020.farmacia.entity.Price;
@Component
public class FilteringUtil {
	
	public FilteringUtil() {}
	
	public List<Drug> filterPrices(List<Drug> drugs) {
		List<Drug> result = new ArrayList<>();
		for(Drug dr :  drugs) {
			
			List<Farmacy> farm = (List<Farmacy>) dr.getFarmacies();
			for (Farmacy f : farm) {
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

}
