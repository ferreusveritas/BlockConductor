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

public class GradientShape extends Shape {
	
	public static final double DEFAULT_MIN_Y = 0.0;
	public static final double DEFAULT_MAX_Y = 255.0;
	public static final String MIN_Y = "minY";
	public static final String MAX_Y = "maxY";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("gradient")
		.loaderClass(Loader.class)
		.sceneObjectClass(GradientShape.class)
		.value(new NumberNodeValue.Builder(MIN_Y).def(DEFAULT_MIN_Y).build())
		.value(new NumberNodeValue.Builder(MAX_Y).def(DEFAULT_MAX_Y).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double minY;
	private final double maxY;
	
	private GradientShape(UUID uuid, double minY, double maxY) {
		super(uuid);
		this.minY = minY;
		this.maxY = maxY;
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
		if(pos.y() <= minY) {
			return 1.0;
		}
		if(pos.y() >= maxY) {
			return 0.0;
		}
		return 1.0 - ((pos.y() - minY) / (maxY - minY));
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double minY;
		private final double maxY;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MIN_Y) Double minY,
			@JsonProperty(MAX_Y) Double maxY
		) {
			super(uuid);
			this.minY = minY;
			this.maxY = maxY;
		}
		
		protected Shape create() {
			return new GradientShape(
				getUuid(),
				minY,
				maxY
			);
		}
		
	}
	
}
