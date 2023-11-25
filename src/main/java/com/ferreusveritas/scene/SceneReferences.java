package com.ferreusveritas.scene;

import com.ferreusveritas.block.mapper.BlockMapper;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.image.Image;
import com.ferreusveritas.model.Model;
import com.ferreusveritas.shapes.Shape;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.ArrayList;
import java.util.List;

public class SceneReferences implements Jsonable {
	
	List<BlockProvider> blockProviders;
	List<BlockMapper> blockMappers;
	List<Shape> shapes;
	List<Image> images;
	List<Model> models;
	
	public SceneReferences() {
		this.blockProviders = new ArrayList<>();
		this.blockMappers = new ArrayList<>();
		this.shapes = new ArrayList<>();
		this.images = new ArrayList<>();
		this.models = new ArrayList<>();
	}
	
	public SceneReferences(Scene scene, JsonObj src) {
		this.blockProviders = src.getList("blockProviders").toMutableList(scene::createBlockProvider);
		this.blockMappers = src.getList("blockMappers").toMutableList(scene::createBlockMapper);
		this.shapes = src.getList("shapes").toMutableList(scene::createShape);
		this.images = src.getList("images").toMutableList(scene::createImage);
		this.models = src.getList("models").toMutableList(scene::createModel);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("blockProviders", JsonObj.newList(blockProviders))
			.set("blockMappers", JsonObj.newList(blockMappers))
			.set("shapes", JsonObj.newList(shapes))
			.set("images", JsonObj.newList(images))
			.set("models", JsonObj.newList(models));
	}
	
}