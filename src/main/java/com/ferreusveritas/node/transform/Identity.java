package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class Identity extends Transform {

	public static final String TYPE = "identity";
	
	private Identity(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return Matrix4X4.IDENTITY;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Identity build() {
			return new Identity(uuid);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
		}
		
		@Override
		public Identity load(LoaderSystem loaderSystem) {
			return new Identity(getUuid());
		}
		
	}
	
}
