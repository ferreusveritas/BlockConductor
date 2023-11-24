package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class TranslateBlockProvider extends BlockProvider {
	
	public static final String TYPE = "translate";
	
	private final BlockProvider provider;
	private final Vec3I offset;
	
	public TranslateBlockProvider(Scene scene, BlockProvider provider, Vec3I offset) {
		super(scene);
		this.provider = provider;
		this.offset = offset;
	}
	
	public TranslateBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.provider = src.getObj("provider").map(scene::createBlockProvider).orElseThrow(() -> new InvalidJsonProperty("Missing provider"));
		this.offset = src.getObj("offset").map(Vec3I::new).orElseThrow(() -> new InvalidJsonProperty("Missing offset"));
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("provider", provider)
			.set("offset", offset);
	}
	
}
