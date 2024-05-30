package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.BlockNodeValue;

import java.util.Optional;
import java.util.UUID;

/**
 * A BlockProvider that provides a single block type for every position in the world.
 */
public class SolidBlockProvider extends BlockProvider {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("solid")
		.loaderClass(Loader.class)
		.sceneObjectClass(SolidBlockProvider.class)
		.value(new BlockNodeValue.Builder(BLOCK).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final Block block;
	
	private SolidBlockProvider(UUID uuid, Block block) {
		super(uuid);
		this.block = block;
	}
	
	public SolidBlockProvider(Block block) {
		this(null, block);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		Blocks blocks = new Blocks(request.area().size());
		blocks.fill(block);
		return Optional.of(blocks);
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return true;
	}
	
	@Override
	public AABBI getAABB() {
		return AABBI.INFINITE;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final Block block;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(BLOCK) Block block
		) {
			super(uuid);
			this.block = block;
		}
		
		protected BlockProvider create() {
			return new SolidBlockProvider(block);
		}
		
	}
	
}
