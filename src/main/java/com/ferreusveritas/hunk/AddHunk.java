package com.ferreusveritas.hunk;

import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * AddHunk is a type of CombineHunk that adds the values of all the Hunks together.
 */
public class AddHunk extends CombineHunk {
	
	public static final String TYPE = "add";
	
	public AddHunk(Scene scene, List<Hunk> hunks) {
		super(scene, hunks);
	}
	
	public AddHunk(Scene scene, Hunk... hunks) {
		this(scene, List.of(hunks));
	}
	
	public AddHunk(Scene scene, JsonObj src) {
		super(scene, src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		double accum = 0;
		for(Hunk hunk : hunks) {
			accum += hunk.getVal(pos);
		}
		return accum;
	}
	
}
