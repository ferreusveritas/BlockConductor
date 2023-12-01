package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A Hunk that slices another Hunk along Y = 0 and infinitely extrudes the result in the Y direction.
 */
public class SliceHunk extends Hunk {

	public static final String TYPE = "slice";
	
	private final Hunk hunk;
	private final AABBD aabb;
	
	public SliceHunk(Scene scene, Hunk hunk) {
		super(scene);
		this.hunk = hunk;
		this.aabb = calcBounds(hunk);
	}
	
	public SliceHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj("hunk").map(scene::createHunk).orElseThrow(missing("hunk"));
		this.aabb = calcBounds(hunk);
	}
	
	private AABBD calcBounds(Hunk hunk) {
		AABBD bounds = hunk.bounds();
		return new AABBD(
			bounds.min().withY(Double.NEGATIVE_INFINITY),
			bounds.max().withY(Double.POSITIVE_INFINITY)
		);
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
		return hunk.getVal(pos.withY(0));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("hunk", hunk);
	}
	
}
