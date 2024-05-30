package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.misc.MiscHelper;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.NumberNodeValue;

import java.util.UUID;

public class LayerShape extends Shape {
	
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("layer")
		.loaderClass(Loader.class)
		.sceneObjectClass(LayerShape.class)
		.value(new NumberNodeValue.Builder(MIN).def(0).build())
		.value(new NumberNodeValue.Builder(MAX).def(1).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double min; // inclusive
	private final double max; // exclusive
	private final AABBD bounds;
	
	private LayerShape(UUID uuid, double min, double max) {
		super(uuid);
		this.min = min;
		this.max = max;
		this.bounds = calculateBounds();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(Double.NEGATIVE_INFINITY, min, Double.NEGATIVE_INFINITY),
			new Vec3D(Double.POSITIVE_INFINITY, max, Double.POSITIVE_INFINITY)
		);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return pos.y() >= min && pos.y() < max ? 1.0 : 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double min;
		private final double max;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MIN) Double min,
			@JsonProperty(MAX) Double max
		) {
			super(uuid);
			this.min = min;
			this.max = max;
			validate();
		}
		
		private void validate() {
			MiscHelper.require(min, MIN);
			MiscHelper.require(max, MAX);
			MiscHelper.require(min < max, MIN + " must be less than " + MAX);
		}
		
		protected Shape create() {
			return new LayerShape(getUuid(), min, max);
		}
		
	}
	
}
