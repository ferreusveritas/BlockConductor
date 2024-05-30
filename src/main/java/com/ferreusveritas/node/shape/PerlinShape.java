package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.NumberNodeValue;
import org.spongepowered.noise.module.source.Perlin;
import org.spongepowered.noise.module.source.Simplex;

import java.util.UUID;

/**
 * A Shape that returns the perlin noise value at a given position
 */
public class PerlinShape extends Shape {
	
	public static final String FREQUENCY = "frequency";
	public static final String LACUNARITY = "lacunarity";
	public static final String PERSISTENCE = "persistence";
	public static final String OCTAVES = "octaves";
	public static final String SEED = "seed";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("perlin")
		.loaderClass(Loader.class)
		.sceneObjectClass(PerlinShape.class)
		.value(new NumberNodeValue.Builder(FREQUENCY).def(Simplex.DEFAULT_SIMPLEX_FREQUENCY).build())
		.value(new NumberNodeValue.Builder(LACUNARITY).def(Simplex.DEFAULT_SIMPLEX_LACUNARITY).build())
		.value(new NumberNodeValue.Builder(PERSISTENCE).def(Simplex.DEFAULT_SIMPLEX_PERSISTENCE).build())
		.value(new NumberNodeValue.Builder(OCTAVES).def(Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT).min(1).max(Simplex.SIMPLEX_MAX_OCTAVE).increment(1).build())
		.value(new NumberNodeValue.Builder(SEED).def(Simplex.DEFAULT_SIMPLEX_SEED).integer().build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Perlin perlin;
	private final double frequency;
	private final double lacunarity;
	private final double persistence;
	private final int octaves;
	private final int seed;
	
	private PerlinShape(UUID uuid, double frequency, double lacunarity, double persistence, int octaves, int seed) {
		super(uuid);
		this.frequency = frequency;
		this.lacunarity = lacunarity;
		this.persistence = persistence;
		this.octaves = octaves;
		this.seed = seed;
		this.perlin = setupPerlin();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private Perlin setupPerlin() {
		Perlin p = new Perlin();
		p.setFrequency(frequency);
		p.setLacunarity(lacunarity);
		p.setPersistence(persistence);
		p.setOctaveCount(octaves);
		p.setSeed(seed);
		return p;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return perlin.get(pos.x(), pos.y(), pos.z());
	}

	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double frequency;
		private final double lacunarity;
		private final double persistence;
		private final int octaves;
		private final int seed;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(FREQUENCY) Double frequency,
			@JsonProperty(LACUNARITY) Double lacunarity,
			@JsonProperty(PERSISTENCE) Double persistence,
			@JsonProperty(OCTAVES) Integer octaves,
			@JsonProperty(SEED) Integer seed
		) {
			super(uuid);
			this.frequency = frequency;
			this.lacunarity = lacunarity;
			this.persistence = persistence;
			this.octaves = octaves;
			this.seed = seed;
		}
		
		protected Shape create() {
			return new PerlinShape(
				getUuid(),
				frequency,
				lacunarity,
				persistence,
				octaves,
				seed
			);
		}
		
	}
	
}
