package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

import java.util.Optional;

public class NumberNodeValue extends NodeValue {
	
	@JsonProperty
	private final Number def;
	
	@JsonProperty
	private final Number min;
	
	@JsonProperty
	private final Number max;
	
	@JsonProperty
	private final Number increment;
	
	private NumberNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		Number def,
		Number min,
		Number max,
		Number increment
	) {
		super(name, span, nullable);
		this.def = def;
		this.min = min;
		this.max = max;
		this.increment = increment;
	}
	
	@Override
	public String getType() {
		return "number";
	}
	
	public Optional<Number> def() {
		return Optional.ofNullable(def);
	}
	
	public Optional<Number> min() {
		return Optional.ofNullable(min);
	}
	
	public Optional<Number> max() {
		return Optional.ofNullable(max);
	}
	
	public Optional<Number> increment() {
		return Optional.ofNullable(increment);
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<NumberNodeValue> {
		
		private Number def = 0;
		private Number min = null;
		private Number max = null;
		private Number increment = null;
		
		public Builder(String name) {
			super(name);
		}
		
		public Builder def(Number def) {
			this.def = def;
			return this;
		}
		
		public Builder min(Number min) {
			this.min = min;
			return this;
		}
		
		public Builder max(Number max) {
			this.max = max;
			return this;
		}
		
		public Builder integer() {
			return min(0).max(Integer.MAX_VALUE).increment(1);
		}
		
		public Builder increment(Number increment) {
			this.increment = increment;
			return this;
		}
		
		@Override
		public NumberNodeValue build() {
			return new NumberNodeValue(name, span, nullable, def, min, max, increment);
		}
		
	}
	
}
