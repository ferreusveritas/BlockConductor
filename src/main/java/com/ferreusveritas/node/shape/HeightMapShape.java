package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class HeightMapShape extends Shape {

	public static final String TYPE = "heightmap";
	public static final String HEIGHT = "height";
	public static final String MIN_Y = "minY";
	public static final double DEFAULT_HEIGHT = 255.0;
	public static final double DEFAULT_MIN_Y = 0.0;
	
	private final Shape shape;
	private final double height;
	private final double minY;
	private final AABBD bounds;
	
	private HeightMapShape(UUID uuid, Shape shape, double height, double minY) {
		super(uuid);
		this.shape = shape;
		this.height = height;
		this.minY = minY;
		this.bounds = calculateBounds(height);
		validate();
	}
	
	private void validate() {
		if(height < 1.0) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
	}
	
	public AABBD calculateBounds(double height) {
		AABBD shapeBounds = shape.bounds();
		Vec3D min = shapeBounds.min().withY(Double.NEGATIVE_INFINITY);
		Vec3D max = shapeBounds.max().withY(height);
		return new AABBD(min, max);
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
		if(pos.y() < minY) {
			return 1.0;
		}
		double val = shape.getVal(pos.withY(0.0));
		double h = val * height;
		if(pos.y() < h) {
			return 1.0;
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HEIGHT, height)
			.set(MIN_Y, minY)
			.set(SHAPE, shape);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = VoidShape.VOID;
		private double height = DEFAULT_HEIGHT;
		private double minY = DEFAULT_MIN_Y;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder height(double height) {
			this.height = height;
			return this;
		}
		
		public Builder minY(double minY) {
			this.minY = minY;
			return this;
		}
		
		public HeightMapShape build() {
			return new HeightMapShape(uuid, shape, height, minY);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final double height;
		private final double minY;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.height = src.getDouble(HEIGHT).orElse(DEFAULT_HEIGHT);
			this.minY = src.getDouble(MIN_Y).orElse(DEFAULT_MIN_Y);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new HeightMapShape(getUuid(), s, height, minY);
		}
		
	}
	
}
