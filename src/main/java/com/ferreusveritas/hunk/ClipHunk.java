package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class ClipHunk extends Hunk {
	
	public static final String TYPE = "clip";
	
	private final Hunk hunk;
	private final AABBI bounds;
	
	public ClipHunk(Scene scene, Hunk hunk, AABBI bounds) {
		super(scene);
		this.hunk = hunk;
		this.bounds = bounds;
	}
	
	public ClipHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
		this.bounds = src.getObj(BOUNDS).map(AABBI::new).orElseThrow(missing(BOUNDS));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBI bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		Vec3I vec = pos.toVecI();
		if(bounds.isInside(vec)) {
			return hunk.getVal(pos);
		}
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HUNK, hunk)
			.set(BOUNDS, bounds);
	}
}
