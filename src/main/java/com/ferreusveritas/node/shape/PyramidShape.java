package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class PyramidShape extends Shape {

	public static final String TYPE = "pyramid";
	public static final double DEFAULT_HEIGHT = 1.0;
	public static final double DEFAULT_BASE = 2.0;
	public static final String HEIGHT = "height";
	public static final String BASE = "base";
	
	private final double height;
	private final double base;
	private final AABBD bounds;
	
	private PyramidShape(UUID uuid, double height, double base) {
		super(uuid);
		this.height = height;
		this.base = base;
		this.bounds = calculateBounds(height, base);
	}
	
	private AABBD calculateBounds(double height, double base) {
		double halfBase = base / 2.0;
		return new AABBD(
			new Vec3D(-halfBase, 0, -halfBase),
			new Vec3D(halfBase, height, halfBase)
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
		if(!bounds.contains(pos)) {
			return 0.0;
		}
		double halfBase = base / 2.0;
		double x = Math.abs(pos.x());
		double z = Math.abs(pos.z());
		double y = pos.y();
		double slope = height / halfBase;
		double heightAtPos = slope * (halfBase - Math.max(x, z));
		if(y < heightAtPos) {
			return 1.0;
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HEIGHT, height)
			.set(BASE, base);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double height = DEFAULT_HEIGHT;
		private double base = DEFAULT_BASE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder height(double height) {
			this.height = height;
			return this;
		}
		
		public Builder base(double base) {
			this.base = base;
			return this;
		}
		
		public PyramidShape build() {
			return new PyramidShape(uuid, height, base);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double height;
		private final double base;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.height = src.getDouble(HEIGHT).orElse(DEFAULT_HEIGHT);
			this.base = src.getDouble(BASE).orElse(DEFAULT_BASE);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new PyramidShape(getUuid(), height, base);
		}
		
	}
	
}
