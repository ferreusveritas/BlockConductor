package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

public record BlockInOut(
	Block in,
	Block out
) implements Jsonable {
	
	public BlockInOut(Scene scene, JsonObj src) {
		this(
			src.getObj("in").map(scene::block).orElseThrow(() -> new InvalidJsonProperty("in missing")),
			src.getObj("out").map(scene::block).orElseThrow(() -> new InvalidJsonProperty("out missing"))
		);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("in", in)
			.set("out", out);
	}
	
}
