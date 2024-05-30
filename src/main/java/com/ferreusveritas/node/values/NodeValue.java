package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

public abstract class NodeValue {
	
	public abstract String getType();
	
	@JsonProperty
	private final String name;
	
	@JsonProperty
	private final DataSpan span;
	
	@JsonProperty
	private final boolean nullable;
	
	protected NodeValue(String name, DataSpan span, boolean nullable) {
		this.name = name;
		this.span = span;
		this.nullable = nullable;
	}
	
	public String name() {
		return name;
	}
	
	public DataSpan span() {
		return span;
	}
	
	public boolean nullable() {
		return nullable;
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public abstract static class Builder<T extends NodeValue> {
		
		protected final String name;
		protected DataSpan span = DataSpan.SINGLE;
		protected boolean nullable = false;
		
		protected Builder(String name) {
			this.name = name;
		}
		
		public Builder<T> span(DataSpan span) {
			this.span = span;
			return this;
		}
		
		public Builder<T> nullable(boolean nullable) {
			this.nullable = nullable;
			return this;
		}
		
		public abstract T build();
		
	}
	
}
