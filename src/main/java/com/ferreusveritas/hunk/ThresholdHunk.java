package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class ThresholdHunk extends Hunk {
	
	public static final String TYPE = "threshold";
	public static final double DEFAULT_THRESHOLD = 0.5;
	public static final String THRESHOLD = "threshold";
	
	private final Hunk hunk;
	private final double threshold;
	
	public ThresholdHunk(Scene scene, Hunk hunk) {
		this(scene, hunk, DEFAULT_THRESHOLD);
	}
	
	public ThresholdHunk(Scene scene, Hunk hunk, double threshold) {
		super(scene);
		this.hunk = hunk;
		this.threshold = threshold;
	}
	
	public ThresholdHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
		this.threshold = src.getDouble(THRESHOLD).orElse(DEFAULT_THRESHOLD);
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
		return hunk.getVal(pos) > threshold ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HUNK, hunk)
			.set(THRESHOLD, threshold);
	}
	
}
