package com.ferreusveritas.node.shape.support;

import com.ferreusveritas.math.AABBD;

public enum BoundsOperation {
	EMPTY((a,b) -> AABBD.EMPTY),
	FIRST((a,b) -> a),
	UNION((a,b) -> AABBD.union(a, b)),
	INTERSECTION((a,b) -> AABBD.intersect(a, b)),
	INFINITE((a,b) -> AABBD.INFINITE);
	
	private final BoundsFunction func;
	
	BoundsOperation(BoundsFunction func) {
		this.func = func;
	}
	
	public AABBD apply(AABBD a, AABBD b) {
		return func.apply(a, b);
	}
	
	@FunctionalInterface
	private interface BoundsFunction {
		AABBD apply(AABBD a, AABBD b);
	}
	
}