package com.ferreusveritas.node.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;

import java.util.UUID;

public class Identity extends Transform {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("identity")
		.loaderClass(Loader.class)
		.sceneObjectClass(Identity.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	
	private Identity(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return Matrix4X4.IDENTITY;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}

	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends TransformProviderLoaderNode {
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid
		) {
			super(uuid);
		}
		
		protected Transform create() {
			return new Identity(getUuid());
		}
		
	}
	
}
