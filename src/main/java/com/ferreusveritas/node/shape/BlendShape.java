package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * BlendShape is a type of Shape that blends the values of two shapes together using a blend shape as a mask.
 */
public class BlendShape extends Shape {
	
	public static final String TYPE = "blend";
	public static final String SHAPE_1 = "shape1";
	public static final String SHAPE_2 = "shape2";
	public static final String BLEND = "blend";
	
	private final Shape shape1;
	private final Shape shape2;
	private final Shape blend;
	private final AABBD bounds;
	
	private BlendShape(UUID uuid, Shape shape1, Shape shape2, Shape blend) {
		super(uuid);
		this.shape1 = shape1;
		this.shape2 = shape2;
		this.blend = blend;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return AABBD.union(shape1.bounds(), shape2.bounds());
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
		if(!bounds.contains(pos)){
			return 0.0;
		}
		double blendVal = blend.getVal(pos);
		if(blendVal < 0.0){
			return shape1.getVal(pos);
		}
		if(blendVal > 1.0){
			return shape2.getVal(pos);
		}
		double val1 = shape1.getVal(pos);
		double val2 = shape2.getVal(pos);
		return MathHelper.lerp(blendVal, val1, val2);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE_1, shape1)
			.set(SHAPE_2, shape2)
			.set(BLEND, blend);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape1 = VoidShape.VOID;
		private Shape shape2 = VoidShape.VOID;
		private Shape blend = VoidShape.VOID;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape1(Shape shape1) {
			this.shape1 = shape1;
			return this;
		}
		
		public Builder shape2(Shape shape2) {
			this.shape2 = shape2;
			return this;
		}
		
		public Builder blend(Shape blend) {
			this.blend = blend;
			return this;
		}
		
		public BlendShape build() {
			return new BlendShape(uuid, shape1, shape2, blend);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape1;
		private final NodeLoader shape2;
		private final NodeLoader blend;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape1 = loaderSystem.loader(src, SHAPE_1);
			this.shape2 = loaderSystem.loader(src, SHAPE_2);
			this.blend = loaderSystem.loader(src, BLEND);
		}
		
		@Override
		public Node load(LoaderSystem loaderSystem) {
			Shape s1 = shape1.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE_1));
			Shape s2 = shape2.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE_2));
			Shape b = blend.load(loaderSystem, Shape.class).orElseThrow(wrongType(BLEND));
			return new BlendShape(getUuid(), s1, s2, b);
		}
		
	}
	
}
