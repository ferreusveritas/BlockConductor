package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class CavitateShape extends Shape {
	
	public static final String TYPE = "cavitate";
	
	private final Shape shape;
	private final AABBD bounds;
	
	public CavitateShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	public CavitateShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return shape.bounds();
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
		return bounds.contains(pos) && inside(pos) ? 1.0 : 0.0;
	}
	
	private boolean inside(Vec3D pos) {
		return inShape(pos) &&
			!(
				inShape(pos.down()) &&
				inShape(pos.up()) &&
				inShape(pos.north()) &&
				inShape(pos.south()) &&
				inShape(pos.west()) &&
				inShape(pos.east())
			);
	}
	
	private boolean inShape(Vec3D pos) {
		return shape.getVal(pos) >= 0.5;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape);
	}
	
}
