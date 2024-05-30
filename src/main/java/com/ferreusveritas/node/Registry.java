package com.ferreusveritas.node;

import com.ferreusveritas.node.mapper.IdentityBlockMapper;
import com.ferreusveritas.node.mapper.SimpleBlockMapper;
import com.ferreusveritas.node.ports.PortDataType;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.provider.*;
import com.ferreusveritas.node.shape.*;
import com.ferreusveritas.node.transform.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry {
	
	private static final Map<String, NodeRegistryData> NODE_CLASSES = new HashMap<>();
	private static final Map<Class<?>, NodeRegistryData> NODE_CLASSES_BY_CLASS = new HashMap<>();
	private static final Map<String, PortDataType<?>> PORT_DATA_TYPES = new HashMap<>();
	
	public static void register(NodeRegistryData registryData) {
		NODE_CLASSES.put(registryData.type(), registryData);
		NODE_CLASSES_BY_CLASS.put(registryData.loaderClass(), registryData);
	}
	
	public static void register(PortDataType<?> portDataType) {
		PORT_DATA_TYPES.put(portDataType.name(), portDataType);
	}
	
	public static NodeRegistryData getRegistryData(String type) {
		NodeRegistryData registryData = NODE_CLASSES.get(type);
		if(registryData == null) {
			throw new IllegalArgumentException("Unknown node type: " + type);
		}
		return registryData;
	}
	
	public static NodeRegistryData getRegistryData(Class<?> clazz) {
		NodeRegistryData registryData = NODE_CLASSES_BY_CLASS.get(clazz);
		if(registryData == null) {
			throw new IllegalArgumentException("Unknown node class: " + clazz);
		}
		return registryData;
	}
	
	public static List<NodeRegistryData> getRegistryData() {
		return new ArrayList<>(NODE_CLASSES.values());
	}
	
	static {
		register(PortDataTypes.BLOCKS);
		register(PortDataTypes.SHAPE);
		register(PortDataTypes.MAPPER);
		register(PortDataTypes.TRANSFORM);
		register(PortDataTypes.BOOLEAN);
		
		register(BlendShape.REGISTRY_DATA);
		register(CacheShape.REGISTRY_DATA);
		register(CavitateShape.REGISTRY_DATA);
		register(CheckerShape.REGISTRY_DATA);
		register(ClipShape.REGISTRY_DATA);
		register(CombineShape.REGISTRY_DATA);
		register(ConstantShape.REGISTRY_DATA);
		register(CubeShape.REGISTRY_DATA);
		register(CurveShape.REGISTRY_DATA);
		register(CylinderShape.REGISTRY_DATA);
		register(GradientShape.REGISTRY_DATA);
		register(GrowShape.REGISTRY_DATA);
		register(HeightMapShape.REGISTRY_DATA);
		register(ImageShape.REGISTRY_DATA);
		register(LayerShape.REGISTRY_DATA);
		register(ModelShape.REGISTRY_DATA);
		register(PerlinShape.REGISTRY_DATA);
		register(PlaneShape.REGISTRY_DATA);
		register(PyramidShape.REGISTRY_DATA);
		register(RadialGradientShape.REGISTRY_DATA);
		register(SimplexShape.REGISTRY_DATA);
		register(SliceShape.REGISTRY_DATA);
		register(SphereShape.REGISTRY_DATA);
		register(TransformShape.REGISTRY_DATA);
		register(TranslateShape.REGISTRY_DATA);
		register(VoidShape.REGISTRY_DATA);
		
		register(CombineBlockProvider.REGISTRY_DATA);
		register(MapperBlockProvider.REGISTRY_DATA);
		register(ReferenceBlockProvider.REGISTRY_DATA);
		register(RouteBlockProvider.REGISTRY_DATA);
		register(ShapeBlockProvider.REGISTRY_DATA);
		register(SolidBlockProvider.REGISTRY_DATA);
		register(TerrainBlockProvider.REGISTRY_DATA);
		register(TranslateBlockProvider.REGISTRY_DATA);
		
		register(Identity.REGISTRY_DATA);
		register(Matrix.REGISTRY_DATA);
		register(RotateX.REGISTRY_DATA);
		register(RotateY.REGISTRY_DATA);
		register(RotateZ.REGISTRY_DATA);
		register(Scale.REGISTRY_DATA);
		register(Transforms.REGISTRY_DATA);
		register(Translate.REGISTRY_DATA);
		
		register(IdentityBlockMapper.REGISTRY_DATA);
		register(SimpleBlockMapper.REGISTRY_DATA);
	}
	
	private Registry() {
		throw new UnsupportedOperationException("This class cannot be instantiated");
	}
	
}
