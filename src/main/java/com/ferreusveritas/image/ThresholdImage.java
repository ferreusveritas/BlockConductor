package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

public class ThresholdImage extends Image {
	
	public static final String TYPE = "threshold";
	public static final double DEFAULT_THRESHOLD = 0.5;
	
	private final Image image;
	private final double threshold;
	
	public ThresholdImage(Scene scene, Image image) {
		this(scene, image, DEFAULT_THRESHOLD);
	}
	
	public ThresholdImage(Scene scene, Image image, double threshold) {
		super(scene);
		this.image = image;
		this.threshold = threshold;
	}
	
	public ThresholdImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.image = src.getObj("image").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image"));
		this.threshold = src.getDouble("threshold").orElse(DEFAULT_THRESHOLD);
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
		return image.getVal(x, y) > threshold ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("image", image)
			.set("threshold", threshold);
	}
	
}
