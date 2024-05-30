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

/**
 * A Shape that represents a plane at y = 0 with one side being 1.0 and the other 0.0
 */
public class PlaneShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("plane")
		.loaderClass(Loader.class)
		.sceneObjectClass(PlaneShape.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private PlaneShape(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.y() < 0 ? 1.0 : 0.0;
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
			return new PlaneShape(getUuid());
		}
		
	}
	
}
