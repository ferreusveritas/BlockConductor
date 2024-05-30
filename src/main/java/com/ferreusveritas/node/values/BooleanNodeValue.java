package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

import java.util.Optional;

public class BooleanNodeValue extends NodeValue {
	
	@JsonProperty
	private final Boolean def;

	private BooleanNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		Boolean def
	) {
		super(name, span, nullable);
		this.def = def;
	}
	
	@Override
	public String getType() {
		return "boolean";
	}
	
	public Optional<Boolean> def() {
		return Optional.ofNullable(def);
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<BooleanNodeValue> {
		
		private Boolean def = null;
		
		public Builder(String name) {
			super(name);
		}
		
		public Builder def(Boolean def) {
			this.def = def;
			return this;
		}
		
		@Override
		public BooleanNodeValue build() {
			return new BooleanNodeValue(name, span, nullable, def);
		}
		
	}
	
}
