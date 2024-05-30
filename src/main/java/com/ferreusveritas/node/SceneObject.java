package com.ferreusveritas.node;

import com.ferreusveritas.support.exceptions.InvalidJsonProperty;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class SceneObject {
	
	public static final String UID = "uuid";
	
	private final UUID uuid;
	
	protected SceneObject(UUID uuid) {
		this.uuid = uuid == null ? UUID.randomUUID() : uuid;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public abstract NodeRegistryData getRegistryData();
	
	public final String getType() {
		return getRegistryData().type();
	}
	
	protected static Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
}
