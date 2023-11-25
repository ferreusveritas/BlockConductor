package com.ferreusveritas.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ReferenceModel extends Model {

	public static final String TYPE = "reference";
	
	private final Model ref;
	
	public ReferenceModel(Scene scene, Model ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceModel(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString("ref").map(UUID::fromString).orElse(null);
		this.ref = scene.getModel(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public boolean pointIsInside(Vec3D point) {
		return ref.pointIsInside(point);
	}
	
	@Override
	public AABBD getAABB() {
		return ref.getAABB();
	}
	
}
