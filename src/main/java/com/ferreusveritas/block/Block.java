package com.ferreusveritas.block;

import java.util.Map;
import java.util.Objects;

/**
 * A block is a 3D object that can be placed in the world.
 * A block is defined by a name and it's properties.
 * @param name A string that uniquely identifies the block type.
 * @param properties A map of properties that define the state of the block.
 */
public record Block(
	String name,
	Map<String, BlockProperty> properties
) {
	
	public Block {
		Objects.requireNonNull(name);
		properties = properties == null ? Map.of() : Map.copyOf(properties);
	}
	
	public Block(String name) {
		this(name, Map.of());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, properties);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Block block) {
			return name.equals(block.name) && properties.equals(block.properties);
		}
		return false;
	}
	
}
