package com.ferreusveritas.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Collision;
import com.ferreusveritas.math.RectD;
import com.ferreusveritas.math.Vec3D;

public class SimpleFace {
	
	private final Vec3D[] vertices = new Vec3D[3];
	private final Vec3D normal;
	
	public SimpleFace(Vec3D[] vertices, Vec3D normal) {
		this.vertices[0] = vertices[0];
		this.vertices[1] = vertices[1];
		this.vertices[2] = vertices[2];
		this.normal = normal;
	}
	
	public Vec3D getVertex(int index) {
		return vertices[index];
	}
	
	public Vec3D getNormal() {
		return normal;
	}
	
	public AABBD getAABB() {
		Vec3D vertex = vertices[0];
		AABBD aabb = new AABBD(vertex, vertex);
		for(int i = 1; i < 3; i++) {
			vertex = vertices[i];
			AABBD vertAABB = new AABBD(vertex, vertex);
			aabb = aabb.union(vertAABB);
		}
		return aabb;
	}
	
	public boolean rectIntersects(RectD rect) {
		RectD thisAABB = getAABB().toRect();
		return thisAABB.intersects(rect) && Collision.rectIntersectsTriangle(rect, vertices);
	}
	
}
