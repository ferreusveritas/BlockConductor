package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class CubeShape extends Shape {
	
	public static final String TYPE = "cube";
	public static final String SIZE = "size";
	public static final String CENTERED = "centered";
	
	private final Vec3D size;
	private final boolean centered;
	private final AABBD aabb;
	
	private CubeShape(UUID uuid, Vec3D size, boolean centered) {
		super(uuid);
		this.size = size;
		this.centered = centered;
		this.aabb = createBounds(size, centered);
	}
	
	private static AABBD createBounds(Vec3D size, boolean centered) {
		Vec3D min = centered ? size.div(2).neg() : Vec3D.ZERO;
		Vec3D max = min.add(size);
		return new AABBD(min, max);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return aabb;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return aabb.contains(pos) ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SIZE, size)
			.set(CENTERED, centered);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Vec3D size = Vec3D.ONE;
		private boolean centered = false;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder size(Vec3D size) {
			this.size = size;
			return this;
		}
		
		public Builder centered(boolean centered) {
			this.centered = centered;
			return this;
		}
		
		public CubeShape build() {
			if(size == null) {
				throw new IllegalStateException("size must not be null");
			}
			return new CubeShape(uuid, size, centered);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Vec3D size;
		private final boolean centered;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.size = src.getObj(SIZE).map(Vec3D::new).orElse(Vec3D.ONE);
			this.centered = src.getBoolean(CENTERED).orElse(false);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new CubeShape(getUuid(), size, centered);
		}
		
	}
	
}
