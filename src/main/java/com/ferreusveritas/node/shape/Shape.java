package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.Node;

import java.util.UUID;

public abstract class Shape extends Node {
	
	public static final String SHAPE = "shape";
	public static final String BOUNDS = "bounds";
	public static final String TYPE = "type";
	private static final double THRESHOLD = 0.5;
	
	protected Shape(UUID uuid) {
		super(uuid);
	}
	
	public abstract String getType();
	
	public Class<? extends Node> getNodeClass() {
		return Shape.class;
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
