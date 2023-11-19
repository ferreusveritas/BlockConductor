package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

public class TranslateBlockProvider extends BlockProvider {
	
	public static final String TYPE = "translate";
	
	private final BlockProvider provider;
	private final Vec3I offset;
	
	@JsonCreator
	public TranslateBlockProvider(
		@JsonProperty("provider") BlockProvider provider,
		@JsonProperty("offset") Vec3I offset
	) {
		this.provider = provider;
		this.offset = offset;
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"provider", provider,
			"offset", offset
		);
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
