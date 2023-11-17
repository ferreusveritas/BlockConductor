package com.ferreusveritas.math;

public class Collision3D {
	
	private static final double CLOSENESS = 0.00001;
	private static final double SMALL_NUM = 0.00000001; // anything that avoids division overflow
	
	public static boolean lineInTriangle(Line3D line, Vec3D[] tri) {
		
		// triangle vectors
		// get triangle edge vectors and plane normal
		Vec3D u = tri[1].sub(tri[0]);
		Vec3D v = tri[2].sub(tri[0]);
		Vec3D n = u.cross(v);
		
		if(n.isZero()) {    //triangle is degenerate
			return false;
		}
		
		Vec3D dir = line.end().sub(line.start());// ray direction vector
		Vec3D w0 = line.start().sub(tri[0]);
		double a = -n.dot(w0);
		double b = n.dot(dir);
		
		if (Math.abs(b) < SMALL_NUM) {	// ray is  parallel to triangle plane
			// if a == 0  // ray lies in triangle plane
			return false; // ray disjoint from plane
		}
		
		// get intersect point of ray with triangle plane
		double r = a / b;
		if (r < 0.0) { // ray goes away from triangle
			return false; // no intersect
		}
		if (r > 1.0) { // for a segment.
			return false; // no intersect
		}
		
		Vec3D i = line.start().add(dir.mul(r)); // intersect point of ray and plane
		
		// is i inside tri?
		double uu = u.dot(u);
		double uv = u.dot(v);
		double vv = v.dot(v);
		
		Vec3D w = i.sub(tri[0]);
		
		double wu = w.dot(u);
		double wv = w.dot(v);
		double d = uv * uv - uu * vv;
		
		// get and test parametric coords
		double s = (uv * wv - vv * wu) / d;
		if (s < -CLOSENESS || s > (1 + CLOSENESS)) {
			return false;
		}
		double t = (uv * wu - uu * wv) / d;
		return (t >= -CLOSENESS) && ((s + t) <= (1 + CLOSENESS));
	}
	
	private Collision3D() {
		throw new IllegalStateException("Utility class");
	}
	
}
