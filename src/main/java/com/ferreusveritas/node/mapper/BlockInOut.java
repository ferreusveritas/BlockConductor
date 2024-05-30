package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;

public record BlockInOut(
	Block in,
	Block out
) {
}
