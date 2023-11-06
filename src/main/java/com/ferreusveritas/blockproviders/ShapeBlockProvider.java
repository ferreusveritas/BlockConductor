package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.Block;
import com.ferreusveritas.api.Blocks;
import com.ferreusveritas.api.VecI;
import com.ferreusveritas.shapeproviders.ShapeProvider;

import java.util.Optional;

public class ShapeBlockProvider extends BlockProvider {
	
	private final ShapeProvider shapeProvider;
	private final Block block;

	public ShapeBlockProvider(ShapeProvider shapeProvider, Block block) {
		this.shapeProvider = shapeProvider;
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}
	
	@Override
	public Optional<Blocks> getBlocks(AABB area) {
		if(!intersects(area)) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area);
		AABB bounds = getAABB().intersect(area).orElseThrow();
		bounds.forEach((abs,rel) -> processBlock(blocks, abs, abs.sub(area.min())));
		return Optional.of(blocks);
	}
	
	private void processBlock(Blocks blocks, VecI absPos, VecI relPos) {
		if(shapeProvider.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public AABB getAABB() {
		return shapeProvider.getAABB();
	}
	
}
