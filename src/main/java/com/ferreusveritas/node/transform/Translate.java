package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class Translate extends Transform {
	
	public static final String TYPE = "translate";
	
	private final Vec3D offset;
	private final Matrix4X4 matrix;
	
	private Translate(UUID uuid, Vec3D offset) {
		super(uuid);
		this.offset = offset;
		this.matrix = Matrix4X4.IDENTITY.translate(offset);
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("offset", offset);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Vec3D offset = Vec3D.ZERO;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder offset(Vec3D offset) {
			this.offset = offset;
			return this;
		}
		
		public Translate build() {
			return new Translate(uuid, offset);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Vec3D offset;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.offset = src.getObj("offset").map(Vec3D::new).orElse(Vec3D.ZERO);
		}
		
		@Override
		public Translate load(LoaderSystem loaderSystem) {
			return new Translate(getUuid(), offset);
		}
		
	}
	
}
