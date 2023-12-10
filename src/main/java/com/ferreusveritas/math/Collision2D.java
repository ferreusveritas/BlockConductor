package com.ferreusveritas.math;


public class Collision2D {
	
	public static boolean rectIntersectsTriangle(RectD rect, Vec3D[] triangle3D) {
		Vec2D[] triangle = new Vec2D[3];
		for(int i = 0; i < 3; i++) {
			triangle[i] = triangle3D[i].toVec2D();
		}
		return rectIntersectsTriangle(rect, triangle);
	}
	
	public static boolean rectIntersectsTriangle(RectD rect, Vec2D[] triangle) {
		for(Vec2D vertex : triangle) {
			if(rect.contains(vertex)) {
				return true; // Any triangle vertex inside the rect is a collision
			}
		}
		Vec2D[] rectVertices = rect.vertices();
		for(Vec2D corner : rectVertices) {
			if(pointInTriangle(corner, triangle)) {
				return true; // Any rectangle corner inside the triangle is a collision
			}
		}
		for(int i = 0; i < 4; i++) {
			Vec2D a1 = rectVertices[i];
			Vec2D a2 = rectVertices[(i + 1) % 4];
			Line2D line = new Line2D(a1, a2);
			if(lineIntersectsTriangle(line, triangle)) {
				return true; // Any edge intersecting the triangle is a collision
			}
		}
		return false;
	}
	
	public static boolean lineIntersectsTriangle(Line2D lineA, Vec2D[] triangle) {
		for(int i = 0; i < 3; i++) {
			Vec2D b1 = triangle[i];
			Vec2D b2 = triangle[(i + 1) % 3];
			Line2D lineB = new Line2D(b1, b2);
			if(lineIntersectsLine(lineA, lineB)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean lineIntersectsLine(Line2D a, Line2D b) {
		double a1x = a.start().x();
		double a1z = a.start().z();
		double a2x = a.end().x();
		double a2z = a.end().z();
		double b1x = b.start().x();
		double b1z = b.start().z();
		double b2x = b.end().x();
		double b2z = b.end().z();
		double s1x = a2x - a1x;
		double s1z = a2z - a1z;
		double s2x = b2x - b1x;
		double s2z = b2z - b1z;
		double denominator = -s2x * s1z + s1x * s2z;
		if(denominator == 0) {
			return false; // Collinear
		}
		double s = (-s1z * (a1x - b1x) + s1x * (a1z - b1z)) / denominator;
		double t = ( s2x * (a1z - b1z) - s2z * (a1x - b1x)) / denominator;
		return s >= 0 && s <= 1 && t >= 0 && t <= 1;
	}
	
	private static double sign (Vec2D p1, Vec2D p2, Vec2D p3) {
		return (p1.x() - p3.x()) * (p2.z() - p3.z()) - (p2.x() - p3.x()) * (p1.z() - p3.z());
	}
	
	public static boolean pointInTriangle(Vec2D p, Vec2D[] tri) {
		double d1 = sign(p, tri[0], tri[1]);
		double d2 = sign(p, tri[1], tri[2]);
		double d3 = sign(p, tri[2], tri[0]);
		boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
		boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);
		return !(hasNeg && hasPos);
	}
	
	private Collision2D() {
		throw new IllegalStateException("Utility class");
	}
	
}
