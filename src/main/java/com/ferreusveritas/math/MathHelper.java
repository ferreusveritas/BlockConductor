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
	
	/**
	 * Remap a value from one range to another
	 * @param value value to remap
	 * @param low1 low end of original range
	 * @param high1 high end of original range
	 * @param low2 low end of new range
	 * @param high2 high end of new range
	 * @return remapped value
	 */
	public static double remap(double value, double low1, double high1, double low2, double high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}
	
	/**
	 * Linear Interpolation
	 * @param v 0.0 - 1.0
	 * @param a
	 * @param b
	 * @return interpolated value
	 */
	public static double lerp(double v, double a, double b) {
		return a + v * (b - a);
	}
	
	/**
	 * Cubic Hermite Interpolation
	 * @param n0
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param a 0.0 - 1.0
	 * @return interpolated value
	 */
	public static double cerp(final double n0, final double n1, final double n2, final double n3, final double a) {
		double p = n3 - n2 - (n0 - n1);
		double q = n0 - n1 - p;
		double r = n2 - n0;
		return p * a * a * a + q * a * a + r * a + n1;
	}
	
	public static Object dbl(Double d) {
		return Double.isInfinite(d) || Double.isNaN(d) ? Double.toString(d) : d;
	}
	
	private MathHelper() {
		throw new IllegalStateException("Utility class");
	}
	
}