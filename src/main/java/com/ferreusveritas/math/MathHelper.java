package com.ferreusveritas.math;

public class MathHelper {
	
	/**
	 *  x mod y behaving the same way as Math.floorMod but with doubles
	 */
	public static double floatMod(double x, double y){
		return (x - Math.floor(x/y) * y);
	}
	
	public static double wrap(double val, double min, double max) {
		return floatMod(val - min, max - min) + min;
	}
	
	public static int clamp(int v, int min, int max) {
		return Math.max(Math.min(v, max), min);
	}
	
	public static double clamp(double v, double min, double max) {
		return Math.max(Math.min(v, max), min);
	}
	
	public static double lerp(double v, double a, double b) {
		return a + v * (b - a);
	}
	
	public static double remap(double value, double low1, double high1, double low2, double high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}
	
	private MathHelper() {
		throw new IllegalStateException("Utility class");
	}
	
}