package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class TranslateBlockProvider extends BlockProvider {
	
	public static final String TYPE = "translate";
	public static final String OFFSET = "offset";
	public static final String PROVIDER = "provider";
	
	private final Vec3I offset;
	private final BlockProvider provider;
	
	public TranslateBlockProvider(Scene scene, Vec3I offset, BlockProvider provider) {
		super(scene);
		this.offset = offset;
		this.provider = provider;
	}
	
	public TranslateBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.offset = src.getObj(OFFSET).map(Vec3I::new).orElseThrow(missing(OFFSET));
		this.provider = src.getObj(PROVIDER).map(scene::createBlockProvider).orElseThrow(missing(PROVIDER));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public Vec3I getOffset() {
		return offset;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		Request newRequest = request.withArea(request.area().offset(offset.neg()));
		return provider.getBlocks(newRequest);
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return provider.intersects(area.offset(offset.neg()));
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return provider.getAABB().map(a -> a.offset(offset.neg()));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OFFSET, offset)
			.set(PROVIDER, provider);
	}
	
}
