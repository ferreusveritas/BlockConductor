package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.transform.Transform;
import com.ferreusveritas.transform.TransformFactory;

public class TransformHunk extends Hunk {
	
	public static final String TYPE = "transform";
	public static final String TRANSFORM = "transform";
	
	private final Hunk hunk;
	private final Transform transform;
	private final Matrix4X4 matrix;
	private final AABBD bounds;
	
	public TransformHunk(Scene scene, Hunk hunk, Transform transform) {
		super(scene);
		this.hunk = hunk;
		this.transform = transform;
		this.matrix = transform.getMatrix().invert();
		this.bounds = hunk.bounds().transform(transform.getMatrix());
	}
	
	public TransformHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.hunk = src.getObj(HUNK).map(scene::createHunk).orElseThrow(missing(HUNK));
		this.transform = src.getObj(TRANSFORM).map(TransformFactory::create).orElseThrow(missing(TRANSFORM));
		this.matrix = transform.getMatrix().invert();
		this.bounds = hunk.bounds().transform(transform.getMatrix());
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
		Vec3D transPos = matrix.transform(pos);
		if(hunk.bounds().isInside(transPos)) {
			return hunk.getVal(transPos);
		}
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(TRANSFORM, transform)
			.set(HUNK, hunk);
	}
	
}
