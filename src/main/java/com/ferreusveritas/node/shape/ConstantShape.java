package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * ConstantShape is a Shape that returns a constant value for all coordinates.
 */
public class ConstantShape extends Shape {
	
	public static final String TYPE = "constant";
	public static final String VALUE = "value";
	
	private final double value;
	
	private ConstantShape(UUID uuid, double value) {
		super(uuid);
		this.value = value;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return value;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(VALUE, value);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double value = 0.0;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder value(double value) {
			this.value = value;
			return this;
		}
		
		public ConstantShape build() {
			return new ConstantShape(uuid, value);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double value;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.value = src.getDouble(VALUE).orElse(0.0);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new ConstantShape(getUuid(), value);
		}
		
	}
	
}
