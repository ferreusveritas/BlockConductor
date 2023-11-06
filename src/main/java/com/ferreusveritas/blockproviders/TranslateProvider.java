package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.Blocks;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

public class TranslateProvider extends BlockProvider {
	
	private final BlockProvider provider;
	private final VecI offset;
	
	public TranslateProvider(BlockProvider provider, VecI offset) {
		this.provider = provider;
		this.offset = offset;
	}
	
	public VecI getOffset() {
		return offset;
	}
	
	@Override
	public Optional<Blocks> getBlocks(AABB area) {
		return provider.getBlocks(area.move(offset.neg()));
	}
	
	@Override
	public boolean intersects(AABB aabb) {
		return provider.intersects(aabb.move(offset.neg()));
	}
	
	@Override
	public AABB getAABB() {
		return provider.getAABB().move(offset.neg());
	}
	
}
