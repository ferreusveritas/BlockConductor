package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.node.transform.Identity;
import com.ferreusveritas.node.transform.Transform;

import java.util.UUID;

public class TransformShape extends Shape {
	
	public static final String TYPE = "transform";
	public static final String TRANSFORM = "transform";
	
	private final Shape shape;
	private final Transform transform;
	private final Matrix4X4 matrix;
	private final AABBD bounds;
	
	private TransformShape(UUID uuid, Shape shape, Transform transform) {
		super(uuid);
		this.shape = shape;
		this.transform = transform;
		this.matrix = transform.getMatrix().invert();
		this.bounds = calculateBounds(shape);
	}
	
	private AABBD calculateBounds(Shape shape) {
		return shape.bounds().transform(transform.getMatrix());
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
		Vec3D transPos = matrix.transform(pos);
		if(shape.bounds().contains(transPos)) {
			return shape.getVal(transPos);
		}
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(TRANSFORM, transform)
			.set(SHAPE, shape);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = VoidShape.VOID;
		private Transform transform = Identity.INSTANCE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder transform(Transform transform) {
			this.transform = transform;
			return this;
		}
		
		public TransformShape build() {
			return new TransformShape(uuid, shape, transform);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final NodeLoader transform;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.transform = loaderSystem.loader(src, TRANSFORM);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			Transform t = this.transform.load(loaderSystem, Transform.class).orElseThrow(wrongType(TRANSFORM));
			return new TransformShape(getUuid(), s, t);
		}
		
	}
	
}
