package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class VoidShape extends Shape {
	
	public static final UUID VOID_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static final VoidShape VOID = new VoidShape(VOID_UUID);
	
	public static final String TYPE = "void";
	
	private VoidShape(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.EMPTY;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return 0.0;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return VOID;
		}
		
	}
	
}
