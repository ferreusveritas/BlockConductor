package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class SphereShape extends Shape {
	
	public static final String TYPE = "sphere";
	public static final String RADIUS = "radius";
	
	private final double radius;
	private final AABBD bounds;
	
	private SphereShape(UUID uuid, double radius) {
		super(uuid);
		this.radius = radius;
		this.bounds = calculateBounds(radius);
	}
	
	private static AABBD calculateBounds(double radius) {
		if(radius <= 0) {
			throw new IllegalArgumentException("radius must be greater than zero");
		}
		return new AABBD(
			new Vec3D(-radius, -radius, -radius),
			new Vec3D(radius, radius, radius)
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
		return pos.lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(RADIUS, radius);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid;
		private double radius;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder radius(double radius) {
			this.radius = radius;
			return this;
		}
		
		public SphereShape build() {
			return new SphereShape(uuid, radius);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double radius;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.radius = src.getDouble(RADIUS).orElse(1.0);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new SphereShape(getUuid(), radius);
		}
		
	}
	
}
