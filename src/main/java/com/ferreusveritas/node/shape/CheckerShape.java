package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class CheckerShape extends Shape {
	
	public static final String TYPE = "checker";
	public static final String AXIS = "axis";
	
	private final Axis axis; // null means all axes(3D checker block)
	
	private CheckerShape(UUID uuid, Axis axis) {
		super(uuid);
		this.axis = axis;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return isInside(pos.toVecI()) ? 1.0 : 0.0;
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(axis == null) {
			return (pos.x() + pos.y() + pos.z()) % 2 == 0;
		}
		return switch (axis) {
			case X -> pos.y() + pos.z() % 2 == 0;
			case Y -> pos.x() + pos.z() % 2 == 0;
			case Z -> pos.x() + pos.y() % 2 == 0;
		};
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(AXIS, axis);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Axis axis = null; // null means all axes(3D checker block)
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder axis(Axis axis) {
			this.axis = axis;
			return this;
		}
		
		public CheckerShape build() {
			return new CheckerShape(uuid, axis);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Axis axis; // null means all axes(3D checker block)
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.axis = src.getString(AXIS).flatMap(Axis::of).orElse(null);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new CheckerShape(getUuid(), axis);
		}
		
	}
	
}
