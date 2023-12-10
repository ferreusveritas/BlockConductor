package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class VoidShape extends Shape {
	
	public static final String TYPE = "void";
	
	public VoidShape(Scene scene) {
		super(scene);
	}
	
	public VoidShape(Scene scene, JsonObj src) {
		super(scene, src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.EMPTY;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return 0.0;
	}
	
}
