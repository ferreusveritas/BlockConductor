package com.ferreusveritas.node.model.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Collision2D;
import com.ferreusveritas.math.RectD;
import com.ferreusveritas.math.Vec3D;

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
		AABBD tempAABB = AABBD.EMPTY;
		for(int i = 0; i < 3; i++) {
			Vec3D vertex = vertices[i];
			AABBD vertAABB = new AABBD(vertex, vertex);
			tempAABB = AABBD.union(tempAABB, vertAABB);
		}
		return tempAABB;
	}
	
	public AABBD getAABB() {
		return aabb;
	}
	
	public boolean rectIntersects(RectD rect) {
		RectD thisAABB = getAABB().toRect();
		return thisAABB.intersects(rect) && Collision2D.rectIntersectsTriangle(rect, vertices);
	}
	
}
