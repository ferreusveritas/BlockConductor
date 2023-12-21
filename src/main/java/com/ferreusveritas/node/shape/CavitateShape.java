package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class CavitateShape extends Shape {
	
	public static final String TYPE = "cavitate";
	
	private final Shape shape;
	private final AABBD bounds;
	
	private CavitateShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return shape.bounds();
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
		return bounds.contains(pos) && inside(pos) ? 1.0 : 0.0;
	}
	
	private boolean inside(Vec3D pos) {
		return inShape(pos) &&
			!(
				inShape(pos.down()) &&
				inShape(pos.up()) &&
				inShape(pos.north()) &&
				inShape(pos.south()) &&
				inShape(pos.west()) &&
				inShape(pos.east())
			);
	}
	
	private boolean inShape(Vec3D pos) {
		return shape.getVal(pos) >= 0.5;
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
		private Shape shape = null;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public CavitateShape build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			return new CavitateShape(uuid, shape);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.shape = loaderSystem.loader(src, SHAPE);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new CavitateShape(getUuid(), s);
		}
		
	}
	
}
