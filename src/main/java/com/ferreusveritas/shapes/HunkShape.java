package com.ferreusveritas.shapes;

import com.ferreusveritas.hunk.Hunk;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class HunkShape extends Shape {
	
	public static final String TYPE = "hunk";
	public static final double DEFAULT_THRESHOLD = 0.5;
	
	private final Hunk hunk;
	private final double threshold;
	
	public HunkShape(Scene scene, Hunk hunk) {
		this(scene, hunk, DEFAULT_THRESHOLD);
	}
	
	public HunkShape(Scene scene, Hunk hunk, double threshold) {
		super(scene);
		this.hunk = hunk;
		this.threshold = threshold;
	}
	
	public HunkShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj("hunk").map(scene::createHunk).orElseThrow(() -> new InvalidJsonProperty("Missing hunk"));
		this.threshold = src.getDouble("threshold").orElse(DEFAULT_THRESHOLD);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(hunk.bounds());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return hunk.getVal(pos.toVecD()) > threshold;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("hunk", hunk)
			.set("threshold", threshold);
	}
	
}
