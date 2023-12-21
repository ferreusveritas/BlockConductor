package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ClipShape extends Shape {
	
	public static final String TYPE = "clip";
	
	private final Shape shape;
	private final AABBD bounds;
	
	public ClipShape(UUID uuid, Shape shape, AABBD bounds) {
		super(uuid);
		this.shape = shape;
		this.bounds = bounds;
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
		if(bounds.contains(pos)) {
			return shape.getVal(pos);
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BOUNDS, bounds)
			.set(SHAPE, shape);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = VoidShape.VOID;
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
		
		public ClipShape build() {
			return new ClipShape(uuid, shape, bounds);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final AABBD bounds;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.bounds = src.getObj(BOUNDS).map(AABBD::fromJson).orElseThrow(missing(BOUNDS));
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new ClipShape(getUuid(), s, bounds);
		}
		
	}
	
}
