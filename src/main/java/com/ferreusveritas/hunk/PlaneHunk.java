package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A Hunk that represents a plane at y = 0 with one side being 1.0 and the other 0.0
 */
public class PlaneHunk extends Hunk {
	
	public static final String TYPE = "plane";
	
	public PlaneHunk(Scene scene) {
		super(scene);
	}
	
	public PlaneHunk(Scene scene, JsonObj src) {
		super(scene, src);
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
		return pos.y() < 0 ? 1.0 : 0.0;
	}
	
}
