package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.mapper.BlockMapper;
import com.ferreusveritas.block.mapper.BlockMapperFactory;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class MapperBlockProvider extends BlockProvider {
	
	public static final String TYPE = "mapper";
	
	private final BlockMapper mapper;
	private final BlockProvider provider;
	
	public MapperBlockProvider(Scene scene, BlockMapper mapper, BlockProvider provider) {
		super(scene);
		this.mapper = mapper;
		this.provider = provider;
		if(mapper == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a mapper");
		}
		if(provider == null) {
			throw new IllegalArgumentException("MapperBlockProvider must have a provider");
		}
	}
	
	public MapperBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.mapper = src.getObj("mapper").map(scene::createBlockMapper).orElseThrow(() -> new IllegalArgumentException("Missing mapper"));
		this.provider = src.getObj("provider").map(scene::createBlockProvider).orElseThrow(() -> new IllegalArgumentException("Missing provider"));
	}
	
	@Override
	public String getType() {
		return TYPE;
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("mapper", mapper)
			.set("provider", provider);
	}
	
}
