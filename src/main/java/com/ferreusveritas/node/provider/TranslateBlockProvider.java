package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class TranslateBlockProvider extends BlockProvider {
	
	public static final String TYPE = "translate";
	public static final String OFFSET = "offset";
	public static final String PROVIDER = "provider";
	
	private final Vec3I offset;
	private final BlockProvider provider;
	private final AABBI aabb;
	
	private TranslateBlockProvider(UUID uuid, Vec3I offset, BlockProvider provider) {
		super(uuid);
		this.offset = offset;
		this.provider = provider;
		this.aabb = calculateBounds(provider);
	}
	
	private AABBI calculateBounds(BlockProvider provider) {
		return provider.getAABB().offset(offset.neg());
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
		AABBI area = request.area();
		if(!area.intersects(aabb)) {
			return Optional.empty();
		}
		Request newRequest = request.withArea(area.offset(offset.neg()));
		return provider.getBlocks(newRequest);
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OFFSET, offset)
			.set(PROVIDER, provider);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder  {
		
		private UUID uuid = null;
		private Vec3I offset = Vec3I.ZERO;
		private BlockProvider provider = null;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder offset(Vec3I offset) {
			this.offset = offset;
			return this;
		}
		
		public Builder provider(BlockProvider provider) {
			this.provider = provider;
			return this;
		}
		
		public BlockProvider build() {
			if(provider == null) {
				throw new IllegalStateException("provider is null");
			}
			return new TranslateBlockProvider(uuid, offset, provider);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Vec3I offset;
		private final NodeLoader provider;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.offset = src.getObj(OFFSET).map(Vec3I::new).orElseThrow(missing(OFFSET));
			this.provider = loaderSystem.loader(src, PROVIDER);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			BlockProvider b = provider.load(loaderSystem, BlockProvider.class).orElseThrow(wrongType(PROVIDER));
			return new TranslateBlockProvider(getUuid(), offset, b);
		}
		
	}
	
}
