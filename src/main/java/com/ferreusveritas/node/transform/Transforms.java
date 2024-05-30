package com.ferreusveritas.node.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.*;
import com.ferreusveritas.node.ports.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Transforms extends Transform {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("transforms")
		.loaderClass(Loader.class)
		.sceneObjectClass(Transforms.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.TRANSFORM, DataSpan.MULTIPLE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	
	private final Matrix4X4 matrix;
	
	public Transforms(UUID uuid, List<Transform> operations) {
		super(uuid);
		this.matrix = createMatrix(operations);
	}
	
	private Matrix4X4 createMatrix(List<Transform> transforms) {
		Matrix4X4 mat = Matrix4X4.IDENTITY;
		for(Transform transform : transforms) {
			mat = transform.getMatrix().mul(mat);
		}
		return mat;
	}
	
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends TransformProviderLoaderNode {
		
		private final List<InputPort<Transform>> operations;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(TRANSFORM) List<PortAddress> operations
		) {
			super(uuid);
			this.operations = createInputsAndRegisterConnections(PortDataTypes.TRANSFORM, operations);
		}
		
		protected Transforms create() {
			List<Transform> transforms = operations.stream()
				.map(InputPort::readOpt)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
			return new Transforms(getUuid(), transforms);
		}
		
	}
	
}
