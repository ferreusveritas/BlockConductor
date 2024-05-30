package com.ferreusveritas.node.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.NumberNodeValue;

import java.util.UUID;

public class RotateY extends Transform {
	
	public static final String ANGLE = "angle";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(TRANSFORM)
		.minorType("rotatey")
		.loaderClass(Loader.class)
		.sceneObjectClass(RotateY.class)
		.value(new NumberNodeValue.Builder(ANGLE).def(0.0).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.TRANSFORM))
		.build();
	
	private final Matrix4X4 matrix;
	
	private RotateY(UUID uuid, double angle) {
		super(uuid);
		this.matrix = Matrix4X4.IDENTITY.rotateY(Math.toRadians(angle));
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
		
		private final double angle;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(ANGLE) Double angle
		) {
			super(uuid);
			this.angle = angle;
		}
		
		protected Transform create() {
			return new RotateY(getUuid(), angle);
		}
		
	}
	
}
