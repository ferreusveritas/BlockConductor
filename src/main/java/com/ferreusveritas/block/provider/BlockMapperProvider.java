package com.ferreusveritas.block.provider;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.mapper.BlockMapper;

import java.util.Optional;

public class BlockMapperProvider extends BlockProvider {
	
	private final BlockMapper blockMapper;
	private final BlockProvider provider;
	
	public BlockMapperProvider(BlockMapper blockMapper, BlockProvider provider) {
		this.blockMapper = blockMapper;
		this.provider = provider;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABB area = request.area();
		AABB bounds = intersect(area).orElse(null);
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
			Block mapped = blockMapper.map(block);
			blocks.set(rel, mapped);
		});
		return Optional.of(mappedBlocks);
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return provider.getAABB();
	}
}
