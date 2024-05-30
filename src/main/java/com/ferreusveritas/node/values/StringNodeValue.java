package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

import java.util.Optional;

public class StringNodeValue extends NodeValue {
	
	@JsonProperty
	private final String def;
	
	@JsonProperty
	private final Integer maxLength;
	
	private StringNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		String def,
		Integer maxLength
	) {
		super(name, span, nullable);
		this.def = def;
		this.maxLength = maxLength;
	}
	
	@Override
	public String getType() {
		return "string";
	}
	
	public Optional<String> def() {
		return Optional.ofNullable(def);
	}
	
	public Optional<Integer> maxLength() {
		return Optional.ofNullable(maxLength);
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<StringNodeValue> {
		
		private String def = "";
		private Integer maxLength = null;
		
		public Builder(String name) {
			super(name);
		}
		
		public Builder def(String def) {
			this.def = def;
			return this;
		}
		
		public Builder maxLength(Integer maxLength) {
			this.maxLength = maxLength;
			return this;
		}
		
		@Override
		public StringNodeValue build() {
			return new StringNodeValue(name, span, nullable, def, maxLength);
		}
		
	}
	
}
