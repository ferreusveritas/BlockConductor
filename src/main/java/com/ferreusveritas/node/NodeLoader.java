package com.ferreusveritas.node;

import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class NodeLoader {
	
	public static final Logger LOG = org.slf4j.LoggerFactory.getLogger(NodeLoader.class);
	
	private final UUID uuid;
	private Node loaded;
	
	protected NodeLoader(LoaderSystem loaderSystem, JsonObj src) {
		this.uuid = src.getString("uuid").map(UUID::fromString).orElseGet(UUID::randomUUID);
		if(loaderSystem.get(uuid).isPresent()) {
			throw new InvalidJsonProperty("Duplicate UUID: " + uuid);
		}
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public <T extends Node> Optional<T> load(LoaderSystem loaderSystem, Class<T> clazz) {
		if(loaded == null) {
			this.loaded = load(loaderSystem);
			loaderSystem.put(uuid, loaded);
		}
		if(clazz.isInstance(loaded)) {
			try {
				return Optional.of(clazz.cast(loaded));
			} catch (ClassCastException e) {
				return Optional.empty();
			}
		}
		LOG.warn("Loaded node {} is not of type {}", uuid, clazz.getSimpleName());
		return Optional.empty();
	}
	protected abstract Node load(LoaderSystem loaderSystem);
	
	protected Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
	protected Supplier<InvalidJsonProperty> wrongType(String name) {
		return () -> new InvalidJsonProperty("Wrong type for " + name);
	}
	
}
