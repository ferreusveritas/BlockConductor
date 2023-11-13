package com.ferreusveritas.block.provider;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoutingBlockProvider extends BlockProvider {
	
	private final Map<String, BlockProvider> providers;
	private final AABBI aabb;
	
	public RoutingBlockProvider(Map<String, BlockProvider> providers) {
		this.providers = new HashMap<>(providers);
		this.aabb = unionProviders(this.providers.values());
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
		return Optional.of(AABBI.INFINITE);
	}
}
