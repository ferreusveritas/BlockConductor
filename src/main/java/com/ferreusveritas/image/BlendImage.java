package com.ferreusveritas.image;

import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

/**
 * BlendImage is a type of CombineImage that blends the values of two images together using a blend image as a mask.
 */
public class BlendImage extends Image {
	
	public static final String TYPE = "blend";
	
	private final Image image1;
	private final Image image2;
	private final Image blend;
	private final RectI bounds;
	
	public BlendImage(Scene scene, Image image1, Image image2, Image blend) {
		super(scene);
		this.image1 = image1;
		this.image2 = image2;
		this.blend = blend;
		this.bounds = RectI.union(image1.bounds(), image2.bounds());
	}
	
	public BlendImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.image1 = src.getObj("image1").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image1"));
		this.image2 = src.getObj("image2").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image1"));
		this.blend = src.getObj("blend").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing blend"));
		this.bounds = RectI.union(image1.bounds(), image2.bounds());
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
		double blendVal = blend.getVal(x, y);
		if(blendVal < 0.0){
			return image1.getVal(x, y);
		}
		if(blendVal > 1.0){
			return image2.getVal(x, y);
		}
		double val1 = image1.getVal(x, y);
		double val2 = image2.getVal(x, y);
		return MathHelper.lerp(blendVal, val1, val2);
	}
	
}
