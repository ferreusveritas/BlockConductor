package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * CombineHunk is an abstract Hunk that combines the values of multiple Hunks together.
 */
public abstract class CombineHunk extends Hunk {
	
	protected final List<Hunk> hunks;
	protected final AABBD bounds;
	
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
	
	private AABBD createBounds(List<Hunk> hunks) {
		return hunks.stream().map(Hunk::bounds).reduce((a, b) -> AABBD.union(a, b)).orElse(AABBD.INFINITE);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("hunks", JsonObj.newList(hunks));
	}
	
}
