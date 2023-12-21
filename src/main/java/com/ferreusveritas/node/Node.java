package com.ferreusveritas.node;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class Node implements Jsonable {
	
	public static final String CLASS = "class";
	public static final String TYPE = "type";
	public static final String UID = "uuid";
	
	private final UUID uuid;
	
	protected Node(UUID uuid) {
		this.uuid = uuid == null ? UUID.randomUUID() : uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * Get the class of this node
	 *
	 * @return The class of this node
	 */
	public abstract Class<? extends Node> getNodeClass();
	
	/**
	 * Get the subtype of this node
	 *
	 * @return The subtype of this node
	 */
	public abstract String getType();
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set(CLASS, getNodeClass().getSimpleName())
			.set(TYPE, getType())
			.set(UID, uuid.toString());
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
	protected Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
}
