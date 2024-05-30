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

public class Translate extends Transform {
	
	public static final String OFFSET = "offset";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("translate")
		.loaderClass(Loader.class)
		.sceneObjectClass(Translate.class)
		.value(new VecNodeValue.Builder(OFFSET).def(Vec3D.ZERO).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	
	private final Matrix4X4 matrix;
	
	private Translate(UUID uuid, Vec3D offset) {
		super(uuid);
		this.matrix = Matrix4X4.IDENTITY.translate(offset);
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
		
		private final Vec3D offset;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(OFFSET) Vec3D offset
		) {
			super(uuid);
			this.offset = offset == null ? Vec3D.ZERO : offset;
		}
		
		protected Transform create() {
			return new Translate(getUuid(), offset);
		}
		
	}
	
}
