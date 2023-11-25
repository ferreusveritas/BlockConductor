package com.ferreusveritas.block;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

/**
 * A block is a 3D object that can be placed in the world.
 * A block is defined by a name and a state.
 * @param name A string that uniquely identifies the block.
 * @param state A string that defines the block's properties.
 */
public record Block(
	String name,
	String state
) implements Jsonable {
	
	public Block(String name) {
		this(name, "");
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("name", name)
			.set("state", state);
	}
	
	@Override
	public String toString() {
		return name + "|" + state;
	}
	
}
