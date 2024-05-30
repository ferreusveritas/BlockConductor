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

public class PyramidShape extends Shape {
	
	public static final String HEIGHT = "height";
	public static final String BASE = "base";
	public static final double DEFAULT_HEIGHT = 1.0;
	public static final double DEFAULT_BASE = 2.0;
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("pyramid")
		.loaderClass(Loader.class)
		.sceneObjectClass(PyramidShape.class)
		.value(new NumberNodeValue.Builder(HEIGHT).def(DEFAULT_HEIGHT).min(0.0).build())
		.value(new NumberNodeValue.Builder(BASE).def(DEFAULT_BASE).min(0.0).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final double height;
	private final double base;
	private final AABBD bounds;
	
	private PyramidShape(UUID uuid, double height, double base) {
		super(uuid);
		this.height = height;
		this.base = base;
		this.bounds = calculateBounds(height, base);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(double height, double base) {
		double halfBase = base / 2.0;
		return new AABBD(
			new Vec3D(-halfBase, 0, -halfBase),
			new Vec3D(halfBase, height, halfBase)
		);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(!bounds.contains(pos)) {
			return 0.0;
		}
		double halfBase = base / 2.0;
		double x = Math.abs(pos.x());
		double z = Math.abs(pos.z());
		double y = pos.y();
		double slope = height / halfBase;
		double heightAtPos = slope * (halfBase - Math.max(x, z));
		if(y < heightAtPos) {
			return 1.0;
		}
		return 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double height;
		private final double base;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(HEIGHT) Double height,
			@JsonProperty(BASE) Double base
		) {
			super(uuid);
			this.height = height;
			this.base = base;
		}
		
		protected Shape create() {
			return new PyramidShape(getUuid(), height, base);
		}
		
	}
	
}
