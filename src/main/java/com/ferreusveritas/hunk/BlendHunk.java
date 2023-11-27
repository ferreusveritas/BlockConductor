package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * BlendHunk is a type of Hunk that blends the values of two hunks together using a blend hunk as a mask.
 */
public class BlendHunk extends Hunk {
	
	public static final String TYPE = "blend";
	public static final String HUNK_1 = "hunk1";
	public static final String HUNK_2 = "hunk2";
	public static final String BLEND = "blend";
	
	private final Hunk hunk1;
	private final Hunk hunk2;
	private final Hunk blend;
	private final AABBI bounds;
	
	public BlendHunk(Scene scene, Hunk hunk1, Hunk hunk2, Hunk blend) {
		super(scene);
		this.hunk1 = hunk1;
		this.hunk2 = hunk2;
		this.blend = blend;
		this.bounds = AABBI.union(hunk1.bounds(), hunk2.bounds());
	}
	
	public BlendHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk1 = src.getObj(HUNK_1).map(scene::createHunk).orElseThrow(missing(HUNK_1));
		this.hunk2 = src.getObj(HUNK_2).map(scene::createHunk).orElseThrow(missing(HUNK_2));
		this.blend = src.getObj(BLEND).map(scene::createHunk).orElseThrow(missing(BLEND));
		this.bounds = AABBI.union(hunk1.bounds(), hunk2.bounds());
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
		double blendVal = blend.getVal(pos);
		if(blendVal < 0.0){
			return hunk1.getVal(pos);
		}
		if(blendVal > 1.0){
			return hunk2.getVal(pos);
		}
		double val1 = hunk1.getVal(pos);
		double val2 = hunk2.getVal(pos);
		return MathHelper.lerp(blendVal, val1, val2);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HUNK_1, hunk1)
			.set(HUNK_2, hunk2)
			.set(BLEND, blend);
	}
}
