package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.StringNodeValue;
import com.ferreusveritas.resources.Resources;
import com.ferreusveritas.resources.model.qsp.QspModel;
import com.ferreusveritas.resources.model.qsp.QspModelPart;

import java.util.UUID;

/**
 * A Shape that uses a Model
 */
public class ModelShape extends Shape {
	
	public static final String MODEL = "model";
	public static final String PART = "part";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType(MODEL)
		.loaderClass(Loader.class)
		.sceneObjectClass(ModelShape.class)
		.value(new StringNodeValue.Builder(MODEL).build())
		.value(new StringNodeValue.Builder(PART).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final QspModelPart model;
	private final AABBD bounds;
	
	private ModelShape(UUID uuid, QspModelPart model) {
		super(uuid);
		this.model = model;
		this.bounds = model.getAABB();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return bounds.contains(pos) && model.pointIsInside(pos) ? 1.0 : 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final String model;
		private final String part;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MODEL) String model,
			@JsonProperty(PART) String part
		) {
			super(uuid);
			this.model = model;
			this.part = part;
		}
		
		protected Shape create() {
			QspModelPart qspModelPart = Resources.load(model, QspModel.class).getPart(part);
			return new ModelShape(
				getUuid(),
				qspModelPart
			);
		}
		
	}
	
}
