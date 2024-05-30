package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.DataSpan;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Combines multiple block providers into a single provider.
 * Each provider is processed in order. Blocks from each provider are overwritten by blocks from later providers.
 */
public class CombineBlockProvider extends BlockProvider {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("combine")
		.loaderClass(Loader.class)
		.sceneObjectClass(CombineBlockProvider.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.BLOCKS, DataSpan.MULTIPLE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final List<BlockProvider> providers;
	private final AABBI aabb;
	
	private CombineBlockProvider(UUID uuid, List<BlockProvider> providers) {
		super(uuid);
		this.providers = providers;
		this.aabb = unionProviders(this.providers);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		if (!this.aabb.intersects(area)) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area.size());
		for (BlockProvider provider : providers) {
			processProvider(provider, request, blocks);
		}
		return Optional.of(blocks);
	}
	
	private void processProvider(BlockProvider provider, Request request, Blocks blocks) {
		AABBI area = request.area();
		if(!provider.intersects(area)) {
			return;
		}
		Optional<Blocks> blocksOptional = provider.getBlocks(request);
		if (blocksOptional.isEmpty()) {
			return;
		}
		Blocks blocksLayer = blocksOptional.get();
		area.forEach((abs, rel) -> processBlock(blocksLayer, rel, blocks));
	}
	
	private void processBlock(Blocks blocksLayer, Vec3I pos, Blocks blocks) {
		Block block = blocksLayer.get(pos);
		if (block != BlockCache.NONE) {
			blocks.set(pos, block);
		}
	}
	
	@Override
	public boolean intersects(AABBI area) {
		if(!this.aabb.intersects(area)) {
			return false;
		}
		for (BlockProvider provider : providers) {
			if (provider.intersects(area)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final List<InputPort<BlockProvider>> providers;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(BLOCKS) List<PortAddress> providerAddresses
		) {
			super(uuid);
			this.providers = createInputsAndRegisterConnections(PortDataTypes.BLOCKS, providerAddresses);
		}
		
		protected BlockProvider create() {
			return new CombineBlockProvider(
				getUuid(),
				providers.stream().map(InputPort::read).toList()
			);
		}
		
	}
	
}
