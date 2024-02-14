package com.ferreusveritas.create;

import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;

public record CreateSchematicBlock(
	Vec3I pos,
	int state
) implements Nbtable {
	
	public static final String POS = "pos";
	public static final String STATE = "state";
	
	@Override
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		
		ListTag<IntTag> posTag = new ListTag<>(IntTag.class);
		posTag.addInt(pos.x());
		posTag.addInt(pos.y());
		posTag.addInt(pos.z());
		
		tag.put(POS, posTag);
		tag.putInt(STATE, state);
		
		return tag;
	}
	
}
