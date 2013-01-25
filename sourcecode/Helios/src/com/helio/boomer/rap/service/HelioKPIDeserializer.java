package com.helio.boomer.rap.service;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class HelioKPIDeserializer<HelioKPI> implements
		JsonDeserializer<HelioKPI> {

	@Override
	public HelioKPI deserialize(
			JsonElement jsonElement,
			Type typeArg,
			JsonDeserializationContext context) throws JsonParseException {
		//TODO: Not implemented yet.
		return null;
	}

}
