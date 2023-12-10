package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class GrowShape extends Shape {
	
	public static final String TYPE = "grow";
	
	private final Shape shape;
	private final AABBD bounds;
	
	public GrowShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	public GrowShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return shape.bounds().expand(1.0);
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
		if(!bounds.contains(pos)){
			return 0.0;
		}
		return
			isInside(pos.down()) ||
			isInside(pos.up()) ||
			isInside(pos.north()) ||
			isInside(pos.south()) ||
			isInside(pos.west()) ||
			isInside(pos.east())
			? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape.toJsonObj());
	}
	
}
