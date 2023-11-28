package com.ferreusveritas.math;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * An axis-aligned bounding box.
 */
public record AABBI(
	Vec3I min,
	Vec3I max
) implements Jsonable, Nbtable {
	public static final AABBI NONE = new AABBI(Vec3I.ZERO, Vec3I.ZERO);
	public static final AABBI INFINITE = new AABBI(Vec3I.MIN, Vec3I.MAX);
	
	public AABBI(int x1, int y1, int z1, int x2, int y2, int z2) {
		this(new Vec3I(x1, y1, z1), new Vec3I(x2, y2, z2));
	}
	
	public AABBI(RectI rect, int y1, int y2) {
		this(rect.x1(), y1, rect.z1(), rect.x2(), y2, rect.z2());
	}
	
	public AABBI(AABBD aabb) {
		this(
			new Vec3I(
				convMin(aabb.min().x()),
				convMin(aabb.min().y()),
				convMin(aabb.min().z())
			),
			new Vec3I(
				convMax(aabb.max().x()),
				convMax(aabb.max().y()),
				convMax(aabb.max().z())
			)
		);
	}
	
	private static int convMin(double v) {
		return (Double.isInfinite(v) && v < 0) ? Integer.MIN_VALUE : (int)Math.floor(v);
	}
	
	private static int convMax(double v) {
		return (Double.isInfinite(v) && v > 0) ? Integer.MAX_VALUE : (int)Math.ceil(v);
	}
	
	public AABBI(JsonObj src) {
		this(
			src.getObj("min").map(Vec3I::new).orElseThrow(() -> new InvalidJsonProperty("Missing min")),
			src.getObj("max").map(Vec3I::new).orElseThrow(() -> new InvalidJsonProperty("Missing max"))
		);
	}
	
	public AABBD toAABBD() {
		if(this == INFINITE) {
			return AABBD.INFINITE;
		}
		return new AABBD(this);
	}
	
	public RectI toRectI() {
		if(this == INFINITE) {
			return RectI.INFINITE;
		}
		return new RectI(this);
	}
	
	/**
	 * Returns a new AABB moved by the specified amount.
	 * @param pos The amount to move
	 * @return A new AABB moved by the specified amount.
	 */
	public AABBI offset(Vec3I pos) {
		if(this == INFINITE) {
			return this;
		}
		return new AABBI(min.add(pos), max.add(pos));
	}

	/**
	 * Returns the size of this AABB.
	 * @return The size of this AABB.
	 */
	public Vec3I size() {
		if(this == INFINITE) {
			return Vec3I.MAX;
		}
		return max.sub(min).add(new Vec3I(1, 1, 1));
	}

	public int vol() {
		if(this == INFINITE) {
			return Integer.MAX_VALUE;
		}
		return size().vol();
	}

	public boolean badSize() {
		Vec3I size = size();
		return size.x() <= 0 || size.y() <= 0 || size.z() <= 0;
	}

	private static Optional<AABBI> badFilter(AABBI in) {
		return in.badSize() ? Optional.empty() : Optional.of(in);
	}

	/**
	 * Returns whether a point is inside this AABB.
	 * @param pos The point
	 * @return Whether the point is inside this AABB.
	 */
	public boolean isInside(Vec3I pos) {
		return
			pos.x() >= min.x() && pos.x() <= max.x() &&
			pos.y() >= min.y() && pos.y() <= max.y() &&
			pos.z() >= min.z() && pos.z() <= max.z();
	}
	
	/**
	 * Returns whether this AABB intersects another AABB.
	 * @param aabb The other AABB
	 * @return Whether this AABB intersects the other AABB.
	 */
	public boolean intersects(AABBI aabb) {
		return
			aabb != null &&
			min.x() <= aabb.max.x() && max.x() >= aabb.min.x() &&
			min.y() <= aabb.max.y() && max.y() >= aabb.min.y() &&
			min.z() <= aabb.max.z() && max.z() >= aabb.min.z();
	}

	/**
	 * Returns the intersection of this AABB with another AABB.
	 * @param aabb The other AABB
	 * @return The intersection of this AABB with the other AABB, or {@link Optional#empty()} if there is no intersection.
	 */
	public Optional<AABBI> intersect(AABBI aabb) {
		if(!intersects(aabb)) {
			return Optional.empty();
		}
		return Optional.of(new AABBI(min.max(aabb.min), max.min(aabb.max)));
	}

	public Optional<AABBI> expand(int amount) {
		return expand(new Vec3I(amount, amount, amount));
	}

	public Optional<AABBI> expand(Vec3I amount) {
		if(this == INFINITE) {
			return Optional.of(this);
		}
		return badFilter(new AABBI(min.sub(amount), max.add(amount)));
	}

	public Optional<AABBI> shrink(int amount) {
		return expand(-amount);
	}

	public Optional<AABBI> shrink(Vec3I amount) {
		return expand(amount.neg());
	}

	public AABBI union(AABBI aabb) {
		return new AABBI(min.min(aabb.min), max.max(aabb.max));
	}
	
	public static AABBI union(AABBI a, AABBI b) {
		if (a != null && b != null) {
			return a.union(b);
		}
		return a != null ? a : b;
	}
	
	/**
	 * Iterates over all positions in this AABB.
	 * @param func A function that accepts the absolute position and the relative position respectively.
	 */
	public void forEach(BiConsumer<Vec3I, Vec3I> func) {
		Vec3I size = size();
		int maxX = size.x();
		int maxY = size.y();
		int maxZ = size.z();
		for(int y = 0; y < maxY; y++) {
			for(int x = 0; x < maxX; x++) {
				for(int z = 0; z < maxZ; z++) {
					Vec3I rel = new Vec3I(x, y, z);
					func.accept(rel.add(min), rel);
				}
			}
		}
	}
	
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.put("min", min().toNBT());
		tag.put("max", max().toNBT());
		return tag;
	}
	
	public AABBI fromChunk(Vec3I chunkPos) {
		Vec3I blockPos = chunkPos.mul(16);
		Vec3I chunkSize = new Vec3I(16, 16, 16);
		return new AABBI(blockPos, blockPos.add(chunkSize));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("min", min)
			.set("max", max);
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
