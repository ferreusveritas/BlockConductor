package com.ferreusveritas.resources;

import java.io.InputStream;

public interface ResourceLoader<T> {

	T load(InputStream stream);

}
