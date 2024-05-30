package com.ferreusveritas.node.shape.support;

public enum ColorChannel {
	
	A(24),
	R(16),
	G(8),
	B(0);
	
	private final int shift;
	
	ColorChannel(int shift) {
		this.shift = shift;
	}
	
	public int getShift() {
		return shift;
	}
	
	public int getColor(int rgb) {
		return (rgb >> shift) & 0xFF;
	}
	
}
