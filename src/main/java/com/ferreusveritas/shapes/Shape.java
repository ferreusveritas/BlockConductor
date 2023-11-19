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
	@JsonSubTypes.Type(value = BoxShape.class, name = BoxShape.TYPE),
	@JsonSubTypes.Type(value = CacheShape.class, name = CacheShape.TYPE),
	@JsonSubTypes.Type(value = CavitateShape.class, name = CavitateShape.TYPE),
	@JsonSubTypes.Type(value = CheckerShape.class, name = CheckerShape.TYPE),
	@JsonSubTypes.Type(value = CylinderShape.class, name = CylinderShape.TYPE),
	@JsonSubTypes.Type(value = DifferenceShape.class, name = DifferenceShape.TYPE),
	@JsonSubTypes.Type(value = HeightmapShape.class, name = HeightmapShape.TYPE),
	@JsonSubTypes.Type(value = IntersectShape.class, name = IntersectShape.TYPE),
	@JsonSubTypes.Type(value = InvertShape.class, name = InvertShape.TYPE),
	@JsonSubTypes.Type(value = LayerShape.class, name = LayerShape.TYPE),
	@JsonSubTypes.Type(value = MeshModelShape.class, name = MeshModelShape.TYPE),
	@JsonSubTypes.Type(value = SphereShape.class, name = SphereShape.TYPE),
	@JsonSubTypes.Type(value = SurfaceShape.class, name = SurfaceShape.TYPE),
	@JsonSubTypes.Type(value = TranslateShape.class, name = TranslateShape.TYPE),
	@JsonSubTypes.Type(value = UbiquitousShape.class, name = UbiquitousShape.TYPE),
	@JsonSubTypes.Type(value = UnionShape.class, name = UnionShape.TYPE),
	@JsonSubTypes.Type(value = VoidShape.class, name = VoidShape.TYPE)
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
