package com.ferreusveritas.support.nbt;

import net.querz.nbt.tag.CompoundTag;

/**
 * Interface for objects that can be serialized to NBT
 */
public interface Nbtable {
	CompoundTag toNBT();
}
