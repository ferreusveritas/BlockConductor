package com.ferreusveritas.create;

import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;

public class CreateSchematic implements Nbtable {
	
	public static final int DATA_VERSION = 2975;
	private final List<CreateSchematicBlock> blocks;
	private final List<CreateSchematicPaletteEntry> palette;
	private final Vec3I size;
	
	public CreateSchematic(List<CreateSchematicBlock> blocks, List<CreateSchematicPaletteEntry> palette, Vec3I size) {
		this.blocks = blocks;
		this.palette = palette;
		this.size = size;
	}
	
	@Override
	public CompoundTag toNBT() {
		ListTag<CompoundTag> blocksTag = toNBTList(blocks);
		ListTag<CompoundTag> entitiesTag = new ListTag<>(CompoundTag.class);
		ListTag<CompoundTag> paletteTag = toNBTList(palette);
		ListTag<IntTag> sizeTag = vec3iToNBT(size);
		
		CompoundTag tag = new CompoundTag();
		tag.putInt("DataVersion", DATA_VERSION);
		tag.put("blocks", blocksTag);
		tag.put("entities", entitiesTag);
		tag.put("palette", paletteTag);
		tag.put("size", sizeTag);
		
		return tag;
	}
	
	private ListTag<CompoundTag> toNBTList(List<? extends Nbtable> list) {
		ListTag<CompoundTag> tag = new ListTag<>(CompoundTag.class);
		tag.addAll(list.stream().map(Nbtable::toNBT).toList());
		return tag;
	}
	
	private ListTag<IntTag> vec3iToNBT(Vec3I vec) {
		ListTag<IntTag> posTag = new ListTag<>(IntTag.class);
		posTag.addInt(vec.x());
		posTag.addInt(vec.y());
		posTag.addInt(vec.z());
		return posTag;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final List<CreateSchematicBlock> blocks = new ArrayList<>();
		private final List<CreateSchematicPaletteEntry> paletteEntries = new ArrayList<>();
		private Vec3I size = new Vec3I(0, 0, 0);
		
		public Builder addBlock(CreateSchematicBlock block) {
			blocks.add(block);
			return this;
		}
		
		public Builder addPaletteEntry(CreateSchematicPaletteEntry entry) {
			paletteEntries.add(entry);
			return this;
		}
		
		public Builder size(Vec3I size) {
			this.size = size;
			return this;
		}
		
		public CreateSchematic build() {
			return new CreateSchematic(blocks, paletteEntries, size);
		}
		
	}
	
}
