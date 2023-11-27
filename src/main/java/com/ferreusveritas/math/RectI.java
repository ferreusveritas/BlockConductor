package com.ferreusveritas.math;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

public record RectI(
	int x1, //inclusive
	int z1, //inclusive
	int x2, //inclusive
	int z2  //inclusive
) implements Jsonable {
	
	public static final RectI INFINITE = new RectI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public RectI(Vec3I size) {
		this(size.x(), size.z());
	}
	
	public RectI(int width, int height) {
		this(0, 0, width - 1, height - 1);
	}
	
	public RectI(AABBI aabb) {
		this(
			aabb.min().x(),
			aabb.min().z(),
			aabb.max().x(),
			aabb.max().z()
		);
	}
	
	public RectI(JsonObj src) {
		this(
			src.getInt("x1").orElse(Integer.MIN_VALUE),
			src.getInt("z1").orElse(Integer.MIN_VALUE),
			src.getInt("x2").orElse(Integer.MAX_VALUE),
			src.getInt("z2").orElse(Integer.MAX_VALUE)
		);
	}
	
	public boolean isInside(Vec3I vec) {
		return isInside(vec.x(), vec.y());
	}
	
	public boolean isInside(int x, int z) {
		return x >= x1 && x <= x2 && z >= z1 && z <= z2;
	}
	
	public int width() {
		if(x1 == Integer.MIN_VALUE || x2 == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return x2 - x1 + 1;
	}
	
	public int height() {
		if(z1 == Integer.MIN_VALUE || z2 == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return z2 - z1 + 1;
	}
	
	public RectI offset(int x, int z) {
		int x1n = x1;
		int z1n = z1;
		int x2n = x2;
		int z2n = z2;
		if(x1n != Integer.MIN_VALUE || x1n != Integer.MAX_VALUE) {
			x1n += x;
		}
		if(z1n != Integer.MIN_VALUE || z1n != Integer.MAX_VALUE) {
			z1n += z;
		}
		if(x2n != Integer.MIN_VALUE || x2n != Integer.MAX_VALUE) {
			x2n += x;
		}
		if(z2n != Integer.MIN_VALUE || z2n != Integer.MAX_VALUE) {
			z2n += z;
		}
		return new RectI(x1n, z1n, x2n, z2n);
	}
	
	public RectI intersect(RectI o) {
		return new RectI(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
	public RectI union(RectI o) {
		return new RectI(
			Math.min(x1, o.x1),
			Math.min(z1, o.z1),
			Math.max(x2, o.x2),
			Math.max(z2, o.z2)
		);
	}
	
	public static RectI union(RectI a, RectI b) {
		if(a == null) {
			return b;
		}
		if(b == null) {
			return a;
		}
		return a.union(b);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("x1", x1)
			.set("z1", z1)
			.set("x2", x2)
			.set("z2", z2);
	}
	
}
