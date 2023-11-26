package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * CombineImage is an abstract Image that combines the values of multiple images together.
 */
public abstract class CombineImage extends Image {
	
	protected final List<Image> images;
	protected final RectI bounds;
	
	protected CombineImage(Scene scene, List<Image> images) {
		super(scene);
		this.images = images;
		this.bounds = createBounds(images);
	}
	
	protected CombineImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.images = src.getList("images").toImmutableList(scene::createImage);
		this.bounds = createBounds(images);
	}
	
	private RectI createBounds(List<Image> images) {
		return images.stream().map(Image::bounds).reduce((a, b) -> RectI.union(a, b)).orElse(RectI.INFINITE);
	}
	
	@Override
	public RectI bounds() {
		return bounds;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("images", JsonObj.newList(images));
	}
	
}
