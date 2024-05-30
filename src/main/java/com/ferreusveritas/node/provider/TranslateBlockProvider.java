package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.Optional;
import java.util.UUID;

public class TranslateBlockProvider extends BlockProvider {
	
	public static final String OFFSET = "offset";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("translate")
		.loaderClass(Loader.class)
		.sceneObjectClass(TranslateBlockProvider.class)
		.value(new VecNodeValue.Builder(OFFSET).increment(1).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.BLOCKS))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final Vec3I offset;
	private final BlockProvider provider;
	private final AABBI aabb;
	
	private TranslateBlockProvider(UUID uuid, Vec3I offset, BlockProvider provider) {
		super(uuid);
		this.offset = offset;
		this.provider = provider;
		this.aabb = calculateBounds(provider);
	}
	
	private AABBI calculateBounds(BlockProvider provider) {
		return provider.getAABB().offset(offset);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	public Vec3I getOffset() {
		return offset;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		if(!area.intersects(aabb)) {
			return Optional.empty();
		}
		Request newRequest = request.withArea(area.offset(offset.neg()));
		return provider.getBlocks(newRequest);
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final Vec3I offset;
		private final InputPort<BlockProvider> providerInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(OFFSET) Vec3I offset,
			@JsonProperty(BLOCKS) PortAddress blocksAddress
		) {
			super(uuid);
			this.offset = offset;
			this.providerInput = createInputAndRegisterConnection(PortDataTypes.BLOCKS, blocksAddress);
		}
		
		protected BlockProvider create() {
			return new TranslateBlockProvider(
				getUuid(),
				offset,
				get(providerInput)
			);
		}
		
	}
	
}
