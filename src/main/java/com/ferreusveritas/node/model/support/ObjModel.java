package com.ferreusveritas.node.model.support;

import com.ferreusveritas.node.model.support.FullFace;

import java.util.List;

public class ObjModel {
	
	private List<FullFace> faces;
	
	public ObjModel(List<FullFace> faces) {
		this.faces = faces;
	}
	
	public List<FullFace> getFaces() {
		return faces;
	}
	
	public FullFace getFace(int index) {
		return faces.get(index);
	}
	
	public int getFaceCount() {
		return faces.size();
	}
	
}
