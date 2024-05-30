package com.ferreusveritas.resources;

import com.ferreusveritas.resources.image.ImageLoader;
import com.ferreusveritas.resources.model.qsp.QspModel;
import com.ferreusveritas.resources.model.qsp.QspModelLoader;
import com.ferreusveritas.support.storage.Storage;
import com.ferreusveritas.support.storage.StorageException;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Resources {

	private static final Map<Class<?>, ResourceLoader<?>> loaders = createLoaders();
	private static final Map<String, Object> cache = new HashMap<>();
	
	private static Map<Class<?>, ResourceLoader<?>> createLoaders() {
		Map<Class<?>, ResourceLoader<?>> loaders = new HashMap<>();
		loaders.put(BufferedImage.class, new ImageLoader());
		loaders.put(QspModel.class, new QspModelLoader());
		return Map.copyOf(loaders);
	}
	
	public static <T> T load(String path, Class<T> type) {
		
		if(cache.containsKey(path)) {
			Object obj = cache.get(path);
			if(obj != null) {
				if (type.isInstance(obj)) {
					return type.cast(obj);
				}
				throw new ResourceLoaderException("Cached object is not of the correct type: " + type.getName() + " for path: " + path);
			}
		}
		
		ResourceLoader<T> loader = (ResourceLoader<T>) loaders.get(type);
		if(loader == null) {
			throw new ResourceLoaderException("No loader found for type: " + type.getName());
		}
		
		InputStream inputStream;
		
		try {
			inputStream = Storage.getInputStream(path);
		} catch (StorageException e) {
			throw new ResourceLoaderException("Failed to create stream from path: " + path, e);
		}
		
		try {
			T obj = loader.load(inputStream);
			cache.put(path, obj);
			return obj;
		} catch (ResourceLoaderException e) {
			throw new ResourceLoaderException("Failed to load resource from stream of path: " + path, e);
		}

	}
	
}
