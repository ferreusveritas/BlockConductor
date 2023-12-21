package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

/**
 * A BlockProvider that provides a single block type for every position in the world.
 */
public class SolidBlockProvider extends BlockProvider {
	
	public static final String TYPE = "solid";
	public static final String BLOCK = "block";
	
	private final Block block;
	
	private SolidBlockProvider(UUID uuid, Block block) {
		super(uuid);
		this.block = block;
	}
	
	public SolidBlockProvider(Block block) {
		this(null, block);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		Blocks blocks = new Blocks(request.area().size());
		blocks.fill(block);
		return Optional.of(blocks);
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return true;
	}
	
	@Override
	public AABBI getAABB() {
		return AABBI.INFINITE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BLOCK, block);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Block block = null;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder block(Block block) {
			this.block = block;
			return this;
		}
		
		public SolidBlockProvider build() {
			if(block == null) {
				throw new IllegalStateException("SolidBlockProvider requires a block");
			}
			return new SolidBlockProvider(uuid, block);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Block block;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.block = loaderSystem.blockLoader(src, BLOCK);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			return new SolidBlockProvider(getUuid(), block);
		}
		
	}
	
}
