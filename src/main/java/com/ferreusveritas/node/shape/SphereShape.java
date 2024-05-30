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

public class SphereShape extends Shape {
	
	public static final String RADIUS = "radius";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("sphere")
		.loaderClass(Loader.class)
		.sceneObjectClass(SphereShape.class)
		.value(new NumberNodeValue.Builder(RADIUS).min(0.0).def(1.0).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double radius;
	private final AABBD bounds;
	
	private SphereShape(UUID uuid, double radius) {
		super(uuid);
		this.radius = radius;
		this.bounds = calculateBounds(radius);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private static AABBD calculateBounds(double radius) {
		if(radius <= 0) {
			throw new IllegalArgumentException("radius must be greater than zero");
		}
		return new AABBD(
			new Vec3D(-radius, -radius, -radius),
			new Vec3D(radius, radius, radius)
		);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double radius;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(RADIUS) Double radius
		) {
			super(uuid);
			this.radius = radius;
		}
		
		protected Shape create() {
			return new SphereShape(getUuid(), radius);
		}
		
	}
	
}
