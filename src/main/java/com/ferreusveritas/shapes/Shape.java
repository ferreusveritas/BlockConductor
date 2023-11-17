package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	property = "type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = BoxShape.class, name = "box"),
	@JsonSubTypes.Type(value = CacheShape.class, name = "cache"),
	@JsonSubTypes.Type(value = CavitateShape.class, name = "cavitate"),
	@JsonSubTypes.Type(value = CheckerShape.class, name = "checker"),
	@JsonSubTypes.Type(value = CylinderShape.class, name = "cylinder"),
	@JsonSubTypes.Type(value = DifferenceShape.class, name = "difference"),
	@JsonSubTypes.Type(value = HeightmapShape.class, name = "heightmap"),
	@JsonSubTypes.Type(value = IntersectShape.class, name = "intersect"),
	@JsonSubTypes.Type(value = InvertShape.class, name = "invert"),
	@JsonSubTypes.Type(value = LayerShape.class, name = "layer"),
	@JsonSubTypes.Type(value = MeshModelShape.class, name = "model"),
	@JsonSubTypes.Type(value = SphereShape.class, name = "sphere"),
	@JsonSubTypes.Type(value = SurfaceShape.class, name = "surface"),
	@JsonSubTypes.Type(value = TranslateShape.class, name = "translate"),
	@JsonSubTypes.Type(value = UbiquitousShape.class, name = "ubiquitous"),
	@JsonSubTypes.Type(value = UnionShape.class, name = "union"),
	@JsonSubTypes.Type(value = VoidShape.class, name = "void")
})
public interface Shape {
	
	/**
	 * Get the bounding box of the shape
	 * @return the bounding box of the shape. Empty if the shape has no bounding box
	 */
	Optional<AABBI> getAABB();
	
	/**
	 * Check if the shape intersects at the given position
	 * @param pos the area to check
	 * @return true if the shape intersects
	 */
	boolean isInside(Vec3I pos);
}
