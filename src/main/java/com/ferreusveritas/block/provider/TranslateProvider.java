package com.ferreusveritas.block.provider;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

public class TranslateProvider extends BlockProvider {
	
	private final BlockProvider provider;
	private final Vec3I offset;
	
	public TranslateProvider(BlockProvider provider, Vec3I offset) {
		this.provider = provider;
		this.offset = offset;
	}
	
	public Vec3I getOffset() {
		return offset;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return provider.getBlocks(request.withArea(request.area().offset(offset.neg())));
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return provider.intersects(area.offset(offset.neg()));
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return provider.getAABB().map(a -> a.offset(offset.neg()));
	}
	
}
