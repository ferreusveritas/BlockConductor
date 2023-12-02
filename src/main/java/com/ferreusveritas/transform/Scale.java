package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.support.json.JsonObj;

public class Scale extends Transform {

	public static final String TYPE = "scale";
	public static final String SIZE = "size";
	
	private final Vec3D size;
	private final Matrix4X4 matrix;
	
	public Scale(double size) {
		this.size = new Vec3D(size, size, size);
		this.matrix = Matrix4X4.IDENTITY.scale(new Vec3D(size, size, size));
	}
	
	public Scale(Vec3D size) {
		this.size = size;
		this.matrix = Matrix4X4.IDENTITY.scale(size);
	}
	
	public Scale(JsonObj src) {
		JsonObj factorObj = src.getObj(SIZE).orElse(JsonObj.emptyVal());
		this.size = getSize(factorObj);
		this.matrix = Matrix4X4.IDENTITY.scale(size);
	}
	
	private static Vec3D getSize(JsonObj src) {
		if(src.isMap()) {
			return new Vec3D(src);
		}
		if(src.isNumber()) {
			double s = src.asDouble().orElse(1.0);
			return new Vec3D(s, s, s);
		}
		return Vec3D.ONE;
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SIZE, getSizeJsonObj(size));
	}
	
	private static JsonObj getSizeJsonObj(Vec3D factor) {
		if(factor.x() == factor.y() && factor.y() == factor.z()) {
			return new JsonObj(factor.x());
		}
		return factor.toJsonObj();
	}
	
}
