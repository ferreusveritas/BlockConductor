package com.ferreusveritas.math;

import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * An axis-aligned bounding box.
 */
public record AABBI(
	Vec3I min, // inclusive
	Vec3I max // inclusive
) implements Nbtable {
	
	public static final AABBI EMPTY = new AABBI(Vec3I.MIN, Vec3I.MIN);
	public static final AABBI INFINITE = new AABBI(Vec3I.MIN, Vec3I.MAX);
	public static final String MIN = "min";
	public static final String MAX = "max";
	
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
	
	public AABBI(Vec3I vec) {
		this(vec, vec);
	}
	
	private static int convMin(double v) {
		return (Double.isInfinite(v) && v < 0) ? Integer.MIN_VALUE : (int)Math.floor(v);
	}
	
	private static int convMax(double v) {
		return (Double.isInfinite(v) && v > 0) ? Integer.MAX_VALUE : (int)Math.ceil(v);
	}
	
	public AABBD toAABBD() {
		if(this == INFINITE) {
			return AABBD.INFINITE;
		}
		if(this == EMPTY) {
			return AABBD.EMPTY;
		}
		return new AABBD(this);
	}
	
	public RectI toRectI() {
		if(this == INFINITE) {
			return RectI.INFINITE;
		}
		if(this == EMPTY) {
			return RectI.EMPTY;
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
		if(this == EMPTY) {
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
		if(this == EMPTY) {
			return Vec3I.ZERO;
		}
		return max.sub(min).add(Vec3I.ONE);
	}

	public int vol() {
		if(this == INFINITE) {
			return Integer.MAX_VALUE;
		}
		if(this == EMPTY) {
			return 0;
		}
		return size().vol();
	}

	public boolean badSize() {
		Vec3I size = size();
		return size.x() <= 0 || size.y() <= 0 || size.z() <= 0;
	}

	private static AABBI badFilter(AABBI in) {
		return in.badSize() ? EMPTY : in;
	}

	/**
	 * Returns whether a point is inside this AABB.
	 * @param pos The point
	 * @return Whether the point is inside this AABB.
	 */
	public boolean contains(Vec3I pos) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return
			pos.x() >= min.x() && pos.x() <= max.x() &&
			pos.y() >= min.y() && pos.y() <= max.y() &&
			pos.z() >= min.z() && pos.z() <= max.z();
	}
	
	/**
	 * Returns whether another AABB is inside this AABB.
	 * @param aabb The other AABB
	 * @return Whether the other AABB is inside this AABB.
	 */
	public boolean contains(AABBI aabb) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return
			aabb.min.x() >= min.x() && aabb.max.x() <= max.x() &&
			aabb.min.y() >= min.y() && aabb.max.y() <= max.y() &&
			aabb.min.z() >= min.z() && aabb.max.z() <= max.z();
	}
	
	/**
	 * Returns whether this AABB intersects another AABB.
	 * @param aabb The other AABB
	 * @return Whether this AABB intersects the other AABB.
	 */
	public boolean intersects(AABBI aabb) {
		if(this == INFINITE || aabb == INFINITE) {
			return true;
		}
		if(this == EMPTY || aabb == EMPTY) {
			return false;
		}
		return
			min.x() <= aabb.max.x() && max.x() >= aabb.min.x() &&
			min.y() <= aabb.max.y() && max.y() >= aabb.min.y() &&
			min.z() <= aabb.max.z() && max.z() >= aabb.min.z();
	}

	/**
	 * Returns the intersection of this AABB with another AABB.
	 * @param aabb The other AABB
	 * @return The intersection of this AABB with the other AABB, or {@link Optional#empty()} if there is no intersection.
	 */
	public AABBI intersect(AABBI aabb) {
		if(this == INFINITE) {
			return aabb;
		}
		if(aabb == INFINITE) {
			return this;
		}
		if(this == EMPTY || aabb == EMPTY) {
			return EMPTY;
		}
		if(!intersects(aabb)) {
			return EMPTY;
		}
		return new AABBI(min.max(aabb.min), max.min(aabb.max));
	}

	public AABBI expand(int amount) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		return expand(new Vec3I(amount, amount, amount));
	}

	public AABBI expand(Vec3I amount) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		return badFilter(new AABBI(min.sub(amount), max.add(amount)));
	}

	public AABBI shrink(int amount) {
		return expand(-amount);
	}

	public AABBI shrink(Vec3I amount) {
		return expand(amount.neg());
	}
	
	public AABBI union(AABBI aabb) {
		if(this == INFINITE || aabb == INFINITE) {
			return INFINITE;
		}
		if(this == EMPTY) {
			return aabb;
		}
		if(aabb == EMPTY) {
			return this;
		}
		return new AABBI(min.min(aabb.min), max.max(aabb.max));
	}
	
	public static AABBI union(AABBI a, AABBI b) {
		if (a != EMPTY && b != EMPTY) {
			return a.union(b);
		}
		return a != EMPTY ? a : b;
	}
	
	public AABBI union(Vec3I pos) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return new AABBI(pos, pos);
		}
		return new AABBI(min.min(pos), max.max(pos));
	}
	
	/**
	 * Iterates over all positions in this AABB.
	 * @param func A function that accepts the absolute position and the relative position respectively.
	 */
	public void forEach(BiConsumer<Vec3I, Vec3I> func) {
		if(this == INFINITE) {
			throw new UnsupportedOperationException("Cannot iterate over infinite AABB");
		}
		if(this == EMPTY) {
			return;
		}
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
	
	public AABBI resolve() {
		Vec3I rMin = min.resolve();
		Vec3I rMax = max.resolve();
		if(rMin == Vec3I.MIN && rMax == Vec3I.MAX) {
			return INFINITE;
		}
		if(rMin == Vec3I.MIN && rMax == Vec3I.MIN) {
			return EMPTY;
		}
		return badFilter(this);
	}
	
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.put(MIN, min().toNBT());
		tag.put(MAX, max().toNBT());
		return tag;
	}

}
