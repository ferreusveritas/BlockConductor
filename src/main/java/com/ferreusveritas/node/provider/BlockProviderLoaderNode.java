package com.ferreusveritas.node.provider;

import com.ferreusveritas.misc.Lazy;
import com.ferreusveritas.node.LoaderNode;
import com.ferreusveritas.node.ports.PortDataTypes;

import java.util.UUID;

public abstract class BlockProviderLoaderNode extends LoaderNode {
	
	protected BlockProviderLoaderNode(UUID uuid) {
		super(uuid);
		createDefaultOutputPort();
	}
	
	protected void createDefaultOutputPort() {
		Lazy<BlockProvider> blocks = new Lazy<>(this::create);
		createOutputPort(PortDataTypes.BLOCKS, blocks::get);
	}
	
	protected abstract BlockProvider create();
	
}
