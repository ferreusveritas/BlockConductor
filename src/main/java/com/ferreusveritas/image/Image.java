package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.UUID;

public abstract class Image implements Jsonable {
	
	private final Scene scene;
	private final UUID uuid;
	
	protected Image(Scene scene) {
		this.scene = scene;
		this.uuid = UUID.randomUUID();
	}
	
	protected Image(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElse(UUID.randomUUID());
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public abstract String getType();
	
	public abstract RectI bounds();
	
	public abstract double getVal(int x, int y);
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("type", getType())
			.set("uuid", uuid.toString());
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
