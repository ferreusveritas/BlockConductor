package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class GradientShape extends Shape {
	
	public static final String TYPE = "gradient";
	public static final String MIN_Y = "minY";
	public static final String MAX_Y = "maxY";
	public static final double DEFAULT_MIN_Y = 0.0;
	public static final double DEFAULT_MAX_Y = 255.0;
	
	private final double minY;
	private final double maxY;
	
	private GradientShape(UUID uuid, double minY, double maxY) {
		super(uuid);
		this.minY = minY;
		this.maxY = maxY;
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
		if(pos.y() <= minY) {
			return 1.0;
		}
		if(pos.y() >= maxY) {
			return 0.0;
		}
		return 1.0 - ((pos.y() - minY) / (maxY - minY));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MIN_Y, minY)
			.set(MAX_Y, maxY);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double minY = DEFAULT_MIN_Y;
		private double maxY = DEFAULT_MAX_Y;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder minY(double minY) {
			this.minY = minY;
			return this;
		}
		
		public Builder maxY(double maxY) {
			this.maxY = maxY;
			return this;
		}
		
		public GradientShape build() {
			return new GradientShape(uuid, minY, maxY);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double minY;
		private final double maxY;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.minY = src.getDouble(MIN_Y).orElse(DEFAULT_MIN_Y);
			this.maxY = src.getDouble(MAX_Y).orElse(DEFAULT_MAX_Y);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new GradientShape(getUuid(), minY, maxY);
		}
		
	}
	
}
