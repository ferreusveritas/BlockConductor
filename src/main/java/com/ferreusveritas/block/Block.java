package com.ferreusveritas.block;

/**
 * A block is a 3D object that can be placed in the world.
 * A block is defined by a name and a state.
 * @param name A string that uniquely identifies the block.
 * @param state A string that defines the block's properties.
 */
public record Block(
	String name,
	String state
) {
	
	@Override
	public String toString() {
		return name + "|" + state;
	}
	
}
