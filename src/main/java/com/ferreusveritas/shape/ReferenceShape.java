package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A Shape that references another Shape by UUID
 */
public class ReferenceShape extends Shape {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Shape ref;
	private final AABBD bounds;
	
	public ReferenceShape(Scene scene, Shape ref) {
		super(scene);
		this.ref = ref;
		this.bounds = calculateBounds();
	}
	
	public ReferenceShape(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString(REF).map(UUID::fromString).orElseThrow(missing(REF));
		this.ref = scene.getShape(uuid);
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return ref.bounds();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return ref.getVal(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(REF, ref);
	}
}
