package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.NumberNodeValue;

import java.util.UUID;

/**
 * ConstantShape is a Shape that returns a constant value for all coordinates.
 */
public class ConstantShape extends Shape {
	
	public static final String VALUE = "value";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("constant")
		.loaderClass(Loader.class)
		.sceneObjectClass(ConstantShape.class)
		.value(new NumberNodeValue.Builder(VALUE).def(0.0).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double value;
	
	private ConstantShape(UUID uuid, double value) {
		super(uuid);
		this.value = value;
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
		return value;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double value;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(VALUE) double value
		) {
			super(uuid);
			this.value = value;
		}
		
		protected Shape create() {
			return new ConstantShape(getUuid(), value);
		}
		
	}
	
}
