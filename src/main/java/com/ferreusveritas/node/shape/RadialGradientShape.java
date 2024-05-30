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
 * A Shape that represents a radial gradient from the origin
 */
public class RadialGradientShape extends Shape {
	
	public static final String RADIUS = "radius";
	public static final double DEFAULT_RADIUS = 1.0;
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("radialGradient")
		.loaderClass(Loader.class)
		.sceneObjectClass(RadialGradientShape.class)
		.value(new NumberNodeValue.Builder(RADIUS).min(0.0).def(DEFAULT_RADIUS).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double radius;
	private final AABBD bounds;
	
	private RadialGradientShape(UUID uuid, double radius) {
		super(uuid);
		this.radius = radius;
		this.bounds = calculateBounds(radius);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(double radius) {
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
		double dist = pos.lenSq() / (radius * radius);
		if(dist > 1.0) {
			return 0.0;
		}
		return 1.0 - dist;
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
			return new RadialGradientShape(getUuid(), radius);
		}
		
	}
	
}
