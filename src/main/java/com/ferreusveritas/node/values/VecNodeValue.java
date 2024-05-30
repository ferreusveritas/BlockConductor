package com.ferreusveritas.node.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.DataSpan;

import java.util.Optional;

public class VecNodeValue extends NodeValue {
	
	@JsonProperty
	private final Vec3D def;
	
	@JsonProperty
	private final Vec3D min;
	
	@JsonProperty
	private final Vec3D max;
	
	@JsonProperty
	private final Number increment;
	
	public VecNodeValue(
		String name,
		DataSpan span,
		boolean nullable,
		Vec3D def,
		Vec3D min,
		Vec3D max,
		Number increment
	) {
		super(name, span, nullable);
		this.def = def;
		this.min = min;
		this.max = max;
		this.increment = increment;
	}
	
	public Optional<Vec3D> def() {
		return Optional.ofNullable(def);
	}
	
	public Optional<Vec3D> min() {
		return Optional.ofNullable(min);
	}
	
	public Optional<Vec3D> max() {
		return Optional.ofNullable(max);
	}
	
	public Optional<Number> increment() {
		return Optional.ofNullable(increment);
	}
	
	@Override
	public String getType() {
		return "vec";
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder extends NodeValue.Builder<VecNodeValue> {
		
		private Vec3D def = null;
		private Vec3D min = null;
		private Vec3D max = null;
		private Number increment = null;
		
		public Builder(String name) {
			super(name);
		}
		
		public Builder def(Vec3D def) {
			this.def = def;
			return this;
		}
		
		public Builder min(Vec3D min) {
			this.min = min;
			return this;
		}
		
		public Builder max(Vec3D max) {
			this.max = max;
			return this;
		}
		
		public Builder increment(Number increment) {
			this.increment = increment;
			return this;
		}
		
		@Override
		public VecNodeValue build() {
			return new VecNodeValue(name, span, nullable, def, min, max, increment);
		}
		
	}
	
}
