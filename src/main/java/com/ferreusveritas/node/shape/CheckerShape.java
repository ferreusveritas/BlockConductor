package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.EnumNodeValue;

import java.util.UUID;

public class CheckerShape extends Shape {
	
	public static final String AXIS = "axis";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("checker")
		.loaderClass(Loader.class)
		.sceneObjectClass(CheckerShape.class)
		.value(new EnumNodeValue.Builder(AXIS).values(Axis.class).def(Axis.X).nullable(true).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Axis axis; // null means all axes(3D checker block)
	
	private CheckerShape(UUID uuid, Axis axis) {
		super(uuid);
		this.axis = axis;
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
		return isInside(pos.toVecI()) ? 1.0 : 0.0;
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(axis == null) {
			return (pos.x() + pos.y() + pos.z()) % 2 == 0;
		}
		return switch (axis) {
			case X -> pos.y() + pos.z() % 2 == 0;
			case Y -> pos.x() + pos.z() % 2 == 0;
			case Z -> pos.x() + pos.y() % 2 == 0;
		};
	}

	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final Axis axis; // null means all axes(3D checker block)
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(AXIS) Axis axis
		) {
			super(uuid);
			this.axis = axis;
		}
		
		protected Shape create() {
			return new CheckerShape(
				getUuid(),
				axis
			);
		}
		
	}
	
}
