package com.ferreusveritas.scene;

import com.ferreusveritas.factory.NodeFactory;
import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.*;

public class Scene implements Jsonable {
	
	public static final String DEFS = "defs";
	public static final String ROOT = "root";
	
	private static final NodeFactory nodeFactory = new NodeFactory();
	
	private final Map<UUID, Node> nodes = new HashMap<>();
	private final Map<UUID, Node> defs = new HashMap<>();
	private Node root;
	
	public Scene() {
	}
	
	public Scene(JsonObj src) {
		LoaderSystem loaderSystem = new LoaderSystem(nodeFactory);
		List<NodeLoader> defLoaders = src.getObj(DEFS).orElseGet(JsonObj::newList).toImmutableList(loaderSystem::createLoader);
		NodeLoader rootLoader = src.getObj(ROOT).or(() -> Optional.of(src)).map(loaderSystem::createLoader).orElseThrow();
		defLoaders.stream().map(loader -> loader.load(loaderSystem, Node.class).orElseThrow()).forEach(node -> defs.put(node.getUuid(), node));
		this.root = rootLoader.load(loaderSystem, Node.class).orElseThrow();
		this.nodes.putAll(loaderSystem.getNodes());
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public <T extends Node> Optional<T> getRoot(Class<T> clazz) {
		return Optional.ofNullable(root).filter(clazz::isInstance).map(clazz::cast);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set(DEFS, defs)
			.set(ROOT, root);
	}
	
}
