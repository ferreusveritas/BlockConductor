package com.ferreusveritas.resources.model.obj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ObjModel {

	private final Map<String, ObjModelPart> parts;
	
	private ObjModel(Map<String, ObjModelPart> parts) {
		this.parts = Map.copyOf(parts);
	}
	
	public Optional<ObjModelPart> getPart(String name) {
		return Optional.ofNullable(parts.get(name));
	}
	
	public List<String> getPartNames() {
		return List.copyOf(parts.keySet());
	}
	
	public int getPartCount() {
		return parts.size();
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final Map<String, ObjModelPart.Builder> parts = new HashMap<>();
		private ObjModelPart.Builder currentPart;
		
		public Builder() {
			newPart("");
		}
		
		public Builder newPart(String name) {
			this.currentPart = new ObjModelPart.Builder();
			parts.put(name, currentPart);
			return this;
		}
		
		public Builder addFace(FullFace face) {
			this.currentPart.addFace(face);
			return this;
		}
		
		public Builder addFaces(List<FullFace> faces) {
			this.currentPart.addFaces(faces);
			return this;
		}
		
		public ObjModel build() {
			Map<String, ObjModelPart> builtParts = new HashMap<>();
			for (var entry : parts.entrySet()) {
				String partName = entry.getKey();
				ObjModelPart.Builder partBuilder = entry.getValue();
				ObjModelPart part = partBuilder.build();
				if(!part.isEmpty()) {
					builtParts.put(partName, part);
				}
			}
			return new ObjModel(builtParts);
		}
		
	}
	
}
