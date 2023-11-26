package com.ferreusveritas.image;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * AddImage is a type of CombineImage that adds the values of all the images together.
 */
public class AddImage extends CombineImage {
	
	public static final String TYPE = "add";
	
	public AddImage(Scene scene, List<Image> images) {
		super(scene, images);
	}
	
	public AddImage(Scene scene, Image ... images) {
		this(scene, List.of(images));
	}
	
	public AddImage(Scene scene, JsonObj src) {
		super(scene, src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public double getVal(int x, int y) {
		double accum = 0;
		for(Image image : images) {
			accum += image.getVal(x, y);
		}
		return accum;
	}
	
}
