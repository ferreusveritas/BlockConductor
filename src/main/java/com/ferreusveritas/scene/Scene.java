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
import com.ferreusveritas.support.json.Jsonable;

import java.util.*;

public class Scene implements Jsonable {
	
	private final Map<UUID, BlockProvider> blockProviders = new HashMap<>();
	private final Map<UUID, BlockMapper> blockMappers = new HashMap<>();
	private final Map<UUID, Shape> shapes = new HashMap<>();
	private final Map<UUID, Image> images = new HashMap<>();
	private final Map<UUID, Model> models = new HashMap<>();
	
	private final SceneReferences references;
	private BlockProvider root;
	
	public Scene() {
		this.references = new SceneReferences(this);
	}
	
	public Scene(JsonObj src) {
		this.references = new SceneReferences(this, src);
		this.root = src.getObj("root").map(this::createBlockProvider).orElse(null);
	}
	
	public void setRoot(BlockProvider root) {
		this.root = root;
	}
	
	public BlockProvider getRoot() {
		return root;
	}
	
	public void addBlockProvider(BlockProvider blockProvider) {
		blockProviders.put(blockProvider.getUuid(), blockProvider);
	}
	
	public BlockProvider createBlockProvider(JsonObj src) {
		BlockProvider blockProvider = BlockProviderFactory.create(this, src);
		addBlockProvider(blockProvider);
		return blockProvider;
	}
	
	public BlockProvider getBlockProvider(UUID uuid) {
		return blockProviders.get(uuid);
	}
	
	public void addBlockMapper(BlockMapper blockMapper) {
		blockMappers.put(blockMapper.getUuid(), blockMapper);
	}
	
	public BlockMapper createBlockMapper(JsonObj src) {
		BlockMapper blockMapper = BlockMapperFactory.create(this, src);
		addBlockMapper(blockMapper);
		return blockMapper;
	}
	
	public BlockMapper getBlockMapper(UUID uuid) {
		return blockMappers.get(uuid);
	}
	
	public void addShape(Shape shape) {
		shapes.put(shape.getUuid(), shape);
	}
	
	public Shape createShape(JsonObj src) {
		Shape shape = ShapeFactory.create(this, src);
		addShape(shape);
		return shape;
	}
	
	public Shape getShape(UUID uuid) {
		return shapes.get(uuid);
	}
	
	public void addImage(Image image) {
		images.put(image.getUuid(), image);
	}
	
	public Image createImage(JsonObj src) {
		Image image = ImageFactory.create(this, src);
		addImage(image);
		return image;
	}
	
	public Image getImage(UUID uuid) {
		return images.get(uuid);
	}
	
	public void addModel(Model model) {
		models.put(model.getUuid(), model);
	}
	
	public Model createModel(JsonObj src) {
		Model model = ModelFactory.create(this, src);
		addModel(model);
		return model;
	}
	
	public Model getModel(UUID uuid) {
		return models.get(uuid);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("references", references)
			.set("root", root);
	}
	
}
