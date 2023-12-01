package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class CubeHunk extends Hunk {
	
	public static final String TYPE = "cube";
	
	private final Vec3D size;
	private final boolean centered;
	private final AABBD aabb;
	
	public CubeHunk(Scene scene, Vec3D size, boolean centered) {
		super(scene);
		this.size = size;
		this.centered = centered;
		this.aabb = createBounds(size, centered);
	}
	
	public CubeHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.size = src.getObj("size").map(Vec3D::new).orElse(Vec3D.ONE);
		this.centered = src.getBoolean("centered").orElse(false);
		this.aabb = createBounds(size, centered);
	}
	
	private static AABBD createBounds(Vec3D size, boolean centered) {
		Vec3D min = centered ? size.div(2).neg() : Vec3D.ZERO;
		Vec3D max = min.add(size);
		return new AABBD(min, max);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return aabb;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return aabb.isInside(pos) ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("size", size)
			.set("centered", centered);
	}
	
}
