package com.ferreusveritas.api;

import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

public record Response(
	AABBI area,
	Blocks blocks
) implements Jsonable, Nbtable {
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("area", area)
			.set("blocks", blocks);
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
	@Override
	public CompoundTag toNBT() {
		CompoundTag main = new CompoundTag();
		main.put("area", area.toNBT());
		main.put("blocks", blocks.toNBT());
		return main;
	}
	
}
