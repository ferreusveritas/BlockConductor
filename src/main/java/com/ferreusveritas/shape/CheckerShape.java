package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class CheckerShape extends Shape {
	
	public static final String TYPE = "checker";
	public static final String AXIS = "axis";
	
	private final Axis axis; // null means all axes(3D checker block)
	
	public CheckerShape(Scene scene, Axis axis) {
		super(scene);
		this.axis = axis;
	}
	
	public CheckerShape(Scene scene, JsonObj src) {
		super(scene);
		this.axis = src.getString(AXIS).flatMap(Axis::of).orElse(null);
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
		return isInside(pos.toVecI()) ? 1.0 : 0.0;
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(axis == null) {
			return (pos.x() + pos.y() + pos.z()) % 2 == 0;
		}
		return switch (axis) {
			case X -> pos.y() + pos.z() % 2 == 0;
			case Y -> pos.x() + pos.z() % 2 == 0;
			case Z -> pos.x() + pos.y() % 2 == 0;
		};
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(AXIS, axis);
	}
	
}
