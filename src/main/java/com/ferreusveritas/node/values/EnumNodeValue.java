package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.DataSpan;

import java.util.ArrayList;
import java.util.List;

public class EnumNodeValue extends NodeValue {
	
	@JsonProperty
	private final List<String> values;
	
	@JsonProperty
	private final String def;

	private EnumNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		List<String> values,
		String def
	) {
		super(name, span, nullable);
		this.values = List.copyOf(values);
		this.def = def;
		validate();
	}
	
	private void validate() {
		if(values.isEmpty()) {
			throw new IllegalArgumentException("EnumNodeValue must have at least one value");
		}
		if(def != null && !values.contains(def)) {
			throw new IllegalArgumentException("Invalid value for EnumNodeValue: " + def);
		}
	}
	
	@Override
	public String getType() {
		return "enum";
	}

	public List<String> values() {
		return values;
	}

	public String def() {
		return def;
	}

	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////

	public static class Builder extends NodeValue.Builder<EnumNodeValue> {

		private final List<String> values = new ArrayList<>();
		private String def = "";

		public Builder(String name) {
			super(name);
		}
		
		public Builder values(Class<? extends Enum<?>> enumClass) {
			Enum<?>[] constants = enumClass.getEnumConstants();
			for(Enum<?> constant : constants) {
				values.add(constant.name());
			}
			return this;
		}
		
		public Builder def(Enum<?> def) {
			this.def = def.name();
			return this;
		}
		
		@Override
		public EnumNodeValue build() {
			return new EnumNodeValue(name, span, nullable, values, def);
		}

	}

}
