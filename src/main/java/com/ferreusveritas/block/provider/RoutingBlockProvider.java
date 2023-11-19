package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoutingBlockProvider extends BlockProvider {
	
	public static final String TYPE = "routing";
	
	private final Map<String, BlockProvider> providers;
	private final AABBI aabb;
	
	@JsonCreator
	public RoutingBlockProvider(
		@JsonProperty("providers") Map<String, BlockProvider> providers
	) {
		this.providers = new HashMap<>(providers);
		this.aabb = unionProviders(this.providers.values());
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"providers", providers
		);
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
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
}
