package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.BitSet;
import java.util.UUID;

public class CacheShape extends Shape {
	
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("cache")
		.loaderClass(Loader.class)
		.sceneObjectClass(CacheShape.class)
		.value(new VecNodeValue.Builder(MIN).build())
		.value(new VecNodeValue.Builder(MAX).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final AABBD bounds;
	private final AABBI aabb;
	private final BitSet cache;
	
	private CacheShape(UUID uuid, Shape shape, AABBD bounds) {
		super(uuid);
		this.shape = shape;
		this.bounds = bounds;
		this.aabb = bounds.toAABBI();
		this.cache = buildCache(this.aabb);
	}
	
	private BitSet buildCache(AABBI aabb) {
		if(aabb == null) {
			throw new IllegalArgumentException("CacheShape must have a valid AABB");
		}
		BitSet bitSet = new BitSet(aabb.vol());
		int index = 0;
		for (int y = aabb.min().y(); y <= aabb.max().y(); y++) {
			for (int z = aabb.min().z(); z <= aabb.max().z(); z++) {
				for (int x = aabb.min().x(); x <= aabb.max().x(); x++) {
					bitSet.set(index++, shape.getVal(new Vec3I(x, y, z).toVecD()) >= 0.5);
				}
			}
		}
		return bitSet;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return bounds.contains(pos) && inside(pos.toVecI()) ? 1.0 : 0.0;
	}
	
	private boolean inside(Vec3I pos) {
		return cache.get(pos.calcIndex(aabb.size()));
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final AABBD bounds;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MIN) Vec3D min,
			@JsonProperty(MAX) Vec3D max,
			@JsonProperty(SHAPE) PortAddress shapeAddress
		) {
			super(uuid);
			this.bounds = new AABBD(min, max);
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shapeAddress);
		}
		
		protected Shape create() {
			return new CacheShape(
				getUuid(),
				get(shapeInput),
				bounds
			);
		}
		
	}
	
}
