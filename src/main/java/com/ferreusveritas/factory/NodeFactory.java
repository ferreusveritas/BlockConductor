package com.ferreusveritas.factory;

import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.mapper.BlockMapper;
import com.ferreusveritas.node.mapper.IdentityBlockMapper;
import com.ferreusveritas.node.mapper.ReferenceBlockMapper;
import com.ferreusveritas.node.mapper.SimpleBlockMapper;
import com.ferreusveritas.node.model.MeshModel;
import com.ferreusveritas.node.model.Model;
import com.ferreusveritas.node.model.ReferenceModel;
import com.ferreusveritas.node.provider.*;
import com.ferreusveritas.node.shape.*;
import com.ferreusveritas.node.transform.*;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;

public class NodeFactory {
	
	private final Map<String, NodeTypeFactory> factoryMap = new MapBuilder()
		.add(Shape.class, createShapeFactory())
		.add(BlockMapper.class, createBlockMapperFactory())
		.add(BlockProvider.class, createBlockProviderFactory())
		.add(Model.class, createModelFactory())
		.add(Transform.class, createTransformFactory())
		.build();
	
	public NodeLoader create(LoaderSystem loaderSystem, JsonObj src) {
		String type = src.getString("type").orElseThrow(() -> new RuntimeException("Missing type: " + src));
		String className = src.getString("class").map(String::toLowerCase).orElseThrow(() -> new RuntimeException("Missing class for type: " + type + ", src:" + src));
		
		NodeTypeFactory factory = factoryMap.get(className);
		if(factory == null) {
			throw new InvalidJsonProperty("Unknown class: " + className);
		}
		return factory.create(loaderSystem, src);
	}
	
	private NodeTypeFactory createShapeFactory() {
		return new NodeTypeFactory.Builder()
			.add(BlendShape.TYPE, BlendShape.Loader::new)
			.add(CacheShape.TYPE, CacheShape.Loader::new)
			.add(CavitateShape.TYPE, CavitateShape.Loader::new)
			.add(CheckerShape.TYPE, CheckerShape.Loader::new)
			.add(ClipShape.TYPE, ClipShape.Loader::new)
			.add(CombineShape.TYPE, CombineShape.Loader::new)
			.add(ConstantShape.TYPE, ConstantShape.Loader::new)
			.add(CubeShape.TYPE, CubeShape.Loader::new)
			.add(CurveShape.TYPE, CurveShape.Loader::new)
			.add(CylinderShape.TYPE, CylinderShape.Loader::new)
			.add(GradientShape.TYPE, GradientShape.Loader::new)
			.add(HeightMapShape.TYPE, HeightMapShape.Loader::new)
			.add(ImageShape.TYPE, ImageShape.Loader::new)
			.add(LayerShape.TYPE, LayerShape.Loader::new)
			.add(ModelShape.TYPE, ModelShape.Loader::new)
			.add(PerlinShape.TYPE, PerlinShape.Loader::new)
			.add(PlaneShape.TYPE, PlaneShape.Loader::new)
			.add(PyramidShape.TYPE, PyramidShape.Loader::new)
			.add(RadialGradientShape.TYPE, RadialGradientShape.Loader::new)
			.add(ReferenceShape.TYPE, ReferenceShape.Loader::new)
			.add(SimplexShape.TYPE, SimplexShape.Loader::new)
			.add(SliceShape.TYPE, SliceShape.Loader::new)
			.add(SphereShape.TYPE, SphereShape.Loader::new)
			.add(TransformShape.TYPE, TransformShape.Loader::new)
			.add(TranslateShape.TYPE, TranslateShape.Loader::new)
			.add(VoidShape.TYPE, VoidShape.Loader::new)
			.build();
	}
	
	private NodeTypeFactory createBlockMapperFactory() {
		return new NodeTypeFactory.Builder()
			.add(IdentityBlockMapper.TYPE, IdentityBlockMapper.Loader::new)
			.add(SimpleBlockMapper.TYPE, SimpleBlockMapper.Loader::new)
			.add(ReferenceBlockMapper.TYPE, ReferenceBlockMapper.Loader::new)
			.build();
	}
	
	private NodeTypeFactory createBlockProviderFactory() {
		return new NodeTypeFactory.Builder()
			.add(CombineBlockProvider.TYPE, CombineBlockProvider.Loader::new)
			.add(MapperBlockProvider.TYPE, MapperBlockProvider.Loader::new)
			.add(RoutingBlockProvider.TYPE, RoutingBlockProvider.Loader::new)
			.add(ShapeBlockProvider.TYPE, ShapeBlockProvider.Loader::new)
			.add(SolidBlockProvider.TYPE, SolidBlockProvider.Loader::new)
			.add(TerrainBlockProvider.TYPE, TerrainBlockProvider.Loader::new)
			.add(TranslateBlockProvider.TYPE, TranslateBlockProvider.Loader::new)
			.add(ReferenceBlockProvider.TYPE, ReferenceBlockProvider.Loader::new)
			.build();
	}
	
	private NodeTypeFactory createModelFactory() {
		return new NodeTypeFactory.Builder()
			.add(MeshModel.TYPE, MeshModel.Loader::new)
			.add(ReferenceModel.TYPE, ReferenceModel.Loader::new)
			.build();
	}
	
	private NodeTypeFactory createTransformFactory() {
		return new NodeTypeFactory.Builder()
			.add(Identity.TYPE, Identity.Loader::new)
			.add(Matrix.TYPE, Matrix.Loader::new)
			.add(ReferenceTransform.TYPE, ReferenceTransform.Loader::new)
			.add(RotateX.TYPE, RotateX.Loader::new)
			.add(RotateY.TYPE, RotateY.Loader::new)
			.add(RotateZ.TYPE, RotateZ.Loader::new)
			.add(Scale.TYPE, Scale.Loader::new)
			.add(Transforms.TYPE, Transforms.Loader::new)
			.add(Translate.TYPE, Translate.Loader::new)
			.build();
	}
	
	private static class MapBuilder {
		
		private final Map<String, NodeTypeFactory> factoryMap = new HashMap<>();
		
		public MapBuilder add(Class<? extends Node> type, NodeTypeFactory factory) {
			String className = type.getSimpleName().toLowerCase();
			factoryMap.put(className, factory);
			return this;
		}
		
		public Map<String, NodeTypeFactory> build() {
			return Map.copyOf(factoryMap);
		}
		
	}
	
}
