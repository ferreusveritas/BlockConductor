package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.BitSet;

public class CacheShape extends Shape {

	public static final String TYPE = "cache";
	
	private final Shape shape;
	private final AABBD bounds;
	private final AABBI aabb;
	private final BitSet cache;
	
	public CacheShape(Scene scene, Shape shape, AABBD bounds) {
		super(scene);
		this.shape = shape;
		this.bounds = bounds;
		this.aabb = bounds.toAABBI();
		this.cache = buildCache(this.aabb);
	}
	
	public CacheShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.bounds = src.getObj(BOUNDS).map(AABBD::fromJson).orElseThrow(missing(BOUNDS));
		this.aabb = bounds.toAABBI();
		this.cache = buildCache(this.aabb);
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
					bitSet.set(index++, shape.getVal(new Vec3I(x, y, z).toVecD()) >= 0.5);
				}
			}
		}
		return bitSet;
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
		return bounds.contains(pos) && inside(pos.toVecI()) ? 1.0 : 0.0;
	}
	
	private boolean inside(Vec3I pos) {
		return cache.get(pos.calcIndex(aabb.size()));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(BOUNDS, bounds);
	}
	
}
