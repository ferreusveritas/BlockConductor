package com.ferreusveritas.scene;

import com.ferreusveritas.block.mapper.BlockMapper;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.image.Image;
import com.ferreusveritas.model.FullMeshModel;
import com.ferreusveritas.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	
	private List<BlockProvider> blockProviders = new ArrayList<>();
	private List<BlockMapper> blockMappers = new ArrayList<>();
	private List<Shape> shapes = new ArrayList<>();
	private List<Image> images = new ArrayList<>();
	private List<FullMeshModel> models = new ArrayList<>();
	
	public void addBlockProvider(BlockProvider blockProvider) {
		blockProviders.add(blockProvider);
	}
	
}
