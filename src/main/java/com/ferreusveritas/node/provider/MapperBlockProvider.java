package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.misc.MiscHelper;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.mapper.BlockMapper;

import java.util.Optional;
import java.util.UUID;

import static com.ferreusveritas.node.mapper.BlockMapper.MAPPER;

public class MapperBlockProvider extends BlockProvider {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("mapper")
		.loaderClass(Loader.class)
		.sceneObjectClass(MapperBlockProvider.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.MAPPER))
		.port(new PortDescription(PortDirection.IN, PortDataTypes.BLOCKS))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final BlockMapper mapper;
	private final BlockProvider provider;
	private final AABBI aabb;
	
	private MapperBlockProvider(UUID uuid, BlockMapper mapper, BlockProvider blocks) {
		super(uuid);
		this.mapper = mapper;
		this.provider = blocks;
		this.aabb = calculateBounds(blocks);
		validate();
	}
	
	private void validate() {
		MiscHelper.require(mapper, MAPPER);
		MiscHelper.require(provider, BLOCKS);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBI calculateBounds(BlockProvider provider) {
		return provider.getAABB();
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area);
		if(bounds == AABBI.EMPTY) {
			return Optional.empty();
		}
		Blocks blocks = this.provider.getBlocks(request).orElse(null);
		if(blocks == null) {
			return Optional.empty();
		}
		Blocks mappedBlocks = new Blocks(area.size());
		area.forEach((abs, rel) -> {
			Block block = blocks.get(rel);
			Block mapped = mapper.map(block);
			mappedBlocks.set(rel, mapped);
		});
		return Optional.of(mappedBlocks);
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final InputPort<BlockMapper> mapperInput;
		private final InputPort<BlockProvider> blocksInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MAPPER) PortAddress mapperAddress,
			@JsonProperty(BLOCKS) PortAddress providerAddress
		) {
			super(uuid);
			this.mapperInput = createInputAndRegisterConnection(PortDataTypes.MAPPER, mapperAddress);
			this.blocksInput = createInputAndRegisterConnection(PortDataTypes.BLOCKS, providerAddress);
		}
		
		protected BlockProvider create() {
			return new MapperBlockProvider(
				getUuid(),
				get(mapperInput),
				get(blocksInput)
			);
		}
		
	}
	
}
