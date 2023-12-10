package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class TranslateShape extends Shape {
	
	public static final String TYPE = "translate";
	public static final String OFFSET = "offset";
	
	private final Shape shape;
	private final Vec3D offset;
	
	public TranslateShape(Scene scene, Shape shape, Vec3I offset) {
		this(scene, shape, offset.toVecD());
	}
	
	public TranslateShape(Scene scene, Shape shape, Vec3D offset) {
		super(scene);
		this.shape = shape;
		this.offset = offset;
	}
	
	public TranslateShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.offset = src.getObj(OFFSET).map(Vec3D::new).orElseThrow(missing(OFFSET));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return shape.bounds().offset(offset);
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return shape.getVal(pos.sub(offset));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(OFFSET, offset);
	}
	
}
