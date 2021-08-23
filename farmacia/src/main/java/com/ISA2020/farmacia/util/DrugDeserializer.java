package com.ISA2020.farmacia.util;

import com.ISA2020.farmacia.entity.Drug;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import io.jsonwebtoken.io.IOException;

public class DrugDeserializer extends KeyDeserializer {

	  @Override
	  public Drug deserializeKey(
	    String key, 
	    DeserializationContext ctxt) throws IOException, 
	    JsonProcessingException {
	      
	      return new Drug(key);
	    }
	}
