package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.Node;

import java.util.UUID;

public abstract class Transform extends Node {
	
	protected Transform(UUID uuid) {
		super(uuid);
	}
	
	public abstract Matrix4X4 getData();
	
	@Override
	public Class<? extends Node> getNodeClass() {
		return Transform.class;
	}
	
	public abstract String getType();
	
}

