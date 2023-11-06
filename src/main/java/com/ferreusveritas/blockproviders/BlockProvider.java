package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.Blocks;

import java.util.Optional;

public abstract class BlockProvider {
	
	/**
	 * Get a block array for the given area.
	 * @param area The area to get blocks for.
	 * @return An optional containing the blocks, or empty if the area has no blocks.
	 */
	public Optional<Blocks> getBlocks(AABB area) {
		if (!intersects(area)) {
			return Optional.empty();
		}
		return getBlocks(area);
	}
	
	/**
	 * Check if the given area intersects with this block provider.
	 * @param aabb The area to check.
	 * @return True if the area intersects with this block provider, false otherwise.
	 */
	public boolean intersects(AABB aabb) {
		return getAABB().intersects(aabb);
	}
	
	/**
	 * Get the AABB for this block provider.
	 * @return The AABB for this block provider.
	 */
	public abstract AABB getAABB();
	
}
