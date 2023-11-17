package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.mapper.BlockMapper;

import java.util.Optional;

public class MapperBlockProvider extends BlockProvider {
	
	private final BlockMapper mapper;
	private final BlockProvider provider;
	
	@JsonCreator
	public MapperBlockProvider(
		@JsonProperty("mapper") BlockMapper mapper,
		@JsonProperty("provider") BlockProvider provider
	) {
		this.mapper = mapper;
		this.provider = provider;
		if(mapper == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a mapper");
		}
		if(provider == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a provider");
		}
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area).orElse(null);
		if(bounds == null) {
			return Optional.empty();
		}
		Blocks blocks = provider.getBlocks(request).orElse(null);
		if(blocks == null) {
			return Optional.empty();
		}
		Blocks mappedBlocks = new Blocks(area);
		area.forEach((abs, rel) -> {
			Block block = blocks.get(rel);
			Block mapped = mapper.map(block);
			mappedBlocks.set(rel, mapped);
		});
		return Optional.of(mappedBlocks);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return provider.getAABB();
	}
}
