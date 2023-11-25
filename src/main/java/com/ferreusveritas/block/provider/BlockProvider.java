package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public abstract class BlockProvider implements Jsonable {
	
	private final Scene scene;
	private final UUID uuid;
	
	BlockProvider(Scene scene) {
		this.scene = scene;
		this.uuid = UUID.randomUUID();
	}
	
	BlockProvider(Scene scene, JsonObj src) {
		this.scene = scene;
		this.uuid = src.getString("uuid").map(UUID::fromString).orElse(UUID.randomUUID());
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public abstract String getType();
	
	/**
	 * Get a block array for the given area.
	 * @param request The request.
	 * @return An optional containing the blocks, or empty if the area has no blocks.
	 */
	public abstract Optional<Blocks> getBlocks(Request request);
	
	/**
	 * Check if the given area intersects with this block provider.
	 * @param area The area to check.
	 * @return True if the area intersects with this block provider, false otherwise.
	 */
	public boolean intersects(AABBI area) {
		return getAABB().map(a -> a.intersects(area)).orElse(false);
	}
	
	/**
	 * Get the AABB for this block provider.
	 * @return The AABB for this block provider.
	 */
	public abstract Optional<AABBI> getAABB();
	
	/**
	 * Get the AABB for this block provider, intersected with the given area.
	 * @param area The area to intersect with.
	 * @return The AABB for this block provider, intersected with the given area.
	 */
	protected Optional<AABBI> intersect(AABBI area) {
		return getAABB().flatMap(a -> a.intersect(area));
	}
	
	/**
	 * Get the unioned AABB for the given block providers.
	 * @param providers The block providers to union.
	 * @return The AABB for the given block providers.
	 */
	protected AABBI unionProviders(Collection<BlockProvider> providers) {
		AABBI aabb = null;
		for (BlockProvider provider : providers) {
			aabb = AABBI.union(aabb, provider.getAABB().orElse(null));
		}
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("type", getType())
			.set("uuid", uuid.toString());
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
