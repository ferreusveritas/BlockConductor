package com.ferreusveritas.support.nbt;

import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;

public class NbtHelper {
	
	private static final NBTSerializer serializer = new NBTSerializer();
	
	public static byte[] serialize(Nbtable nbtable) {
		if(nbtable == null) {
			return new byte[0];
		}
		CompoundTag main = nbtable.toNBT();
		return serialize(main);
	}
	
	public static byte[] serialize(CompoundTag tag) {
		NamedTag file = new NamedTag("", tag);
		byte[] bytes;
		try {
			bytes = serializer.toBytes(file);
		} catch (IOException e) {
			throw new NbtException("Failed to serialize NBT", e);
		}
		return bytes;
	}
	
	private NbtHelper() {
		throw new IllegalStateException("Utility class");
	}
	
}
