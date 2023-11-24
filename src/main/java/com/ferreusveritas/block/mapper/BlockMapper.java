package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.UUID;

/**
 * A BlockMapper is used to map one block to another.
 * This is useful for things like mapping a block to a different blockstate.
 */
public abstract class BlockMapper implements Jsonable {
	
	private final Scene scene;
	private final UUID uuid;
	
	protected BlockMapper(Scene scene) {
		this.scene = scene;
		this.uuid = UUID.randomUUID();
	}
	
	protected BlockMapper(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElse(UUID.randomUUID());
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public abstract Block map(Block block);
	
	public abstract String getType();
	
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
