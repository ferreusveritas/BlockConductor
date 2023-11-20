package com.ferreusveritas.support;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Factory<T> {
	
	private final Map<String, Function<JsonObj, T>> factoryMap;
	
	private Factory(Builder<T> builder) {
		this.factoryMap = Map.copyOf(builder.factoryMap);
	}
	
	public T create(JsonObj src) {
		String type = src.getString("type").orElseThrow(() -> new InvalidJsonProperty("Missing type"));
		Function<JsonObj, T> factory = factoryMap.get(type);
		if(factory == null) {
			throw new InvalidJsonProperty("Unknown type: " + type);
		}
		return factory.apply(src);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder<T> {
		
		private final Map<String, Function<JsonObj, T>> factoryMap = new HashMap<>();
		
		public Builder<T> add(String type, Function<JsonObj, T> factory) {
			factoryMap.put(type, factory);
			return this;
		}
		
		public Factory<T> build() {
			return new Factory<>(this);
		}
		
	}

}