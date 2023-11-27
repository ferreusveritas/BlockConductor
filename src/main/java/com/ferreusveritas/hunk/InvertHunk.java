package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class InvertHunk extends Hunk {
	
	public static final String TYPE = "invert";
	
	private final Hunk hunk;
	
	public InvertHunk(Scene scene, Hunk hunk) {
		super(scene);
		this.hunk = hunk;
	}
	
	public InvertHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return hunk.bounds();
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return 1.0 - hunk.getVal(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HUNK, hunk);
	}
	
}
