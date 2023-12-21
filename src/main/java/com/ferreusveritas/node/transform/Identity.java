package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class Identity extends Transform {

	public static final String TYPE = "identity";
	public static final UUID IDENTITY_UUID = UUID.fromString("67854e7e-9b7a-4b7a-9e1a-9b7a4b7a9e1a");
	public static final Identity INSTANCE = new Identity(IDENTITY_UUID);
	
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
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
		}
		
		@Override
		public Identity load(LoaderSystem loaderSystem) {
			return INSTANCE;
		}
		
	}
	
}
