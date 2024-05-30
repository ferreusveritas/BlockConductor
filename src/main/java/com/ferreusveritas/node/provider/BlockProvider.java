package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.SceneObject;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public abstract class BlockProvider extends SceneObject {
	
	public static final String BLOCK = "block";
	public static final String BLOCKS = "blocks";
	public static final String BLOCK_PROVIDER = "blockProvider";
	
	BlockProvider(UUID uuid) {
		super(uuid);
	}
	
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
		return getAABB().intersects(area);
	}
	
	/**
	 * Get the AABB for this block provider.
	 * @return The AABB for this block provider. Never null.
	 */
	public abstract AABBI getAABB();
	
	/**
	 * Get the AABB for this block provider, intersected with the given area.
	 * @param area The area to intersect with.
	 * @return The AABB for this block provider, intersected with the given area.
	 */
	protected AABBI intersect(AABBI area) {
		return getAABB().intersect(area);
	}
	
	/**
	 * Get the unioned AABB for the given block providers.
	 * @param providers The block providers to union.
	 * @return The AABB for the given block providers.
	 */
	protected AABBI unionProviders(Collection<BlockProvider> providers) {
		AABBI aabb = AABBI.EMPTY;
		for (BlockProvider provider : providers) {
			aabb = AABBI.union(aabb, provider.getAABB());
		}
		return aabb;
	}
	
}
