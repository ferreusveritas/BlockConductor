package com.ferreusveritas.create;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockProperty;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

import java.util.Map;

public record CreateSchematicPaletteEntry(
	String name,
	Map<String, BlockProperty> properties
) implements Nbtable {
	
	public static final String NAME = "Name";
	public static final String PROPERTIES = "Properties";
	
	public CreateSchematicPaletteEntry(String name, Map<String, BlockProperty> properties) {
		this.name = name;
		this.properties = Map.copyOf(properties);
	}
	
	public CreateSchematicPaletteEntry(Block block) {
		this(block.name(), Map.copyOf(block.properties()));
	}
	
	@Override
	public CompoundTag toNBT() {
		CompoundTag propertiesTag = new CompoundTag();
		if(!properties.isEmpty()) {
			properties.forEach((k, v) -> v.addToNBT(propertiesTag, k));
		}
		
		CompoundTag tag = new CompoundTag();
		tag.putString(NAME, name);
		if(propertiesTag.size() > 0) {
			tag.put(PROPERTIES, propertiesTag);
		}
		return tag;
	}
	
}
