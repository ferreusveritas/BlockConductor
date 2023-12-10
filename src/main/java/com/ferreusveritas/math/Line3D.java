package com.ferreusveritas.math;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

public record Line3D(
	Vec3D start,
	Vec3D end
) implements Jsonable {
	
	public static final String START = "start";
	public static final String END = "end";
	
	public Line3D(JsonObj src) {
		this(
			src.getObj(START).map(Vec3D::new).orElseThrow(),
			src.getObj(END).map(Vec3D::new).orElseThrow()
		);
	}
	
	public double length() {
		return start.distanceTo(end);
	}
	
	public Vec3D vec3D() {
		return end.sub(start);
	}
	
	public Line3D offset(Vec3D v) {
		return new Line3D(start.add(v), end.add(v));
	}
	
	public Line3D flip() {
		return new Line3D(end, start);
	}
	
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set(START, start)
			.set(END, end);
	}
	
}
