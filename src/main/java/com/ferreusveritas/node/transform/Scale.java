package com.ferreusveritas.node.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.UUID;

public class Scale extends Transform {
	
	public static final String SIZE = "size";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("scale")
		.loaderClass(Loader.class)
		.sceneObjectClass(Scale.class)
		.value(new VecNodeValue.Builder(SIZE).def(Vec3D.ONE).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	
	private final Matrix4X4 matrix;
	
	private Scale(UUID uuid, Vec3D size) {
		super(uuid);
		this.matrix = Matrix4X4.IDENTITY.scale(size);
	}
	
	@Override
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
		
		private final Vec3D size;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SIZE) Vec3D size
		) {
			super(uuid);
			this.size = size;
		}
		
		protected Scale create() {
			return new Scale(getUuid(), size);
		}
		
	}
	
}
