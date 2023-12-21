package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class LayerShape extends Shape {
	
	public static final String TYPE = "layer";
	public static final String MIN = "min";
	public static final String MAX = "max";
	
	private final double min; // inclusive
	private final double max; // exclusive
	private final AABBD bounds;
	
	private LayerShape(UUID uuid, double min, double max) {
		super(uuid);
		this.min = min;
		this.max = max;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(Double.NEGATIVE_INFINITY, min, Double.NEGATIVE_INFINITY),
			new Vec3D(Double.POSITIVE_INFINITY, max, Double.POSITIVE_INFINITY)
		);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.y() >= min && pos.y() < max ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MIN, min)
			.set(MAX, max);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double min = 0.0;
		private double max = 1.0;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder min(double min) {
			this.min = min;
			return this;
		}
		
		public Builder max(double max) {
			this.max = max;
			return this;
		}
		
		public LayerShape build() {
			return new LayerShape(uuid, min, max);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double min; // inclusive
		private final double max; // exclusive
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.min = src.getDouble(MIN).orElse(0.0);
			this.max = src.getDouble(MAX).orElse(1.0);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new LayerShape(getUuid(), min, max);
		}
		
	}
	
}
