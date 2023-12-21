package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.BitSet;
import java.util.UUID;

public class CacheShape extends Shape {

	public static final String TYPE = "cache";
	
	private final Shape shape;
	private final AABBD bounds;
	private final AABBI aabb;
	private final BitSet cache;
	
	private CacheShape(UUID uuid, Shape shape, AABBD bounds) {
		super(uuid);
		this.shape = shape;
		this.bounds = bounds;
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
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////

	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = null;
		private AABBD bounds = AABBD.EMPTY;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder bounds(AABBD bounds) {
			this.bounds = bounds;
			return this;
		}
		
		public CacheShape build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			return new CacheShape(uuid, shape, bounds);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final AABBD bounds;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.bounds = src.getObj(BOUNDS).map(AABBD::fromJson).orElseThrow(missing(BOUNDS));
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new CacheShape(getUuid(), s, bounds);
		}
		
	}
	
}
