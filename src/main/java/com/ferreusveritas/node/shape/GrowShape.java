package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class GrowShape extends Shape {
	
	public static final String TYPE = "grow";
	
	private final Shape shape;
	private final AABBD bounds;
	
	private GrowShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return shape.bounds().expand(1.0);
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
		return
			isInside(pos.down()) ||
			isInside(pos.up()) ||
			isInside(pos.north()) ||
			isInside(pos.south()) ||
			isInside(pos.west()) ||
			isInside(pos.east())
			? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape.toJsonObj());
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
		
		public GrowShape build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			return new GrowShape(uuid, shape);
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
			return new GrowShape(getUuid(), s);
		}
		
	}
	
}
