/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Copyright (C) 2005 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.modules.generation;

import ca.grimoire.jnoise.modules.CompositeModule;
import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.basic.Gradient.Quality;
import ca.grimoire.jnoise.modules.composition.Add;
import ca.grimoire.jnoise.modules.map.Bias;
import ca.grimoire.jnoise.modules.transform.Scale;
import ca.grimoire.jnoise.util.Hash;

/**
 * Noise module generating a Perlin noise pattern. The noise is composed of a
 * number of octaves of higher- and higher-frequency gradient noise of smaller
 * and smaller amplitude overlaid on each other.
 * <p>
 * If you are using the included XML noise configuration system, Perlin modules
 * can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;perlin seed="<var>seed</var>" octaves="<var>octaves</var>" lacunarity="<var>lacunarity</var>" persistence="<var>persistence</var>" quality="<var>QUALITY</var>" /&gt;</code>
 * </blockquote>
 * <p>
 * The <var>QUALITY</var> is one of <kbd>LOW</kbd>, <kbd>MEDIUM</kbd>, or
 * <kbd>HIGH</kbd>
 * 
 * @see jnoise.modules.basic.Gradient
 */
public final class Perlin implements CompositeModule {

  private static final Add createPerlinChain (int seed, int octaves,
      double lacunarity, double persistence, Gradient.Quality quality) {
    Module[] parts = new Module[octaves];

    for (int octave = 0; octave < octaves; ++octave)
      parts[octave] = createPerlinOctave (octave, seed, lacunarity,
          persistence, quality);

    return new Add (parts);
  }

  private static Scale createPerlinOctave (int octave, int seed,
      double lacunarity, double persistence, Quality quality) {
    double frequency = Math.pow (lacunarity, octave);
    double amplitude = Math.pow (persistence, octave);

    Gradient generator = new Gradient (seed + octave, quality);
    return new Scale (new Bias (generator, amplitude), frequency);
  }

  /**
   * Creates a new perlin noise generator.
   * 
   * @param seed
   *          the seed to use as a base for gradient generation.
   * @param octaves
   *          the number of octaves of noise to produce.
   * @param lacunarity
   *          the per-octave frequency multiplier.
   * @param persistence
   *          the per-octave amplitude multiplier.
   * @param quality
   *          the quality level of the underlying gradient noise.
   */
  public Perlin (int seed, int octaves, double lacunarity, double persistence,
      Gradient.Quality quality) {
    assert (octaves >= 0);
    assert (quality != null);

    this.quality = quality;
    this.persistence = persistence;
    this.lacunarity = lacunarity;
    this.octaves = octaves;
    this.seed = seed;

    this.generators = new Gradient[octaves];
    for (int octave = 0; octave < octaves; ++octave)
      generators[octave] = new Gradient (seed + octave, quality);
  }

  /**
   * Returns a module generating perlin noise a different, slower, equivalent
   * way.
   * 
   * @return a poor imitation of this auspicious module.
   * @see jnoise.modules.CompositeModule#getBase()
   */
  public Add getBase () {
    return createPerlinChain (seed, octaves, lacunarity, persistence, quality);
  }

  /**
   * Returns the frequency multiplier between successive octaves.
   * 
   * @return the module's lacunarity.
   */
  public double getLacunarity () {
    return lacunarity;
  }

  /**
   * Returns the number of octaves of noise generated.
   * 
   * @return the octave count for the module.
   */
  public int getOctaves () {
    return octaves;
  }

  /**
   * Returns the amplitude multiplier between successive octaves.
   * 
   * @return the module's persistence.
   */
  public double getPersistence () {
    return persistence;
  }

  /**
   * Returns the quality of the generated noise.
   * 
   * @return the quality of the noise generated.
   */
  public Gradient.Quality getQuality () {
    return quality;
  }

  /**
   * Returns the random number seed used to create perlin noise.
   * 
   * @return the generator seed for the module.
   */
  public int getSeed () {
    return seed;
  }

  /**
   * Generates perlin noise for a location.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the noise value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    double freq = 1.0;
    double amp = 1.0;
    double value = 0.0;

    assert (generators.length == octaves);
    for (Gradient generator : generators) {
      assert (generator.getQuality ().equals (quality));
      value += generator.getValue (x * freq, y * freq, z * freq) * amp;

      freq *= lacunarity;
      amp *= persistence;
    }

    return value;
  }

  /**
   * Compares this module for equality with another object. This Perlin module
   * is equal to an object if and only if the object is also a Perlin module
   * with the same seed, quality, lacunarity, persistence, and octave count.
   * 
   * @param object
   *          the object to compare.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof Perlin))
      return false;

    Perlin other = (Perlin) object;
    return getSeed () == other.getSeed ()
        && getLacunarity () == other.getLacunarity ()
        && getOctaves () == other.getOctaves ()
        && getPersistence () == other.getPersistence ()
        && getQuality ().equals (other.getQuality ());
  }

  /**
   * Generates a hashcode for this perlin module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return seed ^ octaves ^ Hash.hashDouble (lacunarity)
        ^ Hash.hashDouble (persistence);
  }

  private final Gradient[] generators;
  private final double     lacunarity;
  private final int        octaves;
  private final double     persistence;
  private final Quality    quality;
  private final int        seed;
}
