package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

import java.util.Optional;

public class BlockNodeValue extends NodeValue {
	
	@JsonProperty
	private final String def;
	
	private BlockNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		String def
	) {
		super(name, span, nullable);
		this.def = def;
	}
	
	@Override
	public String getType() {
		return "block";
	}
	
	public Optional<String> def() {
		return Optional.ofNullable(def);
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<BlockNodeValue> {
		
		private String def = "";
		
		public Builder(String name) {
			super(name);
		}
		
		public Builder def(String def) {
			this.def = def;
			return this;
		}
		
		@Override
		public BlockNodeValue build() {
			return new BlockNodeValue(name, span, nullable, def);
		}
		
	}
	
}
