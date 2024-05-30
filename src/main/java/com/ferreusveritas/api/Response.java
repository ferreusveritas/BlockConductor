package com.ferreusveritas.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

import java.util.Optional;

public record Response(
	AABBI area,
	Blocks blocks
) implements Nbtable {
	
	@Override
	public CompoundTag toNBT() {
		CompoundTag main = new CompoundTag();
		main.put("area", (area == null ? AABBI.EMPTY: area).toNBT());
		main.put("blocks", (blocks == null ? new Blocks(Vec3I.ZERO) : blocks).toNBT());
		return main;
	}
	
	@JsonIgnore
	public Optional<Blocks> getBlocks() {
		return Optional.ofNullable(blocks);
	}
	
}
