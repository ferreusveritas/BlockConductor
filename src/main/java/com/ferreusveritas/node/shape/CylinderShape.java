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

public class CylinderShape extends Shape {
	
	public static final String HEIGHT = "height";
	public static final String RADIUS = "radius";
	public static final double DEFAULT_HEIGHT = 1;
	public static final double DEFAULT_RADIUS = 1.0;
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("cylinder")
		.loaderClass(Loader.class)
		.sceneObjectClass(CylinderShape.class)
		.value(new NumberNodeValue.Builder(HEIGHT).min(0.0).def(DEFAULT_HEIGHT).build())
		.value(new NumberNodeValue.Builder(RADIUS).min(0.0).def(DEFAULT_RADIUS).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double height;
	private final double radius;
	private final AABBD bounds;
	
	private CylinderShape(UUID uuid, double height, double radius) {
		super(uuid);
		this.height = height;
		this.radius = radius;
		this.bounds = calculateBounds();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(-radius, 0, -radius),
			new Vec3D(radius, height, radius)
		);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.y() >= 0 && pos.y() < height && pos.withY(0.0).lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double height;
		private final double radius;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(HEIGHT) Double height,
			@JsonProperty(RADIUS) Double radius
		) {
			super(uuid);
			this.height = height;
			this.radius = radius;
		}
		
		protected Shape create() {
			return new CylinderShape(
				getUuid(),
				height,
				radius
			);
		}
		
	}
	
}
