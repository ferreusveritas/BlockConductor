package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import org.spongepowered.noise.module.source.Simplex;

public class SimplexHunk extends Hunk {
	
	public static final String TYPE = "perlin";
	
	private final Simplex simplex;
	private final double frequency;
	private final double lacunarity;
	private final double persistence;
	private final int octaves;
	private final int seed;
	
	private SimplexHunk(Scene scene, double frequency, double lacunarity, double persistence, int octaves, int seed) {
		super(scene);
		this.frequency = frequency;
		this.lacunarity = lacunarity;
		this.persistence = persistence;
		this.octaves = octaves;
		this.seed = seed;
		this.simplex = setupSimplex();
	}
	
	public SimplexHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.frequency = src.getDouble("frequency").orElse(Simplex.DEFAULT_SIMPLEX_FREQUENCY);
		this.lacunarity = src.getDouble("lacunarity").orElse(Simplex.DEFAULT_SIMPLEX_LACUNARITY);
		this.persistence = src.getDouble("persistence").orElse(Simplex.DEFAULT_SIMPLEX_PERSISTENCE);
		this.octaves = src.getInt("octaves").orElse(Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT);
		this.seed = src.getInt("seed").orElse(Simplex.DEFAULT_SIMPLEX_SEED);
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
	public AABBI bounds() {
		return AABBI.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return simplex.get(pos.x(), pos.y(), pos.z());
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("frequency", frequency)
			.set("lacunarity", lacunarity)
			.set("persistence", persistence)
			.set("octaves", octaves)
			.set("seed", seed);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		private double frequency = Simplex.DEFAULT_SIMPLEX_FREQUENCY;
		private double lacunarity = Simplex.DEFAULT_SIMPLEX_LACUNARITY;
		private double persistence = Simplex.DEFAULT_SIMPLEX_PERSISTENCE;
		private int octaves = Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT;
		private int seed = Simplex.DEFAULT_SIMPLEX_SEED;
		
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
		
		public SimplexHunk build(Scene scene) {
			return new SimplexHunk(scene, frequency, lacunarity, persistence, octaves, seed);
		}
		
	}
	
}
