package com.ferreusveritas.support.nbt;

import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class NbtHelper {
	
	private static final NBTSerializer serializer = new NBTSerializer(false);
	
	public static byte[] serialize(Nbtable nbtable) {
		if(nbtable == null) {
			return new byte[0];
		}
		CompoundTag main = nbtable.toNBT();
		return serialize(main);
	}
	
	public static byte[] serialize(CompoundTag tag) {
		return serialize(tag, true);
	}
	
	public static byte[] serialize(CompoundTag tag, boolean compressed) {
		NamedTag file = new NamedTag("", tag);
		byte[] bytes;
		try {
			bytes = serializer.toBytes(file);
		} catch (IOException e) {
			throw new NbtException("Failed to serialize NBT", e);
		}
		if(compressed) {
			bytes = gzip(bytes);
		}
		return bytes;
	}
	
	private static byte[] gzip(byte[] dataToCompress) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length);
			try (byteStream) {
				try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream)) {
					zipStream.write(dataToCompress);
				}
			}
			return byteStream.toByteArray();
		}
		catch(Exception e) {
			throw new NbtException("Failed to gzip serialized NBT", e);
		}
	}
	
	private NbtHelper() {
		throw new IllegalStateException("Utility class");
	}
	
}
