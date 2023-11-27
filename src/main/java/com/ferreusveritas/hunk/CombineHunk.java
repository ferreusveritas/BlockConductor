package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * CombineHunk is an abstract Hunk that combines the values of multiple Hunks together.
 */
public abstract class CombineHunk extends Hunk {
	
	protected final List<Hunk> hunks;
	protected final AABBI bounds;
	
	protected CombineHunk(Scene scene, List<Hunk> hunks) {
		super(scene);
		this.hunks = hunks;
		this.bounds = createBounds(hunks);
	}
	
	protected CombineHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunks = src.getList("hunks").toImmutableList(scene::createHunk);
		this.bounds = createBounds(hunks);
	}
	
	private AABBI createBounds(List<Hunk> hunks) {
		return hunks.stream().map(Hunk::bounds).reduce((a, b) -> AABBI.union(a, b)).orElse(AABBI.INFINITE);
	}
	
	@Override
	public AABBI bounds() {
		return bounds;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("hunks", JsonObj.newList(hunks));
	}
	
}
