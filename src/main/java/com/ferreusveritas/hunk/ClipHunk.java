package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class ClipHunk extends Hunk {
	
	public static final String TYPE = "clip";
	
	private final Hunk hunk;
	private final AABBD bounds;
	
	public ClipHunk(Scene scene, Hunk hunk, AABBD bounds) {
		super(scene);
		this.hunk = hunk;
		this.bounds = bounds;
	}
	
	public ClipHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
		this.bounds = src.getObj(BOUNDS).map(AABBD::new).orElseThrow(missing(BOUNDS));
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
		if(bounds.isInside(pos)) {
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
