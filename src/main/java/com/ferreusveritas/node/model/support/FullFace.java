package com.ferreusveritas.node.model.support;

import com.ferreusveritas.math.Vec2D;
import com.ferreusveritas.math.Vec3D;

public class FullFace {

	private final Vec3D[] vertices;
	private final Vec2D[] textureCoords;
	private final Vec3D[] normals;
	
	public FullFace(Vec3D[] vertices, Vec2D[] textureCoords, Vec3D[] normal) {
		this.vertices = new Vec3D[] { vertices[0], vertices[1], vertices[2] };
		this.textureCoords = new Vec2D[] { textureCoords[0], textureCoords[1], textureCoords[2] };
		this.normals = new Vec3D[] { normal[0], normal[1], normal[2] };
	}
	
	public Vec3D getVertex(int index) {
		return vertices[index];
	}
	
	public Vec2D getTextureCoord(int index) {
		return textureCoords[index];
	}
	
	public Vec3D getNormal(int index) {
		return normals[index];
	}
	
	public SimpleFace toSimpleFace() {
		return new SimpleFace(vertices);
	}
	
}
