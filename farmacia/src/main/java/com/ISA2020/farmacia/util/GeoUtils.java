package com.ISA2020.farmacia.util;

import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Component;

import com.ISA2020.farmacia.entity.DTO.CoordinatesDTO;

@Component
public class GeoUtils 
{
	
	public String getQueryAdress(String adress) {
		
		return adress.replaceAll(" ", "%20");
	}

	public CoordinatesDTO getLatLong(String result) throws JSONException {
		CoordinatesDTO answer = new CoordinatesDTO();

		String[] splited =result.split(":");
		String latWithComa = splited[2];

		String longiWithComa = splited[3];

		String[] latSplit =  latWithComa.split(",");
		String[] longSplit =  longiWithComa.split(",");
		String lat = latSplit[0];

		String longi =longSplit[0];

		answer.setLatitude(Double.parseDouble(lat));
		answer.setLongitude(Double.parseDouble(longi));

		return answer;
	}

  
}