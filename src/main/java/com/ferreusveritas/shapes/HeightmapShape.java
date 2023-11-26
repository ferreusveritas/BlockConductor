package com.ferreusveritas.shapes;

import com.ferreusveritas.image.Image;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Use an image as a height map
 */
public class HeightmapShape extends Shape {
	
	public static final String TYPE = "heightmap";
	
	private final Image image;
	private final int height;
	private final Vec3I offset;
	private final boolean infinite;
	private final AABBI aabb;
	
	public HeightmapShape(Scene scene, Image image, int height, Vec3I offset, boolean infinite) {
		super(scene);
		this.image = image;
		this.height = height;
		this.offset = offset;
		this.infinite = infinite;
		this.aabb = calculateAABB(image, height, offset, infinite);
		validate();
	}
	
	public HeightmapShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.image = src.getObj("image").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image"));
		this.height = src.getInt("height").orElse(1);
		this.offset = src.getObj("offset").map(Vec3I::new).orElse(Vec3I.ZERO);
		this.infinite = src.getBoolean("infinite").orElse(false);
		this.aabb = calculateAABB(image, height, offset, infinite);
		validate();
	}
	
	private void validate() {
		if(height < 1) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	private static AABBI calculateAABB(Image image, int height, Vec3I offset, boolean infinite) {
		if(infinite) {
			return new AABBI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, height - 1, Integer.MAX_VALUE).offset(offset);
		}
		return new AABBI(image.bounds(), Integer.MIN_VALUE, height - 1).offset(offset);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(!aabb.isPointInside(pos)) {
			return false;
		}
		pos = pos.sub(offset); // bring to relative coordinates
		int x = pos.x();
		int y = pos.y();
		int z = pos.z();
		if(y < 0) {
			return true; // slight optimization.  Anything below the height map is considered inside
		}
		double val = image.getVal(x, z);
		return y <= val * height;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("image", image)
			.set("height", height)
			.set("offset", offset)
			.set("infinite", infinite);
	}
	
}
