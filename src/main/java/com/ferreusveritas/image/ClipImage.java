package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

public class ClipImage extends Image {
	
	public static final String TYPE = "clip";
	
	private final Image image;
	private final RectI bounds;
	
	public ClipImage(Scene scene, Image image, RectI bounds) {
		super(scene);
		this.image = image;
		this.bounds = bounds;
	}
	
	public ClipImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.image = src.getObj("image").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image"));
		this.bounds = src.getObj("bounds").map(RectI::new).orElseThrow(() -> new InvalidJsonProperty("Missing bounds"));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public RectI bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(int x, int y) {
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("image", image)
			.set("bounds", bounds);
	}
}
