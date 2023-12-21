package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class TranslateShape extends Shape {
	
	public static final String TYPE = "translate";
	public static final String OFFSET = "offset";
	
	private final Shape shape;
	private final Vec3D offset;
	private final AABBD bounds;
	
	private TranslateShape(UUID uuid, Shape shape, Vec3D offset) {
		super(uuid);
		this.shape = shape;
		this.offset = offset;
		this.bounds = calculateBounds(shape);
	}
	
	private AABBD calculateBounds(Shape shape) {
		return shape.bounds().offset(offset);
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
		if(!bounds.contains(pos)) {
			return 0.0;
		}
		return shape.getVal(pos.sub(offset));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(OFFSET, offset);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = null;
		private Vec3D offset = Vec3D.ZERO;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder offset(Vec3D offset) {
			this.offset = offset;
			return this;
		}
		
		public Builder offset(Vec3I offset) {
			return offset(offset.toVecD());
		}
		
		public TranslateShape build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			if(offset == null) {
				throw new IllegalStateException("offset cannot be null");
			}
			return new TranslateShape(uuid, shape, offset);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final Vec3D offset;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.offset = src.getObj(OFFSET).map(Vec3D::new).orElseThrow(missing(OFFSET));
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new TranslateShape(getUuid(), s, offset);
		}
		
	}
	
}
