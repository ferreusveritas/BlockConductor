package com.ferreusveritas.image;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * Subtracts the values of all images in the list from the first image in the list.
 */
public class SubImage extends CombineImage {
	
	public static final String TYPE = "add";
	
	public SubImage(Scene scene, List<Image> images) {
		super(scene, images);
	}
	
	public SubImage(Scene scene, Image ... images) {
		this(scene, List.of(images));
	}
	
	public SubImage(Scene scene, JsonObj src) {
		super(scene, src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public double getVal(int x, int y) {
		int size = images.size();
		double accum = size > 0 ? images.get(0).getVal(x, y) : 0.0;
		for(int i = 1; i < size; i++) {
			accum -= images.get(i).getVal(x, y);
		}
		return accum;
	}
	
}
