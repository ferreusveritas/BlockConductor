package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.storage.Storage;

import java.util.Optional;
import java.util.UUID;

public class ResourceBlockProvider extends BlockProvider {
	
	public static final String TYPE = "resource";
	public static final String RES = "res";
	
	private final String res;
	private final BlockProvider provider;
	private final AABBI aabb;
	
	private ResourceBlockProvider(UUID uuid, String res) {
		super(uuid);
		this.res = res;
		this.provider = loadProvider(res);
		this.aabb = calculateBounds(provider);
	}
	
	private BlockProvider loadProvider(String resource) {
		JsonObj src = Storage.getJson(resource);
		Scene scene = new Scene(src);
		return scene.getRoot(BlockProvider.class).orElseThrow();
	}
	
	private AABBI calculateBounds(BlockProvider provider) {
		return provider.getAABB();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return provider.getBlocks(request);
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(RES, res);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid;
		private String res;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder res(String res) {
			this.res = res;
			return this;
		}
		
		public BlockProvider build() {
			return new ResourceBlockProvider(uuid, res);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final String res;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.res = src.getString(RES).orElseThrow(missing(RES));
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			return new ResourceBlockProvider(getUuid(), res);
		}
		
	}
	
}
