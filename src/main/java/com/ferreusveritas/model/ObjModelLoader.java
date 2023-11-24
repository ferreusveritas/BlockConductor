package com.ferreusveritas.model;

import com.ferreusveritas.math.Vec2D;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.support.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjModelLoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(ObjModelLoader.class);
	private static final boolean INVERT_Z = true;
	private static final boolean SWAP_YZ = false;
	
	private List<Vec3D> vertices = new ArrayList<>();
	private List<Vec2D> textureCoordinates = new ArrayList<>();
	private List<Vec3D> normals = new ArrayList<>();
	private final List<FullFace> faces = new ArrayList<>();
	
	public static Optional<ObjModel> load(String path) {
		try {
			InputStream stream = Storage.getInputStream(path);
			return load(stream);
		} catch (Exception e) {
			LOG.error("Failed to load model: {}", path, e);
			return Optional.empty();
		}
	}
	
	public static Optional<ObjModel> load(InputStream stream) {
		InputStreamReader reader = new InputStreamReader(stream);
		return load(reader);
	}
	
	public static Optional<ObjModel> load(Reader reader) {
		ObjModelLoader loader = new ObjModelLoader(reader);
		ObjModel model = new ObjModel(loader.getFaces());
		return Optional.of(model);
	}
	
	private ObjModelLoader(Reader reader) {
		try (BufferedReader bufferedReader = new BufferedReader(reader)) {
			String line;
			while((line = bufferedReader.readLine()) != null) {
				String[] lineParts = line.split(" ");
				processLine(lineParts);
			}
		} catch (IOException e) {
			LOG.error("Error reading model data", e);
		} finally {
			vertices = null;
			textureCoordinates = null;
			normals = null;
		}
	}
	
	private void processLine(String[] lineParts) {
		switch(lineParts[0]) {
			case "v":
				processVertex(lineParts);
				break;
			case "vt":
				processTextureCoordinate(lineParts);
				break;
			case "vn":
				processNormal(lineParts);
				break;
			case "f":
				processFace(lineParts);
				break;
			default:
				break;
		}
	}
	
	private void processVertex(String[] lineParts) {
		Vec3D normal = getVertex(lineParts);
		vertices.add(normal);
	}
	
	private void processTextureCoordinate(String[] lineParts) {
		Vec2D textureCoordinate = getTextureCoord(lineParts);
		textureCoordinates.add(textureCoordinate);
	}
	
	private void processNormal(String[] lineParts) {
		Vec3D normal = getVertex(lineParts);
		normals.add(normal);
	}
	
	private Vec3D getVertex(String[] vertexParts) {
		Vec3D v = new Vec3D(
			Double.parseDouble(vertexParts[1]),
			Double.parseDouble(vertexParts[2]),
			Double.parseDouble(vertexParts[3])
		);
		if(INVERT_Z) {
			v = invertZ(v);
		}
		if(SWAP_YZ) { // Swap Y and Z to convert from Blender to Minecraft coordinate system
			v = swapYZ(v);
		}
		return v;
	}
	
	private Vec2D getTextureCoord(String[] vertexParts) {
		return new Vec2D(
			Double.parseDouble(vertexParts[1]),
			Double.parseDouble(vertexParts[2])
		);
	}
	
	private void processFace(String[] lineParts) {
		Vec3D[] faceVertices = new Vec3D[3];
		Vec2D[] textureTemp = new Vec2D[3];
		Vec3D[] normalTemp = new Vec3D[3];
		for(int i = 0; i < 3; i++) {
			String[] vertexParts = lineParts[i + 1].split("/");
			faceVertices[i] = getIndexOrZero(vertexParts[0], vertices, Vec3D.ZERO);
			textureTemp[i] = getIndexOrZero(vertexParts[1], textureCoordinates, Vec2D.ZERO);
			normalTemp[i] = getIndexOrZero(vertexParts[2], normals, Vec3D.ZERO);
		}
		FullFace face = new FullFace(faceVertices, textureTemp, normalTemp);
		faces.add(face);
	}
	
	private static <T> T getIndexOrZero(String strIndex, List<T> coordinates, T zero) {
		if(strIndex.isEmpty()) {
			return zero;
		}
		int index;
		try {
			index = Integer.parseInt(strIndex) - 1;
		} catch(NumberFormatException e) {
			index = -1;
		}
		if(index < 0 || index >= coordinates.size()) {
			return zero;
		}
		return coordinates.get(index);
	}
	
	private static Vec3D swapYZ(Vec3D vec) {
		return new Vec3D(vec.x(), vec.z(), vec.y());
	}
	
	private static Vec3D invertZ(Vec3D vec) {
		return new Vec3D(vec.x(), vec.y(), -vec.z());
	}
	
	public List<FullFace> getFaces() {
		return faces;
	}
	
}
