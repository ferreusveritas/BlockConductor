package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;


public class ReferenceHunk extends Hunk {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Hunk ref;
	
	public ReferenceHunk(Scene scene, Hunk ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceHunk(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString(REF).map(UUID::fromString).orElseThrow(missing(REF));
		this.ref = scene.getHunk(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return ref.bounds();
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
