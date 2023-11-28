package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * A shape that represents a layer of blocks in the y-axis.
 */
public class LayerShape extends Shape {
	
	public static final String TYPE = "layer";
	public static final String MIN = "min";
	public static final String MAX = "max";
	
	private final int min;
	private final int max;
	
	public LayerShape(Scene scene, int min, int max) {
		super(scene);
		this.min = min;
		this.max = max;
	}
	
	public LayerShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.min = src.getInt(MIN).orElseThrow(missing(MIN));
		this.max = src.getInt(MAX).orElseThrow(missing(MAX));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(new AABBI(Integer.MIN_VALUE, min, Integer.MIN_VALUE, Integer.MAX_VALUE, max, Integer.MAX_VALUE));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return pos.y() >= min && pos.y() <= max;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MIN, min)
			.set(MAX, max);
	}
	
}
