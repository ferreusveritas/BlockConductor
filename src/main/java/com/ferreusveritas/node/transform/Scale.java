package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class Scale extends Transform {

	public static final String TYPE = "scale";
	public static final String SIZE = "size";
	
	private final Vec3D size;
	private final Matrix4X4 matrix;
	
	private Scale(UUID uuid, Vec3D size) {
		super(uuid);
		this.size = size;
		this.matrix = Matrix4X4.IDENTITY.scale(size);
	}
	
	@Override
	public Matrix4X4 getData() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SIZE, getSizeJsonObj(size));
	}
	
	private static JsonObj getSizeJsonObj(Vec3D factor) {
		if(factor.x() == factor.y() && factor.y() == factor.z()) {
			return new JsonObj(factor.x());
		}
		return factor.toJsonObj();
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Vec3D size = Vec3D.ONE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder size(double size) {
			this.size = new Vec3D(size, size, size);
			return this;
		}
		
		public Builder size(Vec3D size) {
			this.size = size;
			return this;
		}
		
		public Scale build() {
			return new Scale(uuid, size);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Vec3D size;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			JsonObj sizeObj = src.getObj(SIZE).orElse(JsonObj.emptyVal());
			this.size = getSize(sizeObj);
		}
		
		private static Vec3D getSize(JsonObj src) {
			if(src.isMap()) {
				return new Vec3D(src);
			}
			if(src.isNumber()) {
				double s = src.asDouble().orElse(1.0);
				return new Vec3D(s, s, s);
			}
			return Vec3D.ONE;
		}
		
		@Override
		public Scale load(LoaderSystem loaderSystem) {
			return new Scale(getUuid(), size);
		}
		
	}
	
}
