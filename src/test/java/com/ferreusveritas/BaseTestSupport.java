package com.ferreusveritas;

import java.io.IOException;
import java.io.InputStream;

public class BaseTestSupport {
	
	public static String readResourceAsString(String path) {
		InputStream is = readResourceAsStream(path);
		if(is == null) {
			throw new RuntimeException("Could not find resource: " + path);
		}
		try(is) {
			return new String(is.readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static InputStream readResourceAsStream(String path) {
		return BaseTestSupport.class.getClassLoader().getResourceAsStream(path);
	}
	
}
