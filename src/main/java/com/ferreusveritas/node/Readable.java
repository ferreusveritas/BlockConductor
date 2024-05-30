package com.ferreusveritas.node;

import java.util.Optional;

public interface Readable<T> {

	T read();
	
	default Optional<T> readOpt() {
		return Optional.ofNullable(read());
	}
	
	Class<T> getType();

}
