package com.ferreusveritas.resources.model.qsp;

import java.util.HashMap;
import java.util.Map;

public class QspModel {
	
	private final Map<String, QspModelPart> parts;
	
	private QspModel(Map<String, QspModelPart> parts) {
		this.parts = Map.copyOf(parts);
	}
	
	public QspModelPart getPart(String name) {
		return parts.get(name);
	}
	
	public int getPartCount() {
		return parts.size();
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final Map<String, QspModelPart> parts = new HashMap<>();
		
		public Builder() {
		}
		
		public Builder addPart(String name, QspModelPart part) {
			parts.put(name, part);
			return this;
		}
		
		public QspModel build() {
			return new QspModel(parts);
		}
		
	}
	
}
