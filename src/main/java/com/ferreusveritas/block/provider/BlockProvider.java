package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.api.Request;

import java.util.Collection;
import java.util.Optional;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	property = "type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = SolidBlockProvider.class, name = "solid"),
	@JsonSubTypes.Type(value = TranslateBlockProvider.class, name = "translate"),
	@JsonSubTypes.Type(value = CombineBlockProvider.class, name = "combine"),
	@JsonSubTypes.Type(value = ShapeBlockProvider.class, name = "shape"),
	@JsonSubTypes.Type(value = MapperBlockProvider.class, name = "mapper"),
	@JsonSubTypes.Type(value = RoutingBlockProvider.class, name = "routing")
})
public abstract class BlockProvider {
	
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
	
}
