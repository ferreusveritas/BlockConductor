package com.ferreusveritas.shapes;

import com.ferreusveritas.hunk.Hunk;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class HunkShape extends Shape {
	
	public static final String TYPE = "hunk";
	public static final String HUNK = "hunk";
	public static final String THRESHOLD = "threshold";
	public static final double DEFAULT_THRESHOLD = 0.5;
	
	private final double threshold;
	private final Hunk hunk;
	
	public HunkShape(Scene scene, Hunk hunk) {
		this(scene, hunk, DEFAULT_THRESHOLD);
	}
	
	public HunkShape(Scene scene, Hunk hunk, double threshold) {
		super(scene);
		this.threshold = threshold;
		this.hunk = hunk;
	}
	
	public HunkShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.threshold = src.getDouble(THRESHOLD).orElse(DEFAULT_THRESHOLD);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(hunk.bounds().toAABBI());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return hunk.getVal(pos.toVecD()) > threshold;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(THRESHOLD, threshold)
			.set(HUNK, hunk);
	}
	
}
