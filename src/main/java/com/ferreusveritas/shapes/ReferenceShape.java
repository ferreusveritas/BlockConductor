package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class ReferenceShape extends Shape {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Shape ref;
	
	public ReferenceShape(Scene scene, Shape ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceShape(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString(REF).map(UUID::fromString).orElseThrow(missing(REF));
		this.ref = scene.getShape(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return ref.getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return ref.isInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(REF, ref.getUuid().toString());
	}
}
