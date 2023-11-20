package com.ferreusveritas.math;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.Optional;

public enum Axis implements Jsonable {
	X,
	Y,
	Z;
	
	public static Optional<Axis> of(String name) {
		return Optional.ofNullable(name).map(String::toUpperCase).map(Axis::valueOf);
	}
	
	public JsonObj toJsonObj() {
		return new JsonObj(name().toLowerCase());
	}
	
}
