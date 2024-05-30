package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.DataSpan;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.*;
import java.util.stream.Collectors;

public class RouteBlockProvider extends BlockProvider {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("route")
		.loaderClass(Loader.class)
		.sceneObjectClass(RouteBlockProvider.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.BLOCKS, DataSpan.MAPPED))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final Map<String, BlockProvider> providers;
	private final AABBI aabb;
	
	private RouteBlockProvider(UUID uuid, Map<String, BlockProvider> providers) {
		super(uuid);
		this.providers = new HashMap<>(providers);
		this.aabb = calculateBounds(this.providers.values());
	}
	
	private AABBI calculateBounds(Collection<BlockProvider> values) {
		return unionProviders(values);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		BlockProvider provider = providers.get(request.context());
		if(provider != null) {
			return provider.getBlocks(request);
		}
		return Optional.empty();
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final Map<String, InputPort<BlockProvider>> providerInputs;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(BLOCKS) Map<String, PortAddress> providerAddresses
		) {
			super(uuid);
			this.providerInputs = providerAddresses.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> createInputAndRegisterConnection(e.getKey(), PortDataTypes.BLOCKS, e.getValue())));
		}
		
		protected RouteBlockProvider create() {
			return new RouteBlockProvider(
				getUuid(),
				providerInputs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> get(e.getValue())))
			);
		}
		
	}
	
}
