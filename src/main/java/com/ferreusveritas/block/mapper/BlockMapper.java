package com.ferreusveritas.block.mapper;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ferreusveritas.block.Block;

/**
 * A BlockMapper is used to map one block to another.
 * This is useful for things like mapping a block to a different blockstate.
 */
@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	property = "type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = IdentityBlockMapper.class, name = "identity"),
	@JsonSubTypes.Type(value = SimpleBlockMapper.class, name = "simple"),
})
public interface BlockMapper {
	Block map(Block block);
	
}
