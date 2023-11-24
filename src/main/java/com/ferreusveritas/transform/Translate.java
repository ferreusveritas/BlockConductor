package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.support.json.JsonObj;

public class Translate extends Transform {
	
	public static final String TYPE = "translate";
	
	private final Vec3D offset;
	private final Matrix4X4 matrix;
	
	public Translate(Vec3D offset) {
		this.offset = offset;
		this.matrix = Matrix4X4.IDENTITY.translate(offset);
	}
	
	public Translate(JsonObj src) {
		this(src.getObj("offset").map(Vec3D::new).orElse(Vec3D.ZERO));
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
			.set("offset", offset);
	}
	
}
