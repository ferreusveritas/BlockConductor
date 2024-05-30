package com.ferreusveritas.node.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.node.DataSpan;
import com.ferreusveritas.node.LoaderNode;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.BlockMapNodeValue;
import com.ferreusveritas.node.values.BlockNodeValue;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A BlockMapper that uses a HashMap to map blocks.
 */
public class SimpleBlockMapper extends BlockMapper {
	
	public static final String MAP = "map";
	public static final String DEFAULT_BLOCK = "defaultBlock";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_MAPPER)
		.minorType("simple")
		.loaderClass(Loader.class)
		.sceneObjectClass(SimpleBlockMapper.class)
		.value(new BlockMapNodeValue.Builder(MAP).span(DataSpan.MULTIPLE).build())
		.value(new BlockNodeValue.Builder(DEFAULT_BLOCK).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.MAPPER))
		.build();
	
	private final Map<Block, Block> map;
	private final Block defaultBlock;
	
	private SimpleBlockMapper(UUID uuid, Map<Block, Block> map, Block defaultBlock) {
		super(uuid);
		this.map = Map.copyOf(map);
		this.defaultBlock = defaultBlock == null ? BlockCache.NONE : defaultBlock;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public Block map(Block block) {
		return map.getOrDefault(block, defaultBlock == null ? block : defaultBlock);
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends LoaderNode {
		
		private final List<BlockInOut> map;
		private final Block defaultBlock;
		
		@JsonCreator
		public Loader(
			@JsonProperty("uuid") UUID uuid,
			@JsonProperty(MAP) List<BlockInOut> map,
			@JsonProperty(DEFAULT_BLOCK) Block defaultBlock
		) {
			super(uuid);
			this.map = map;
			this.defaultBlock = defaultBlock;
			createOutputPort(PortDataTypes.MAPPER, this::create);
		}
		
		private BlockMapper create() {
			Map<Block, Block> blockMap = map.stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
			return new SimpleBlockMapper(getUuid(), blockMap, defaultBlock);
		}
		
	}
	
}
