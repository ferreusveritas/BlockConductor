package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A Shape that represents a plane at y = 0 with one side being 1.0 and the other 0.0
 */
public class PlaneShape extends Shape {
	
	public static final UUID PLANE_UUID = UUID.fromString("4f3c3b7e-5b7a-4b7a-8c7a-7a7a7a7a7a7a");
	public static final PlaneShape PLANE = new PlaneShape(PLANE_UUID);
	public static final String TYPE = "plane";
	
	private PlaneShape(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.y() < 0 ? 1.0 : 0.0;
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
			return PLANE;
		}
		
	}
	
}
