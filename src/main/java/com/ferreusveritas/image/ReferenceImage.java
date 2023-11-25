package com.ferreusveritas.image;

import com.ferreusveritas.math.Pixel;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;


public class ReferenceImage extends Image {
	
	public static final String TYPE = "reference";
	
	private final Image ref;
	
	public ReferenceImage(Scene scene, Image ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceImage(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString("ref").map(UUID::fromString).orElseThrow(() -> new InvalidJsonProperty("ref missing"));
		this.ref = scene.getImage(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public RectI bounds() {
		return ref.bounds();
	}
	
	@Override
	public Pixel getPixel(int x, int y) {
		return ref.getPixel(x, y);
	}
	
}
