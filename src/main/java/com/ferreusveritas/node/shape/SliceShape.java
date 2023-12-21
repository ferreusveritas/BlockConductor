package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A Shape that slices another Shape along Y = 0 and infinitely extrudes the result in the Y direction.
 */
public class SliceShape extends Shape {

	public static final String TYPE = "slice";
	
	private final Shape shape;
	private final AABBD bounds;
	
	private SliceShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds(shape);
	}
	
	private AABBD calculateBounds(Shape shape) {
		AABBD aabb = shape.bounds();
		return new AABBD(
			aabb.min().withY(Double.NEGATIVE_INFINITY),
			aabb.max().withY(Double.POSITIVE_INFINITY)
		);
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
		return shape.getVal(pos.withY(0));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = VoidShape.VOID;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public SliceShape build() {
			return new SliceShape(uuid, shape);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape = loaderSystem.loader(src, SHAPE);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new SliceShape(getUuid(), s);
		}
		
	}
	
}
