package com.ferreusveritas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Collision;
import com.ferreusveritas.math.RectD;
import com.ferreusveritas.math.Vec3D;

import java.util.Arrays;

public class SimpleFace {
	
	@JsonProperty
	private final Vec3D[] vertices = new Vec3D[3];
	
	private final AABBD aabb;
	
	public SimpleFace(Vec3D[] vertices) {
		this.vertices[0] = vertices[0];
		this.vertices[1] = vertices[1];
		this.vertices[2] = vertices[2];
		aabb = calculateAABB();
	}
	
	public Vec3D getVertex(int index) {
		return vertices[index];
	}
	
	public void getVertices(Vec3D[] dest) {
		System.arraycopy(vertices, 0, dest, 0, 3);
	}
	
	public Vec3D[] getVertices() {
		Vec3D[] dest = new Vec3D[3];
		getVertices(dest);
		return dest;
	}
	
	private AABBD calculateAABB() {
		Vec3D vertex = vertices[0];
		AABBD aabb = new AABBD(vertex, vertex);
		for(int i = 1; i < 3; i++) {
			vertex = vertices[i];
			AABBD vertAABB = new AABBD(vertex, vertex);
			aabb = aabb.union(vertAABB);
		}
		return aabb;
	}
	
	public AABBD getAABB() {
		return aabb;
	}
	
	public boolean rectIntersects(RectD rect) {
		RectD thisAABB = getAABB().toRect();
		return thisAABB.intersects(rect) && Collision.rectIntersectsTriangle(rect, vertices);
	}
	
}
