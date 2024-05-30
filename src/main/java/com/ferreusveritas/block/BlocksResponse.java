package com.ferreusveritas.block;

import com.ferreusveritas.math.Vec3I;

import java.util.List;

public record BlocksResponse(
	Vec3I size,
	List<Block> blockMap,
	List<Short> blockData
) {
}
