package com.ferreusveritas.factory;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Factory<R> {
	
	private final Map<String, Function<JsonObj, R>> factoryMap;
	
	private Factory(Builder<R> builder) {
		this.factoryMap = Map.copyOf(builder.factoryMap);
	}
	
	public R create(JsonObj src) {
		String type = src.getString("type").orElseThrow(() -> new InvalidJsonProperty("Missing type"));
		var factory = factoryMap.get(type);
		if(factory == null) {
			throw new InvalidJsonProperty("Unknown type: " + type);
		}
		return factory.apply(src);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder<R> {
		
		private final Map<String, Function<JsonObj, R>> factoryMap = new HashMap<>();
		
		public Builder<R> add(String type, Function<JsonObj, R> factory) {
			factoryMap.put(type, factory);
			return this;
		}
		
		public Factory<R> build() {
			return new Factory<>(this);
		}
		
	}

}