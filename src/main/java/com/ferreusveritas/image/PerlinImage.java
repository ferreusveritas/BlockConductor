package com.ferreusveritas.image;

import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Pixel;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import org.spongepowered.noise.module.source.Perlin;

public class PerlinImage extends Image {
	
	public static final String TYPE = "perlin";
	
	private final Perlin perlin;
	private final double frequency;
	private final double lacunarity;
	private final double persistence;
	private final int octaves;
	private final int seed;
	
	private PerlinImage(Scene scene, double frequency, double lacunarity, double persistence, int octaves, int seed) {
		super(scene);
		this.frequency = frequency;
		this.lacunarity = lacunarity;
		this.persistence = persistence;
		this.octaves = octaves;
		this.seed = seed;
		this.perlin = setupPerlin();
	}
	
	public PerlinImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.frequency = src.getDouble("frequency").orElse(Perlin.DEFAULT_PERLIN_FREQUENCY);
		this.lacunarity = src.getDouble("lacunarity").orElse(Perlin.DEFAULT_PERLIN_LACUNARITY);
		this.persistence = src.getDouble("persistence").orElse(Perlin.DEFAULT_PERLIN_PERSISTENCE);
		this.octaves = src.getInt("octaves").orElse(Perlin.DEFAULT_PERLIN_OCTAVE_COUNT);
		this.seed = src.getInt("seed").orElse(Perlin.DEFAULT_PERLIN_SEED);
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
	public RectI bounds() {
		return RectI.INFINITE;
	}
	
	@Override
	public Pixel getPixel(int x, int y) {
		double val = perlin.get(x, y, 0.0);
		val = val * 0.90;
		val = MathHelper.clamp(val, 0.0, 1.0);
		return new Pixel(val, 0.0, 0.0, 1.0);
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
		private double frequency = Perlin.DEFAULT_PERLIN_FREQUENCY;
		private double lacunarity = Perlin.DEFAULT_PERLIN_LACUNARITY;
		private double persistence = Perlin.DEFAULT_PERLIN_PERSISTENCE;
		private int octaves = Perlin.DEFAULT_PERLIN_OCTAVE_COUNT;
		private int seed = Perlin.DEFAULT_PERLIN_SEED;
		
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
		
		public PerlinImage build(Scene scene) {
			return new PerlinImage(scene, frequency, lacunarity, persistence, octaves, seed);
		}
		
	}
	
}
