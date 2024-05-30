package com.ferreusveritas.node.shape;

import com.ferreusveritas.misc.Lazy;
import com.ferreusveritas.node.LoaderNode;
import com.ferreusveritas.node.ports.PortDataTypes;

import java.util.UUID;

public abstract class ShapeLoaderNode extends LoaderNode {
	
	protected ShapeLoaderNode(UUID uuid) {
		super(uuid);
		createDefaultOutputPort();
	}
	
	protected void createDefaultOutputPort() {
		Lazy<Shape> shape = new Lazy<>(this::create);
		createOutputPort(PortDataTypes.SHAPE, shape::get);
	}
	
	protected abstract Shape create();
	
}
