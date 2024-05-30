package com.ferreusveritas.resources.model.obj;

import java.util.ArrayList;
import java.util.List;

public class ObjModelPart {
	
	private final List<FullFace> faces;
	
	private ObjModelPart(List<FullFace> faces) {
		this.faces = List.copyOf(faces);
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
	
	public boolean isEmpty() {
		return faces.isEmpty();
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final List<FullFace> faces = new ArrayList<>();
		
		public Builder addFace(FullFace face) {
			faces.add(face);
			return this;
		}
		
		public Builder addFaces(List<FullFace> faces) {
			this.faces.addAll(faces);
			return this;
		}
		
		public ObjModelPart build() {
			return new ObjModelPart(faces);
		}
		
	}
	
}
