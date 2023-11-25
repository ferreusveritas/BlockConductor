package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class ReferenceShape extends Shape {
	
	public static final String TYPE = "reference";
	
	private final Shape ref;
	
	public ReferenceShape(Scene scene, Shape ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceShape(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString("ref").map(UUID::fromString).orElseThrow(() -> new InvalidJsonProperty("ref missing"));
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
	
}
