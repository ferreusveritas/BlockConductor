package com.ferreusveritas.model;

import com.ferreusveritas.math.AABBD;

import java.util.List;
import java.util.Optional;

public class SimpleMeshModel {

	private final List<SimpleFace> faces;
	private final AABBD aabb;
	
	public SimpleMeshModel(List<SimpleFace> faces) {
		if(faces.isEmpty()) {
			throw new IllegalArgumentException("Model must have at least one face");
		}
		this.faces = faces;
		this.aabb = calculateAABB().orElseThrow(() -> new IllegalArgumentException("SimpleMeshModel AABB is null"));
	}
	
	public SimpleFace getFace(int index) {
		return faces.get(index);
	}
	
	public List<SimpleFace> getFaces() {
		return faces;
	}
	
	public int getFaceCount() {
		return faces.size();
	}
	
	public AABBD getAABB() {
		return aabb;
	}
	
	private Optional<AABBD> calculateAABB() {
		if(getFaceCount() == 0) {
			return Optional.empty();
		}
		AABBD box = getFace(0).getAABB();
		for(int i = 1; i < getFaceCount(); i++) {
			AABBD faceAABB = getFace(i).getAABB();
			box = box.union(faceAABB);
		}
		return Optional.of(box);
	}
	
	public QSP calculateQSP() {
		return QSP.load(this);
	}
	
}
