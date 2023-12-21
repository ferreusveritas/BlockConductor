package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;
import org.spongepowered.noise.module.source.Perlin;

import java.util.UUID;

/**
 * A Shape that returns the perlin noise value at a given position
 */
public class PerlinShape extends Shape {
	
	public static final String TYPE = "perlin";
	public static final String FREQUENCY = "frequency";
	public static final String LACUNARITY = "lacunarity";
	public static final String PERSISTENCE = "persistence";
	public static final String OCTAVES = "octaves";
	public static final String SEED = "seed";
	
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
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return perlin.get(pos.x(), pos.y(), pos.z());
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(FREQUENCY, frequency)
			.set(LACUNARITY, lacunarity)
			.set(PERSISTENCE, persistence)
			.set(OCTAVES, octaves)
			.set(SEED, seed);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		private UUID uuid = UUID.randomUUID();
		private double frequency = Perlin.DEFAULT_PERLIN_FREQUENCY;
		private double lacunarity = Perlin.DEFAULT_PERLIN_LACUNARITY;
		private double persistence = Perlin.DEFAULT_PERLIN_PERSISTENCE;
		private int octaves = Perlin.DEFAULT_PERLIN_OCTAVE_COUNT;
		private int seed = Perlin.DEFAULT_PERLIN_SEED;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder frequency(double frequency) {
			this.frequency = frequency;
			return this;
		}
		
		public Builder lacunarity(double lacunarity) {
			this.lacunarity = lacunarity;
			return this;
		}
		
		public Builder persistence(double persistence) {
			this.persistence = persistence;
			return this;
		}
		
		public Builder octaves(int octaves) {
			this.octaves = octaves;
			return this;
		}
		
		public Builder seed(int seed) {
			this.seed = seed;
			return this;
		}
		
		public PerlinShape build() {
			return new PerlinShape(uuid, frequency, lacunarity, persistence, octaves, seed);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final double frequency;
		private final double lacunarity;
		private final double persistence;
		private final int octaves;
		private final int seed;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.frequency = src.getDouble(FREQUENCY).orElse(Perlin.DEFAULT_PERLIN_FREQUENCY);
			this.lacunarity = src.getDouble(LACUNARITY).orElse(Perlin.DEFAULT_PERLIN_LACUNARITY);
			this.persistence = src.getDouble(PERSISTENCE).orElse(Perlin.DEFAULT_PERLIN_PERSISTENCE);
			this.octaves = src.getInt(OCTAVES).orElse(Perlin.DEFAULT_PERLIN_OCTAVE_COUNT);
			this.seed = src.getInt(SEED).orElse(Perlin.DEFAULT_PERLIN_SEED);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new PerlinShape(getUuid(), frequency, lacunarity, persistence, octaves, seed);
		}
		
	}
	
}
