package com.ferreusveritas.support;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class BiFactory<T> {
	
	private final Map<String, BiFunction<Scene, JsonObj, T>> factoryMap;
	
	private BiFactory(Builder<T> builder) {
		this.factoryMap = Map.copyOf(builder.factoryMap);
	}
	
	public T create(Scene scene, JsonObj src) {
		String type = src.getString("type").orElseThrow(
			() -> new InvalidJsonProperty("Missing type")
		);
		BiFunction<Scene, JsonObj, T> factory = factoryMap.get(type);
		if(factory == null) {
			throw new InvalidJsonProperty("Unknown type: " + type);
		}
		return factory.apply(scene, src);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder<T> {
		
		private final Map<String, BiFunction<Scene, JsonObj, T>> factoryMap = new HashMap<>();
		
		public Builder<T> add(String type, BiFunction<Scene, JsonObj, T> factory) {
			factoryMap.put(type, factory);
			return this;
		}
		
		public BiFactory<T> build() {
			return new BiFactory<>(this);
		}
		
	}

}