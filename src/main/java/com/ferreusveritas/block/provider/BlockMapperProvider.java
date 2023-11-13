package com.ferreusveritas.block.provider;

import com.ferreusveritas.math.AABBI;
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
			Block mapped = blockMapper.map(block);
			blocks.set(rel, mapped);
		});
		return Optional.of(mappedBlocks);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return provider.getAABB();
	}
}
