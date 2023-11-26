package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

public class InvertImage extends Image {
	
	public static final String TYPE = "invert";
	
	private final Image image;
	
	public InvertImage(Scene scene, Image image) {
		super(scene);
		this.image = image;
	}
	
	public InvertImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.image = src.getObj("image").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image"));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public RectI bounds() {
		return image.bounds();
	}
	
	@Override
	public double getVal(int x, int y) {
		return 1.0 - image.getVal(x, y);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("image", image);
	}
	
}
