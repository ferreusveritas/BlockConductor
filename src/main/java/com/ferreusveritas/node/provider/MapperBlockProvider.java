package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.node.mapper.BlockMapper;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class MapperBlockProvider extends BlockProvider {
	
	public static final String TYPE = "mapper";
	public static final String MAPPER = "mapper";
	public static final String PROVIDER = "provider";
	
	private final BlockMapper mapper;
	private final BlockProvider provider;
	private final AABBI aabb;
	
	private MapperBlockProvider(UUID uuid, BlockMapper mapper, BlockProvider provider) {
		super(uuid);
		this.mapper = mapper;
		this.provider = provider;
		if(mapper == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a mapper");
		}
		if(provider == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a provider");
		}
		this.aabb = calculateBounds(provider);
	}
	
	private AABBI calculateBounds(BlockProvider provider) {
		return provider.getAABB();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area);
		if(bounds == AABBI.EMPTY) {
			return Optional.empty();
		}
		Blocks blocks = provider.getBlocks(request).orElse(null);
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MAPPER, mapper)
			.set(PROVIDER, provider);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder  {
		
		private UUID uuid;
		private BlockMapper mapper;
		private BlockProvider provider;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder mapper(BlockMapper mapper) {
			this.mapper = mapper;
			return this;
		}
		
		public Builder provider(BlockProvider provider) {
			this.provider = provider;
			return this;
		}
		
		public BlockProvider build() {
			return new MapperBlockProvider(uuid, mapper, provider);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader mapper;
		private final NodeLoader provider;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.mapper = loaderSystem.loader(src, MAPPER);
			this.provider = loaderSystem.loader(src, PROVIDER);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			BlockMapper m = mapper.load(loaderSystem, BlockMapper.class).orElseThrow(wrongType(MAPPER));
			BlockProvider b = provider.load(loaderSystem, BlockProvider.class).orElseThrow(wrongType(PROVIDER));
			return new MapperBlockProvider(getUuid(), m, b);
		}
		
	}
	
}
