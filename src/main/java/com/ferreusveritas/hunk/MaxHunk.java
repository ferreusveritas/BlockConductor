package com.ferreusveritas.hunk;

import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * A Hunk that returns the maximum value of a list of hunks
 */
public class MaxHunk extends CombineHunk {
	
	public static final String TYPE = "max";
	
	public MaxHunk(Scene scene, List<Hunk> hunks) {
		super(scene, hunks);
	}
	
	public MaxHunk(Scene scene, Hunk... hunks) {
		this(scene, List.of(hunks));
	}
	
	public MaxHunk(Scene scene, JsonObj src) {
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
			accum = Math.max(accum, hunks.get(i).getVal(pos));
		}
		return accum;
	}
	
}
