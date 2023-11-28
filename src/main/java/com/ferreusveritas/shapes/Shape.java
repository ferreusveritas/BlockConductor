package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class Shape implements Jsonable {
	
	public static final String SHAPE = "shape";
	public static final String SHAPES = "shapes";
	public static final String AABB = "aabb";
	
	private final Scene scene;
	private final UUID uuid;
	
	protected Shape(Scene scene) {
		this.scene = scene;
		this.uuid = java.util.UUID.randomUUID();
	}
	
	protected Shape(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElseGet(UUID::randomUUID);
	}
	
	/**
	 * Get the type of shape
	 * @return the type of shape
	 */
	public abstract String getType();
	
	/**
	 * Get the unique identifier of the shape
	 * @return the unique identifier of the shape
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * Get the bounding box of the shape
	 * @return the bounding box of the shape. Empty if the shape has no bounding box
	 */
	public abstract Optional<AABBI> getAABB();
	
	/**
	 * Check if the shape intersects at the given position
	 * @param pos the area to check
	 * @return true if the shape intersects
	 */
	public abstract boolean isInside(Vec3I pos);
	
	protected Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("type", getType())
			.set("uuid", uuid);
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
