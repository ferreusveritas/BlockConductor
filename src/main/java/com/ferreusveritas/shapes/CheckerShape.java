package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class CheckerShape extends Shape {
	
	public static final String TYPE = "checker";
	
	private final Axis axis; // null means all axes(3D checker block)
	
	public CheckerShape(Scene scene) {
		this(scene, (Axis)null);
	}
	
	public CheckerShape(Scene scene, Axis axis) {
		super(scene);
		this.axis = axis;
	}
	
	public CheckerShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.axis = src.getString("axis").flatMap(Axis::of).orElse(null);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
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
			.set("axis", axis);
	}
	
}
