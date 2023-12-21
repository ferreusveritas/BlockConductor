package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transforms extends Transform {
	
	public static final String TYPE = "transforms";
	public static final String OPERATIONS = "operations";
	
	private final List<Transform> operations;
	private final Matrix4X4 matrix;
	
	public Transforms(UUID uuid, List<Transform> operations) {
		super(uuid);
		this.operations = List.copyOf(operations);
		this.matrix = createMatrix(operations);
	}
	
	private Matrix4X4 createMatrix(List<Transform> transforms) {
		Matrix4X4 mat = Matrix4X4.IDENTITY;
		for(Transform transform : transforms) {
			mat = transform.getMatrix().mul(mat);
		}
		return mat;
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OPERATIONS, operations);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		List<Transform> operations = new ArrayList<>();
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder add(List<Transform> transforms) {
			operations.addAll(transforms);
			return this;
		}
		
		public Builder add(Transform ... transforms) {
			return add(List.of(transforms));
		}
		
		public Transforms build() {
			return new Transforms(uuid, operations);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final List<NodeLoader> operations;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.operations = src.getList(OPERATIONS).toImmutableList(loaderSystem::createLoader);
		}
		
		@Override
		public Transforms load(LoaderSystem loaderSystem) {
			List<Transform> operationList = operations.stream().map(l -> l.load(loaderSystem, Transform.class).orElseThrow()).toList();
			return new Transforms(getUuid(), operationList);
		}
		
	}
	
}
