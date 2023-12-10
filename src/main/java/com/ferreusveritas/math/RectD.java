package com.ferreusveritas.math;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.function.Supplier;

public record RectD(
	double x1,
	double z1,
	double x2,
	double z2
) implements Jsonable {
	
	public static final RectD INFINITE = new RectD(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	public static final RectD EMPTY = new RectD(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	
	public RectD(Vec3D size) {
		this(size.x(), size.z());
	}
	
	public RectD(double width, double height) {
		this(0, 0, width, height);
	}
	
	public RectD(AABBD aabb) {
		this(aabb.min().x(), aabb.min().z(), aabb.max().x(), aabb.max().z());
	}
	
	public static RectD fromJson(JsonObj src) {
		if(src == null) {
			throw new InvalidJsonProperty("Null JsonObj");
		}
		if(src.isString()) {
			String str = src.asString().orElseThrow();
			if(str.equalsIgnoreCase("INFINITE")) {
				return INFINITE;
			}
			if (str.equalsIgnoreCase("EMPTY")) {
				return EMPTY;
			}
			throw new InvalidJsonProperty("Invalid RectD String: " + src);
		}
		if(src.isMap()) {
			double x1 = src.getDouble("x1").orElseThrow(missing("x1"));
			double z1 = src.getDouble("z1").orElseThrow(missing("z1"));
			double x2 = src.getDouble("x2").orElseThrow(missing("x2"));
			double z2 = src.getDouble("z2").orElseThrow(missing("z2"));
			return new RectD(x1, z1, x2, z2).resolve();
		}
		if(src.isList()) {
			if(src.size() < 4) {
				throw new InvalidJsonProperty("List must have at least 4 elements: " + src);
			}
			double x1 = src.getObj(0).flatMap(JsonObj::asDouble).orElseThrow(missing("[0](x1)"));
			double z1 = src.getObj(1).flatMap(JsonObj::asDouble).orElseThrow(missing("[1](z1)"));
			double x2 = src.getObj(2).flatMap(JsonObj::asDouble).orElseThrow(missing("[2](x2)"));
			double z2 = src.getObj(3).flatMap(JsonObj::asDouble).orElseThrow(missing("[3](z2)"));
			return new RectD(x1, z1, x2, z2).resolve();
		}
		throw new InvalidJsonProperty("Invalid RectD Json: " + src);
	}
	
	private static Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing or invalid double value: " + name);
	}
	
	public AABBD toAABB() {
		if(this == INFINITE) {
			return AABBD.INFINITE;
		}
		if(this == EMPTY) {
			return AABBD.EMPTY;
		}
		return new AABBD(this).resolve();
	}
	
	public RectD offset(double x, double z) {
		if(this == INFINITE || this == EMPTY) {
			return this;
		}
		return new RectD(x1 + x, z1 + z, x2 + x, z2 + z);
	}
	
	public Vec3D size() {
		if(this == INFINITE) {
			return Vec3D.INFINITY.withY(0);
		}
		if(this == EMPTY) {
			return Vec3D.ZERO;
		}
		return new Vec3D(x2 - x1, 0, z2 - z1);
	}
	
	public boolean contains(double x, double z) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return x >= x1 && x < x2 && z >= z1 && z < z2;
	}
	
	public boolean contains(Vec2D vec) {
		return contains(vec.x(), vec.z());
	}
	
	public boolean intersects(RectD o) {
		if(this == INFINITE || o == INFINITE) {
			return true;
		}
		if(this == EMPTY || o == EMPTY) {
			return false;
		}
		return x1 < o.x2 && x2 > o.x1 && z1 < o.z2 && z2 > o.z1;
	}
	
	public RectD union(RectD o) {
		if(this == INFINITE || o == INFINITE) {
			return INFINITE;
		}
		if(this == EMPTY) {
			return o;
		}
		if(o == EMPTY) {
			return this;
		}
		return new RectD(
			Math.min(x1, o.x1),
			Math.min(z1, o.z1),
			Math.max(x2, o.x2),
			Math.max(z2, o.z2)
		);
	}
	
	public RectD intersect(RectD o) {
		if(this == INFINITE) {
			return o;
		}
		if(o == INFINITE) {
			return this;
		}
		if(this == EMPTY || o == EMPTY) {
			return EMPTY;
		}
		return new RectD(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
	public RectD[] subdivide() {
		if(this == INFINITE) {
			throw new UnsupportedOperationException("Cannot subdivide infinite RectD");
		}
		if(this == EMPTY) {
			return new RectD[] { EMPTY, EMPTY, EMPTY, EMPTY };
		}
		double xMid = (x1 + x2) / 2;
		double zMid = (z1 + z2) / 2;
		return new RectD[] {
			new RectD(x1, z1, xMid, zMid),
			new RectD(xMid, z1, x2, zMid),
			new RectD(x1, zMid, xMid, z2),
			new RectD(xMid, zMid, x2, z2)
		};
	}
	
	public Vec2D[] vertices() {
		if(this == INFINITE) {
			throw new UnsupportedOperationException("Cannot get vertices of infinite RectD");
		}
		if(this == EMPTY) {
			throw new UnsupportedOperationException("Cannot get vertices of empty RectD");
		}
		return new Vec2D[] {
			new Vec2D(x1, z1),
			new Vec2D(x2, z1),
			new Vec2D(x2, z2),
			new Vec2D(x1, z2)
		};
	}
	
	public RectD resolve() {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		if(Double.isInfinite(x1) && Double.isInfinite(z1) && Double.isInfinite(x2) && Double.isInfinite(z2) &&
			(x1 < 0 && z1 < 0 && x2 > 0 && z2 > 0)
		) {
			return INFINITE;
		}
		if(Double.isNaN(x1) || Double.isNaN(z1) || Double.isNaN(x2) || Double.isNaN(z2)) {
			return EMPTY;
		}
		return this;
	}
	
	@Override
	public JsonObj toJsonObj() {
		RectD resolved = resolve();
		if(resolved == INFINITE) {
			return new JsonObj("INFINITE");
		}
		if(resolved == EMPTY) {
			return new JsonObj("EMPTY");
		}
		return JsonObj.newMap()
			.set("x1", MathHelper.dbl(resolved.x1))
			.set("z1", MathHelper.dbl(resolved.z1))
			.set("x2", MathHelper.dbl(resolved.x2))
			.set("z2", MathHelper.dbl(resolved.z2));
	}
	
}
