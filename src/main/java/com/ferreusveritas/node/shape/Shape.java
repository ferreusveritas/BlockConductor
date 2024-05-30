package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.SceneObject;

import java.util.UUID;

public abstract class Shape extends SceneObject {
	
	public static final String SHAPE = "shape";
	private static final double THRESHOLD = 0.5;
	
	protected Shape(UUID uuid) {
		super(uuid);
	}
	
	public abstract AABBD bounds();
	
	public abstract double getVal(Vec3D pos);
	
	public boolean isInside(Vec3D pos) {
		return getVal(pos) >= THRESHOLD;
	}
	
	public boolean isInside(Vec3I pos) {
		return isInside(pos.toVecD());
	}
	
}
