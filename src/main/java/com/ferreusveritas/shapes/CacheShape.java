package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.BitSet;
import java.util.Optional;

/**
 * A shape that caches the results of isInside() in a BitSet.
 * This is useful for shapes that are expensive to calculate.
 */
public class CacheShape implements Shape {
	
	private final Shape shape;
	private final AABB aabb;
	private final BitSet cache;
	
	public CacheShape(Shape shape, AABB aabb) {
		this.shape = shape;
		this.aabb = determineAAABB(shape, aabb);
		this.cache = buildCache(this.aabb);
	}
	
	private AABB determineAAABB(Shape shape, AABB aabb) {
		return shape.getAABB().flatMap(a -> a.intersect(aabb)).or(shape::getAABB).orElse(null);
	}
	
	private BitSet buildCache(AABB aabb) {
		if(aabb == null) {
			throw new IllegalArgumentException("CacheShape must have a valid AABB");
		}
		BitSet bitSet = new BitSet(aabb.vol());
		int index = 0;
		for (int y = aabb.min().y(); y <= aabb.max().y(); y++) {
			for (int z = aabb.min().z(); z <= aabb.max().z(); z++) {
				for (int x = aabb.min().x(); x <= aabb.max().x(); x++) {
					bitSet.set(index++, shape.isInside(new VecI(x, y, z)));
				}
			}
		}
		return bitSet;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return shape.getAABB().flatMap(a -> a.intersect(this.aabb));
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return cache.get(pos.calcIndex(aabb.size()));
	}
	
}
