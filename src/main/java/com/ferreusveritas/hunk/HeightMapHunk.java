package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class HeightMapHunk extends Hunk {

	public static final String TYPE = "heightmap";
	public static final String HEIGHT = "height";
	public static final String MIN_Y = "minY";
	public static final double DEFAULT_HEIGHT = 255.0;
	public static final double DEFAULT_MIN_Y = 0.0;
	
	private final Hunk hunk;
	private final double height;
	private final double minY;
	private final AABBD bounds;
	
	public HeightMapHunk(Scene scene, Hunk hunk) {
		this(scene, hunk, DEFAULT_HEIGHT, DEFAULT_MIN_Y);
	}
	
	public HeightMapHunk(Scene scene, Hunk hunk, double height) {
		this(scene, hunk, height, DEFAULT_MIN_Y);
	}
	
	public HeightMapHunk(Scene scene, Hunk hunk, double height, double minY) {
		super(scene);
		this.hunk = hunk;
		this.height = height;
		this.minY = minY;
		this.bounds = calcBounds(height);
		validate();
	}
	
	public HeightMapHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
		this.height = src.getDouble(HEIGHT).orElse(DEFAULT_HEIGHT);
		this.minY = src.getDouble(MIN_Y).orElse(DEFAULT_MIN_Y);
		this.bounds = calcBounds(height);
		validate();
	}
	
	private void validate() {
		if(height < 1.0) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
	}
	
	public AABBD calcBounds(double height) {
		AABBD hunkBounds = hunk.bounds();
		Vec3D min = hunkBounds.min().withY(Double.NEGATIVE_INFINITY);
		Vec3D max = hunkBounds.max().withY(height);
		return new AABBD(min, max);
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
		if(pos.y() < minY) {
			return 1.0;
		}
		double val = hunk.getVal(pos.withY(0.0));
		double h = val * height;
		if(pos.y() < h) {
			return 1.0;
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(HEIGHT, height)
			.set(MIN_Y, minY)
			.set(HUNK, hunk);
	}
	
}
