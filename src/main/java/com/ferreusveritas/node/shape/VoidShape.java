package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;

import java.util.UUID;

public class VoidShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("void")
		.loaderClass(Loader.class)
		.sceneObjectClass(VoidShape.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private VoidShape(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.EMPTY;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid
		) {
			super(uuid);
		}
		
		protected Shape create() {
			return new VoidShape(getUuid());
		}
		
	}
	
}
