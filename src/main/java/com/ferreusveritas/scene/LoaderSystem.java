package com.ferreusveritas.scene;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.factory.NodeFactory;
import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class LoaderSystem {
	
	private final NodeFactory nodeFactory;
	private final Map<UUID, Node> nodes = new HashMap<>();
	
	public LoaderSystem(NodeFactory nodeFactory) {
		this.nodeFactory = nodeFactory;
	}
	
	public void put(UUID uuid, Node obj) {
		nodes.put(uuid, obj);
	}
	
	public <T extends Node> Optional<T> get(UUID uuid, Class<T> clazz) {
		return Optional.ofNullable(nodes.get(uuid)).filter(clazz::isInstance).map(clazz::cast);
	}
	
	public Map<UUID, Node> getNodes() {
		return Map.copyOf(nodes);
	}
	
	private final Map<UUID, NodeLoader> loaders = new HashMap<>();
	
	public NodeLoader createLoader(JsonObj src) {
		NodeLoader loader = nodeFactory.create(this, src);
		loaders.put(loader.getUuid(), loader);
		return loader;
	}
	
	public Optional<NodeLoader> getLoader(UUID uuid) {
		return Optional.ofNullable(loaders.get(uuid));
	}
	
	public NodeLoader loader(JsonObj src, String prop) {
		return src.getObj(prop).map(this::createLoader).orElseThrow(missing(prop));
	}
	
	
	////////////////////////////////////////////////////////////////
	// Misc
	////////////////////////////////////////////////////////////////
	
	private final BlockCache blockCache = new BlockCache();
	
	public Block blockLoader(JsonObj src, String prop) {
		return blockCache.resolve(src.getObj(prop).orElseThrow());
	}
	
	public Block blockLoader(JsonObj src) {
		return blockCache.resolve(src);
	}
	
	public Block block(Block block) {
		return blockCache.resolve(block);
	}
	
	protected Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
}
