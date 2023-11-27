package com.ferreusveritas.scene;

import com.ferreusveritas.block.mapper.BlockMapper;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.hunk.Hunk;
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
	List<Hunk> hunks;
	List<Model> models;
	
	public SceneReferences() {
		this.blockProviders = new ArrayList<>();
		this.blockMappers = new ArrayList<>();
		this.shapes = new ArrayList<>();
		this.hunks = new ArrayList<>();
		this.models = new ArrayList<>();
	}
	
	public SceneReferences(Scene scene, JsonObj src) {
		this.blockProviders = src.getList("blockProviders").toMutableList(scene::createBlockProvider);
		this.blockMappers = src.getList("blockMappers").toMutableList(scene::createBlockMapper);
		this.shapes = src.getList("shapes").toMutableList(scene::createShape);
		this.hunks = src.getList("hunks").toMutableList(scene::createHunk);
		this.models = src.getList("models").toMutableList(scene::createModel);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("blockProviders", JsonObj.newList(blockProviders))
			.set("blockMappers", JsonObj.newList(blockMappers))
			.set("shapes", JsonObj.newList(shapes))
			.set("hunks", JsonObj.newList(hunks))
			.set("models", JsonObj.newList(models));
	}
	
}
