package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class PyramidShape extends Shape {

	public static final String TYPE = "pyramid";
	public static final double DEFAULT_HEIGHT = 1.0;
	public static final double DEFAULT_BASE = 2.0;
	
	private final double height;
	private final double base;
	private final AABBD bounds;
	
	public PyramidShape(Scene scene) {
		this(scene, DEFAULT_HEIGHT, DEFAULT_BASE);
	}
	
	public PyramidShape(Scene scene, double height, double base) {
		super(scene);
		this.height = height;
		this.base = base;
		this.bounds = calcBounds(height, base);
	}
	
	public PyramidShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.height = src.getDouble("height").orElse(DEFAULT_HEIGHT);
		this.base = src.getDouble("base").orElse(DEFAULT_BASE);
		this.bounds = calcBounds(height, base);
	}
	
	private AABBD calcBounds(double height, double base) {
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
		if(bounds.contains(pos)) {
			double halfBase = base / 2.0;
			double x = Math.abs(pos.x());
			double z = Math.abs(pos.z());
			double y = pos.y();
			double slope = height / halfBase;
			double heightAtPos = slope * (halfBase - Math.max(x, z));
			if(y < heightAtPos) {
				return 1.0;
			}
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("height", height)
			.set("base", base);
	}
}
