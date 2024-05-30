package com.ferreusveritas.node.transform;

import com.ferreusveritas.misc.Lazy;
import com.ferreusveritas.node.LoaderNode;
import com.ferreusveritas.node.ports.PortDataTypes;

import java.util.UUID;

public abstract class TransformProviderLoaderNode extends LoaderNode {
	
	protected TransformProviderLoaderNode(UUID uuid) {
		super(uuid);
		createDefaultOutputPort();
	}
	
	protected void createDefaultOutputPort() {
		Lazy<Transform> transform = new Lazy<>(this::create);
		createOutputPort(PortDataTypes.TRANSFORM, transform::get);
	}
	
	protected abstract Transform create();
	
}
