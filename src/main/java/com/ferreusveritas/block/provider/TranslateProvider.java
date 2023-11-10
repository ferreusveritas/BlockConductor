package com.ferreusveritas.block.provider;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.math.VecI;

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
	public Optional<Blocks> getBlocks(Request request) {
		return provider.getBlocks(request.withArea(request.area().offset(offset.neg())));
	}
	
	@Override
	public boolean intersects(AABB area) {
		return provider.intersects(area.offset(offset.neg()));
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return provider.getAABB().map(a -> a.offset(offset.neg()));
	}
	
}
