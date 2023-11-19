package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.BitSet;
import java.util.Map;
import java.util.Optional;

/**
 * A shape that caches the results of isInside() in a BitSet.
 * This is useful for shapes that are expensive to calculate.
 */
public class CacheShape implements Shape {
	
	public static final String TYPE = "cache";
	
	private final Shape shape;
	private final AABBI aabb;
	private final BitSet cache;
	
	@JsonCreator
	public CacheShape(
		@JsonProperty("shape") Shape shape,
		@JsonProperty("aabb") AABBI aabb
	) {
		this.shape = shape;
		this.aabb = determineAAABB(shape, aabb);
		this.cache = buildCache(this.aabb);
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"shape", shape,
			"aabb", aabb
		);
	}
	
	private AABBI determineAAABB(Shape shape, AABBI aabb) {
		return shape.getAABB().flatMap(a -> a.intersect(aabb)).or(shape::getAABB).orElse(null);
	}
	
	private BitSet buildCache(AABBI aabb) {
		if(aabb == null) {
			throw new IllegalArgumentException("CacheShape must have a valid AABB");
		}
		BitSet bitSet = new BitSet(aabb.vol());
		int index = 0;
		for (int y = aabb.min().y(); y <= aabb.max().y(); y++) {
			for (int z = aabb.min().z(); z <= aabb.max().z(); z++) {
				for (int x = aabb.min().x(); x <= aabb.max().x(); x++) {
					bitSet.set(index++, shape.isInside(new Vec3I(x, y, z)));
				}
			}
		}
		return bitSet;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB().flatMap(a -> a.intersect(this.aabb));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return cache.get(pos.calcIndex(aabb.size()));
	}
	
}
