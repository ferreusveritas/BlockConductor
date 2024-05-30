package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.StringNodeValue;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.exceptions.ReadException;
import com.ferreusveritas.support.storage.Storage;

import java.util.Optional;
import java.util.UUID;

public class ReferenceBlockProvider extends BlockProvider {
	
	public static final String REF = "ref";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("reference")
		.loaderClass(Loader.class)
		.sceneObjectClass(ReferenceBlockProvider.class)
		.value(new StringNodeValue.Builder(REF).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
	private final BlockProvider blocks;
	
	private ReferenceBlockProvider(UUID uuid, String ref) {
		super(uuid);
		this.blocks = loadBlocks(ref);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private static BlockProvider loadBlocks(String ref) {
		try {
			Scene scene = Storage.getObject(ref, Scene.class);
			return scene.root().getOutputPort("blocks", BlockProvider.class).map(OutputPort::read).orElseThrow();
		} catch (Exception e) {
			throw new ReadException("Failed to load reference block provider: " + ref, e);
		}
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return blocks.getBlocks(request);
	}
	
	@Override
	public AABBI getAABB() {
		return blocks.getAABB();
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final String ref;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(REF) String ref
		) {
			super(uuid);
			this.ref = ref;
		}
		
		protected ReferenceBlockProvider create() {
			return new ReferenceBlockProvider(
				getUuid(),
				ref
			);
		}
		
	}
	
}
