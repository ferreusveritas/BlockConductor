package com.ferreusveritas.node.ports;

import com.ferreusveritas.node.mapper.BlockMapper;
import com.ferreusveritas.node.provider.BlockProvider;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.node.transform.Transform;

public class PortDataTypes {

	public static final PortDataType<BlockProvider> BLOCKS = new PortDataType<>("blocks", BlockProvider.class);
	public static final PortDataType<Shape> SHAPE = new PortDataType<>("shape", Shape.class);
	public static final PortDataType<BlockMapper> MAPPER = new PortDataType<>("mapper", BlockMapper.class);
	public static final PortDataType<Transform> TRANSFORM = new PortDataType<>("transform", Transform.class);
	public static final PortDataType<Boolean> BOOLEAN = new PortDataType<>("boolean", Boolean.class);
	
	private PortDataTypes() {
		throw new UnsupportedOperationException("This class cannot be instantiated");
	}
	
}
