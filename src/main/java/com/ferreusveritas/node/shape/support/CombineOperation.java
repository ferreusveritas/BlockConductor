package com.ferreusveritas.node.shape.support;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

public enum CombineOperation implements Jsonable {
	OR((a,b) -> d(d(a) || d(b)), BoundsOperation.UNION),
	AND((a,b) -> d(d(a) && d(b)), BoundsOperation.INTERSECTION),
	XOR((a,b) -> d(d(a) ^ d(b)), BoundsOperation.UNION),
	NOT((a,b) -> d(!d(a)), BoundsOperation.INFINITE),
	NOR((a,b) -> d(!d(a) && !d(b)), BoundsOperation.INFINITE),
	DIF((a,b) -> d(d(a) && !d(b)), BoundsOperation.UNION),
	ADD(Double::sum, BoundsOperation.UNION),
	SUB((a,b) -> a - b, BoundsOperation.UNION),
	MUL((a,b) -> a * b, BoundsOperation.UNION),
	DIV((a,b) -> a / b, BoundsOperation.UNION),
	MIN(Math::min, BoundsOperation.UNION),
	MAX(Math::max, BoundsOperation.UNION),
	INV((a,b) -> 1.0 - a, BoundsOperation.INFINITE),
	NEG((a,b) -> -a, BoundsOperation.INFINITE),
	THR((a,b) -> a >= b ? 1.0 : 0.0, BoundsOperation.UNION),
	PWR(Math::pow, BoundsOperation.UNION),
	ABS((a,b) -> Math.abs(a), BoundsOperation.INFINITE);
	
	private static final Map<String, CombineOperation> map = EnumSet.allOf(CombineOperation.class).stream().collect(CaseInsensitiveKeyMap::new, (m, v) -> m.put(v.name(), v), Map::putAll);
	private final DoubleFunction func;
	private final BoundsOperation boundsCombineType;
	
	CombineOperation(DoubleFunction func, BoundsOperation boundsCombineType) {
		this.func = func;
		this.boundsCombineType = boundsCombineType;
	}
	
	public double apply(double a, double b) {
		return func.apply(a, b) >= 0.5 ? 1.0 : 0.0;
	}
	
	public AABBD apply(AABBD a, AABBD b) {
		return boundsCombineType.apply(a, b);
	}
	
	public static Optional<CombineOperation> of(String str) {
		return Optional.ofNullable(map.get(str));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return new JsonObj(name());
	}
	
	private interface DoubleFunction {
		double apply(double a, double b);
	}
	
	private static boolean d(double val) {
		return val >= 0.5;
	}
	
	private static double d(boolean val) {
		return val ? 1.0 : 0.0;
	}
	
}
