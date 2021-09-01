package com.ISA2020.farmacia.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class GeoUtils 
{
	
	public String getQueryAdress(String adress) {
		
		return adress.replaceAll(" ", "%20");
	}

	public JSONObject getLatLong(String result) throws JSONException {
		JSONObject answer = new JSONObject();

		String[] splited =result.split(":");
		String latWithComa = splited[2];

		String longiWithComa = splited[3];

		String[] latSplit =  latWithComa.split(",");
		String[] longSplit =  longiWithComa.split(",");
		String lat = latSplit[0];

		String longi =longSplit[0];

		answer.put("latitude", lat);
		answer.put("longitude",longi);

		return answer;
	}

  
}