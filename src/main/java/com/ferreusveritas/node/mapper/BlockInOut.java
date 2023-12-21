package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

public record BlockInOut(
	Block in,
	Block out
) implements Jsonable {
	
	public static final String IN = "in";
	public static final String OUT = "out";
	
	public BlockInOut(LoaderSystem loaderSystem, JsonObj src) {
		this(
			src.getObj(IN).map(loaderSystem::blockLoader).orElseThrow(() -> new InvalidJsonProperty("in missing")),
			src.getObj(OUT).map(loaderSystem::blockLoader).orElseThrow(() -> new InvalidJsonProperty("out missing"))
		);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set(IN, in)
			.set(OUT, out);
	}
	
}
