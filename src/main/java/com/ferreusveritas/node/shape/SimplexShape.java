package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;
import org.spongepowered.noise.module.source.Simplex;

import java.util.UUID;

/**
 * A Shape that returns the simplex noise value at a given position
 */
public class SimplexShape extends Shape {
	
	public static final String TYPE = "simplex";
	public static final String FREQUENCY = "frequency";
	public static final String LACUNARITY = "lacunarity";
	public static final String PERSISTENCE = "persistence";
	public static final String OCTAVES = "octaves";
	public static final String SEED = "seed";
	
	private final Simplex simplex;
	private final double frequency;
	private final double lacunarity;
	private final double persistence;
	private final int octaves;
	private final int seed;
	
	private SimplexShape(UUID uuid, double frequency, double lacunarity, double persistence, int octaves, int seed) {
		super(uuid);
		this.frequency = frequency;
		this.lacunarity = lacunarity;
		this.persistence = persistence;
		this.octaves = octaves;
		this.seed = seed;
		this.simplex = setupSimplex();
	}
	
	private Simplex setupSimplex() {
		Simplex s = new Simplex();
		s.setFrequency(frequency);
		s.setLacunarity(lacunarity);
		s.setPersistence(persistence);
		s.setOctaveCount(octaves);
		s.setSeed(seed);
		return s;
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
		return simplex.get(pos.x(), pos.y(), pos.z());
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
		private double frequency = Simplex.DEFAULT_SIMPLEX_FREQUENCY;
		private double lacunarity = Simplex.DEFAULT_SIMPLEX_LACUNARITY;
		private double persistence = Simplex.DEFAULT_SIMPLEX_PERSISTENCE;
		private int octaves = Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT;
		private int seed = Simplex.DEFAULT_SIMPLEX_SEED;
		
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
		
		public SimplexShape build() {
			return new SimplexShape(uuid, frequency, lacunarity, persistence, octaves, seed);
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
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.frequency = src.getDouble(FREQUENCY).orElse(Simplex.DEFAULT_SIMPLEX_FREQUENCY);
			this.lacunarity = src.getDouble(LACUNARITY).orElse(Simplex.DEFAULT_SIMPLEX_LACUNARITY);
			this.persistence = src.getDouble(PERSISTENCE).orElse(Simplex.DEFAULT_SIMPLEX_PERSISTENCE);
			this.octaves = src.getInt(OCTAVES).orElse(Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT);
			this.seed = src.getInt(SEED).orElse(Simplex.DEFAULT_SIMPLEX_SEED);
			
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new SimplexShape(getUuid(), frequency, lacunarity, persistence, octaves, seed);
		}
		
	}
	
}
