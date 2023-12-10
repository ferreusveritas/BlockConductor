package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class GradientShape extends Shape {
	
	public static final String TYPE = "gradient";
	public static final String MIN_Y = "minY";
	public static final String MAX_Y = "maxY";
	public static final double DEFAULT_MIN_Y = 0.0;
	public static final double DEFAULT_MAX_Y = 255.0;
	
	private final double minY;
	private final double maxY;
	
	public GradientShape(Scene scene) {
		this(scene, DEFAULT_MIN_Y, DEFAULT_MAX_Y);
	}
	
	public GradientShape(Scene scene, double minY, double maxY) {
		super(scene);
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public GradientShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.minY = src.getDouble(MIN_Y).orElse(DEFAULT_MIN_Y);
		this.maxY = src.getDouble(MAX_Y).orElse(DEFAULT_MAX_Y);
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
	
}
