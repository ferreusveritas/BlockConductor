package com.ferreusveritas.shape.support;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.Optional;

public enum ColorChannel implements Jsonable {
	
	A(24),
	R(16),
	G(8),
	B(0);
	
	private final String name;
	private final int shift;
	
	ColorChannel(int shift) {
		this.name = name().toLowerCase();
		this.shift = shift;
	}
	
	public int getShift() {
		return shift;
	}
	
	public int getColor(int rgb) {
		return (rgb >> shift) & 0xFF;
	}
	
	public static Optional<ColorChannel> of(String name) {
		return switch (name.toLowerCase()) {
			case "a" -> Optional.of(A);
			case "r" -> Optional.of(R);
			case "g" -> Optional.of(G);
			case "b" -> Optional.of(B);
			default -> Optional.empty();
		};
	}
	
	@Override
	public JsonObj toJsonObj() {
		return new JsonObj(name);
	}
	
}
