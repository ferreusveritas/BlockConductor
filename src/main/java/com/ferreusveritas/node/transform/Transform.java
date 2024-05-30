package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.SceneObject;

import java.util.UUID;

public abstract class Transform extends SceneObject {
	
	public static final String TRANSFORM = "transform";
	
	protected Transform(UUID uuid) {
		super(uuid);
	}
	
	public abstract Matrix4X4 getMatrix();
	
}

