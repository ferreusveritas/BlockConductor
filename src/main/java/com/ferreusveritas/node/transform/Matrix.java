package com.ferreusveritas.node.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;

import java.util.UUID;

public class Matrix extends Transform {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("matrix")
		.loaderClass(Loader.class)
		.sceneObjectClass(Matrix.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	public static final String DATA = "data";
	
	private final Matrix4X4 data;
	
	private Matrix(UUID uuid, Matrix4X4 matrix) {
		super(uuid);
		this.data = matrix;
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return data;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}

	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends TransformProviderLoaderNode {
		
		private final Matrix4X4 data;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(DATA) Matrix4X4 data
		) {
			super(uuid);
			this.data = data;
		}
		
		protected Matrix create() {
			return new Matrix(getUuid(), data);
		}
		
	}
	
}
