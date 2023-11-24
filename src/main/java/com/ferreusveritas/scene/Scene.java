package com.ferreusveritas.scene;

import com.ferreusveritas.block.mapper.BlockMapper;
import com.ferreusveritas.block.mapper.BlockMapperFactory;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.block.provider.BlockProviderFactory;
import com.ferreusveritas.image.Image;
import com.ferreusveritas.image.ImageFactory;
import com.ferreusveritas.model.Model;
import com.ferreusveritas.model.ModelFactory;
import com.ferreusveritas.shapes.Shape;
import com.ferreusveritas.shapes.ShapeFactory;
import com.ferreusveritas.support.json.JsonObj;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	
	private List<BlockProvider> blockProviders = new ArrayList<>();
	private List<BlockMapper> blockMappers = new ArrayList<>();
	private List<Shape> shapes = new ArrayList<>();
	private List<Image> images = new ArrayList<>();
	private List<Model> models = new ArrayList<>();
	
	public void addBlockProvider(BlockProvider blockProvider) {
		blockProviders.add(blockProvider);
	}
	
	public BlockProvider createBlockProvider(JsonObj src) {
		BlockProvider blockProvider = BlockProviderFactory.create(this, src);
		addBlockProvider(blockProvider);
		return blockProvider;
	}
	
	public void addBlockMapper(BlockMapper blockMapper) {
		blockMappers.add(blockMapper);
	}
	
	public BlockMapper createBlockMapper(JsonObj src) {
		BlockMapper blockMapper = BlockMapperFactory.create(this, src);
		addBlockMapper(blockMapper);
		return blockMapper;
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	public Shape createShape(JsonObj src) {
		Shape shape = ShapeFactory.create(this, src);
		addShape(shape);
		return shape;
	}
	
	public void addImage(Image image) {
		images.add(image);
	}
	
	public Image createImage(JsonObj src) {
		Image image = ImageFactory.create(this, src);
		addImage(image);
		return image;
	}
	
	public void addModel(Model model) {
		models.add(model);
	}
	
	public Model createModel(JsonObj src) {
		Model model = ModelFactory.create(this, src);
		addModel(model);
		return model;
	}
	
}
