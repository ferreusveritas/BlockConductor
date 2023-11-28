package com.ferreusveritas.hunk;

import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * A Hunk that returns the product of a list of hunks
 */
public class MultiplyHunk extends CombineHunk {
	
	public static final String TYPE = "multiply";
	
	public MultiplyHunk(Scene scene, List<Hunk> hunks) {
		super(scene, hunks);
	}
	
	public MultiplyHunk(Scene scene, Hunk... hunks) {
		this(scene, List.of(hunks));
	}
	
	public MultiplyHunk(Scene scene, JsonObj src) {
		super(scene, src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		int size = hunks.size();
		double accum = size > 0 ? hunks.get(0).getVal(pos) : 0.0;
		for(int i = 1; i < size; i++) {
			accum *= hunks.get(i).getVal(pos);
		}
		return accum;
	}
	
}
