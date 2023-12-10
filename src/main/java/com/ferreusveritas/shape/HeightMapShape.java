package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

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
	
	public HeightMapShape(Scene scene, Shape shape) {
		this(scene, shape, DEFAULT_HEIGHT, DEFAULT_MIN_Y);
	}
	
	public HeightMapShape(Scene scene, Shape shape, double height) {
		this(scene, shape, height, DEFAULT_MIN_Y);
	}
	
	public HeightMapShape(Scene scene, Shape shape, double height, double minY) {
		super(scene);
		this.shape = shape;
		this.height = height;
		this.minY = minY;
		this.bounds = calcBounds(height);
		validate();
	}
	
	public HeightMapShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.height = src.getDouble(HEIGHT).orElse(DEFAULT_HEIGHT);
		this.minY = src.getDouble(MIN_Y).orElse(DEFAULT_MIN_Y);
		this.bounds = calcBounds(height);
		validate();
	}
	
	private void validate() {
		if(height < 1.0) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
	}
	
	public AABBD calcBounds(double height) {
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
	
}
