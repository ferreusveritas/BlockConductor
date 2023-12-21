package com.ferreusveritas.node.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.Node;

import java.util.UUID;

public abstract class Model extends Node {
	
	protected Model(UUID uuid) {
		super(uuid);
	}
	
	public abstract String getType();
	
	public Class<? extends Node> getNodeClass() {
		return Model.class;
	}
	
	public abstract boolean pointIsInside(Vec3D point);
	
	public abstract AABBD getAABB();
	
}
