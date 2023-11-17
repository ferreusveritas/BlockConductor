package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.image.Image;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * Use an image as a height map
 */
public class HeightmapShape implements Shape {

	private final Image image;
	private final int height;
	private final Vec3I offset;
	private final AABBI aabb;
	
	@JsonCreator
	public HeightmapShape(
		@JsonProperty("image") Image image,
		@JsonProperty("height") int height,
		@JsonProperty("offset") Vec3I offset,
		@JsonProperty("infinite") boolean infinite
	) {
		this.image = image;
		this.height = height;
		this.offset = offset;
		this.aabb = calculateAABB(image, height, offset, infinite);
		if(height < 1) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
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
		int red = image.getPixel(x, z).getR(); // Use the red channel as the height map
		return y < red * height / 255;
	}
	
}
