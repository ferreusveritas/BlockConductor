package com.ferreusveritas.node.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.node.LoaderNode;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;

import java.util.UUID;

/**
 * A BlockMapper that does nothing.
 * Blocks in will match blocks out.
 */
public class IdentityBlockMapper extends BlockMapper {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_MAPPER)
		.minorType("identity")
		.loaderClass(Loader.class)
		.sceneObjectClass(IdentityBlockMapper.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.MAPPER))
		.build();
	
	private IdentityBlockMapper(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public Block map(Block block) {
		return block;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends LoaderNode {
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid
		) {
			super(uuid);
			createOutputPort(PortDataTypes.MAPPER, this::create);
		}
		
		private BlockMapper create() {
			return new IdentityBlockMapper(getUuid());
		}
		
	}
	
}
