package com.ferreusveritas.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.UUID;

public abstract class Model implements Jsonable {
	
	private final Scene scene;
	private final UUID uuid;
	
	protected Model(Scene scene) {
		this.scene = scene;
		this.uuid = java.util.UUID.randomUUID();
	}
	
	protected Model(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElseGet(UUID::randomUUID);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public abstract String getType();
	
	public abstract boolean pointIsInside(Vec3D point);
	
	public abstract AABBD getAABB();
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("type", getType())
			.set("uuid", uuid);
	}
	
}
