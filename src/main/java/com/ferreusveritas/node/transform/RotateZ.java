package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class RotateZ extends Transform {
	
	public static final String TYPE = "rotatez";
	public static final String ANGLE = "angle";
	
	private final double angle;
	private final Matrix4X4 matrix;
	
	private RotateZ(UUID uuid, double angle) {
		super(uuid);
		this.angle = angle;
		this.matrix = Matrix4X4.IDENTITY.rotateZ(angle);
	}
	
	@Override
	public Matrix4X4 getData() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(ANGLE, angle);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private double angle = 0.0;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder angle(double angle) {
			this.angle = angle;
			return this;
		}
		
		public Builder degrees(double angleDeg) {
			this.angle = Math.toRadians(angleDeg);
			return this;
		}
		
		public RotateZ build() {
			return new RotateZ(uuid, angle);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double angle;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.angle = src.getDouble(ANGLE).orElse(0.0);
		}
		
		@Override
		public RotateZ load(LoaderSystem loaderSystem) {
			return new RotateZ(getUuid(), angle);
		}
		
	}
	
}
