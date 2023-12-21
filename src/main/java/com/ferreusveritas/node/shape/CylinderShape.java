package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class CylinderShape extends Shape {

	public static final String TYPE = "cylinder";
	
	public static final double DEFAULT_HEIGHT = 1;
	public static final double DEFAULT_RADIUS = 1.0;
	public static final String HEIGHT = "height";
	public static final String RADIUS = "radius";
	
	private final double height;
	private final double radius;
	private final AABBD bounds;
	
	private CylinderShape(UUID uuid, double height, double radius) {
		super(uuid);
		this.height = height;
		this.radius = radius;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(-radius, 0, -radius),
			new Vec3D(radius, height, radius)
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
		return pos.y() >= 0 && pos.y() < height && pos.withY(0.0).lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HEIGHT, height)
			.set(RADIUS, radius);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double height = DEFAULT_HEIGHT;
		private double radius = DEFAULT_RADIUS;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder height(double height) {
			this.height = height;
			return this;
		}
		
		public Builder radius(double radius) {
			this.radius = radius;
			return this;
		}
		
		public CylinderShape build() {
			return new CylinderShape(uuid, height, radius);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double height;
		private final double radius;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.height = src.getDouble(HEIGHT).orElse(DEFAULT_HEIGHT);
			this.radius = src.getDouble(RADIUS).orElse(DEFAULT_RADIUS);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new CylinderShape(getUuid(), height, radius);
		}
		
	}
	
}
