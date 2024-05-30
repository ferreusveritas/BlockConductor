package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.node.values.BlockNodeValue;

import java.util.Optional;
import java.util.UUID;

import static com.ferreusveritas.node.shape.Shape.SHAPE;

public class ShapeBlockProvider extends BlockProvider {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType(SHAPE)
		.loaderClass(Loader.class)
		.sceneObjectClass(ShapeBlockProvider.class)
		.value(new BlockNodeValue.Builder(BLOCK).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final Block block;
	private final Shape shape;
	private final AABBI aabb;
	
	private ShapeBlockProvider(UUID uuid, Shape shape, Block block) {
		super(uuid);
		this.block = block;
		this.shape = shape;
		if(block == null) {
			throw new IllegalArgumentException("block cannot be null");
		}
		if(shape == null) {
			throw new IllegalArgumentException("shape cannot be null");
		}
		this.aabb = calculateBounds(shape);
	}
	
	private AABBI calculateBounds(Shape shape) {
		return shape.bounds().toAABBI();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	public Block getBlock() {
		return block;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area);
		if(bounds == AABBI.EMPTY) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area.size());
		bounds.forEach((abs,rel) -> processBlock(blocks, abs, abs.sub(area.min())));
		return Optional.of(blocks);
	}
	
	private void processBlock(Blocks blocks, Vec3I absPos, Vec3I relPos) {
		if(shape.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final Block block;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(BLOCK) Block block,
			@JsonProperty(SHAPE) PortAddress shape
		) {
			super(uuid);
			this.block = block;
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shape);
		}
		
		protected ShapeBlockProvider create() {
			return new ShapeBlockProvider(
				getUuid(),
				get(shapeInput),
				block
			);
		}
		
	}
	
}
