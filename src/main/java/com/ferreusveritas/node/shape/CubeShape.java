package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.BooleanNodeValue;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.UUID;

public class CubeShape extends Shape {
	
	public static final String SIZE = "size";
	public static final String CENTERED = "centered";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("cube")
		.loaderClass(Loader.class)
		.sceneObjectClass(CubeShape.class)
		.value(new VecNodeValue.Builder(SIZE).def(Vec3D.ONE).build())
		.value(new BooleanNodeValue.Builder(CENTERED).def(false).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final AABBD aabb;
	
	private CubeShape(UUID uuid, Vec3D size, boolean centered) {
		super(uuid);
		this.aabb = createBounds(size, centered);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private static AABBD createBounds(Vec3D size, boolean centered) {
		Vec3D min = centered ? size.div(2).neg() : Vec3D.ZERO;
		Vec3D max = min.add(size);
		return new AABBD(min, max);
	}
	
	@Override
	public AABBD bounds() {
		return aabb;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return aabb.contains(pos) ? 1.0 : 0.0;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final Vec3D size;
		private final boolean centered;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SIZE) Vec3D size,
			@JsonProperty(CENTERED) boolean centered
		) {
			super(uuid);
			this.size = size;
			this.centered = centered;
		}
		
		protected Shape create() {
			return new CubeShape(getUuid(), size, centered);
		}
		
	}
	
}