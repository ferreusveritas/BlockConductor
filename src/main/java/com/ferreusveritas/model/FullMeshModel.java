package com.ferreusveritas.model;

import java.util.List;

public class FullMeshModel {
	
	private final List<FullFace> faces;
	
	public FullMeshModel(List<FullFace> faces) {
		this.faces = faces;
	}
	
	public FullFace getFace(int index) {
		return faces.get(index);
	}
	
	public int getFaceCount() {
		return faces.size();
	}
	
	public SimpleMeshModel toSimpleMeshModel() {
		List<SimpleFace> simpleFaces = faces.stream().map(FullFace::toSimpleFace).toList();
		return new SimpleMeshModel(simpleFaces);
	}
	
}
