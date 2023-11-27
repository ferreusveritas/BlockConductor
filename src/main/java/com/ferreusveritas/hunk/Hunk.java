package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class Hunk implements Jsonable {
	
	public static final String HUNK = "hunk";
	public static final String BOUNDS = "bounds";
	
	private final Scene scene;
	private final UUID uuid;
	
	protected Hunk(Scene scene) {
		this.scene = scene;
		this.uuid = UUID.randomUUID();
	}
	
	protected Hunk(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElse(UUID.randomUUID());
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public abstract String getType();
	
	public abstract AABBD bounds();
	
	public abstract double getVal(Vec3D pos);
	
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
	
	protected Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
}
