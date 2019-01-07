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
 * Copyright (C) 2006 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.modules.generation;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.basic.Gradient.Quality;
import ca.grimoire.jnoise.util.Hash;

/**
 * Noise module that outputs 3-dimensional ridged-multifractal noise.This noise
 * module, heavily based on the Perlin-noise module, generates
 * ridged-multifractal noise. Ridged-multifractal noise is generated in much of
 * the same way as Perlin noise, except the output of each octave is modified by
 * an absolute-value function. Modifying the octave values in this way produces
 * ridge-like formations.
 * <p>
 * Ridged-multifractal noise does not use a persistence value. This is because
 * the persistence values of the octaves are based on the values generated from
 * from previous octaves, creating a feedback loop
 * <p>
 * If you are using the included XML noise configuration system,
 * RidgedMultifractal modules can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;ridgedmulti seed="<var>seed</var>" octaves="<var>octaves</var>" lacunarity="<var>lacunarity</var>" quality="<var>QUALITY</var>" /&gt;</code>
 * <br>
 * or<br>
 * <code>&lt;ridgedmulti seed="<var>seed</var>" octaves="<var>octaves</var>" lacunarity="<var>lacunarity</var>" weight="<var>weight</var>" frequency="<var>frequency</var>" quality="<var>QUALITY</var>" /&gt;</code>
 * </blockquote>
 * <p>
 * The <var>QUALITY</var> is one of <kbd>LOW</kbd>, <kbd>MEDIUM</kbd>, or
 * <kbd>HIGH</kbd>.
 */
public final class RidgedMultifractal implements Module {
  // TODO when eliminating optional c'tor, also de-optionalize tag params.

  /**
   * The default frequency used by
   * {@link #RidgedMultifractal(int, int, double, Gradient.Quality)}.
   */
  @Deprecated
  public static final double DEFAULT_FREQUENCY = 1.0;

  /**
   * The default per-octave frequency exponent used by
   * {@link #RidgedMultifractal(int, int, double, Gradient.Quality)}.
   */
  @Deprecated
  public static final double DEFAULT_WEIGHT    = -1.0;

  private static double[] generateSpectralWeights (double lacunarity,
      int octaves, double h, double frequency) {
    double[] weights = new double[octaves];

    for (int octave = 0; octave < octaves; ++octave) {
      weights[octave] = Math.pow (frequency, h);
      frequency *= lacunarity;
    }

    return weights;
  }

  /**
   * Creates a new ridged multifractal generator.
   * 
   * @param seed
   *          the seed to use as a base for gradient generation.
   * @param octaves
   *          the number of octaves of noise to produce.
   * @param lacunarity
   *          the per-octave frequency multiplier.
   * @param weight
   *          the weighting exponent applied to successive octaves' frequencies.
   *          The most interesting patterns occur when the weight is negative.
   * @param frequency
   *          the frequency of the first octave.
   * @param quality
   *          the quality level of the generated noise.
   */
  public RidgedMultifractal (int seed, int octaves, double lacunarity,
      double weight, double frequency, Quality quality) {
    assert (octaves >= 0);
    assert (quality != null);

    generator = new Gradient (seed, quality);
    this.frequency = frequency;
    this.weight = weight;
    this.lacunarity = lacunarity;
    this.octaves = octaves;
    this.weights = generateSpectralWeights (lacunarity, octaves, weight,
        frequency);
  }

  /**
   * Creates a new ridged multifractal generator.
   * 
   * @param seed
   *          the seed to use as a base for gradient generation.
   * @param octaves
   *          the number of octaves of noise to produce.
   * @param lacunarity
   *          the per-octave frequency multiplier.
   * @param quality
   *          the quality level of the generated noise.
   * @deprecated This constructor does not allow clients to customize the
   *             frequency weighting exponent or the base frequency. Use
   *             {@link #RidgedMultifractal(int, int, double, double, double, Quality)}
   *             when constructing RidgedMultifractal generators. This
   *             constructor merely passes the arguments to that constructor,
   *             with <code>DEFAULT_WEIGHT</code> and
   *             <code>DEFAULT_FREQUENCY</code> defaults.
   */
  @Deprecated
  public RidgedMultifractal (int seed, int octaves, double lacunarity,
      Gradient.Quality quality) {
    this (seed, octaves, lacunarity, DEFAULT_WEIGHT, DEFAULT_FREQUENCY, quality);
  }

  /**
   * Compares this module for equality with another object. This module is equal
   * to an object if and only if the object is also a RidgedMultifractal module
   * with the same seed, quality, lacunarity, and octave count.
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
    else if (!(object instanceof RidgedMultifractal))
      return false;

    RidgedMultifractal other = (RidgedMultifractal) object;
    return getSeed () == other.getSeed ()
        && getLacunarity () == other.getLacunarity ()
        && getOctaves () == other.getOctaves ()
        && getQuality ().equals (other.getQuality ());
  }

  /**
   * Returns the base frequency of the module.
   * 
   * @return the frequency of the module's first octave.
   */
  public double getFrequency () {
    return frequency;
  }

  /**
   * Returns the multiplier between the frequencies of successive octaves.
   * 
   * @return this module's lacunarity.
   */
  public double getLacunarity () {
    return lacunarity;
  }

  /**
   * Returns the number of octaves being generated.
   * 
   * @return this module's octave count.
   */
  public int getOctaves () {
    return octaves;
  }

  /**
   * Returns the generated noise quality.
   * 
   * @return this module's generation quality.
   */
  public Gradient.Quality getQuality () {
    return generator.getQuality ();
  }

  /**
   * Returns the random number seed used to generate noise.
   * 
   * @return this module's random number seed.
   */
  public int getSeed () {
    return generator.getSeed ();
  }

  /**
   * Generates a noise value at the passed location.
   * 
   * @param x
   *          the location's x coordinate.
   * @param y
   *          the location's y coordinate.
   * @param z
   *          the location's z coordinate.
   * @return the noise value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    double value = 0.0;
    double weight = 1.0;

    // These parameters should be user-defined; they may be exposed in a
    // future version of jnoise.
    double offset = 1.0;
    double gain = 2.0;

    for (int octave = 0; octave < octaves; octave++) {
      double signal = generator.getValue (x, y, z);

      // Make the ridges.
      signal = Math.abs (signal);
      signal = offset - signal;

      // Square the signal to increase the sharpness of the ridges.
      signal *= signal;

      // The weighting from the previous octave is applied to the signal.
      // Larger values have higher weights, producing sharp points along the
      // ridges.
      signal *= weight;

      // Weight successive contributions by the previous signal.
      weight = signal * gain;
      if (weight > 1.0) {
        weight = 1.0;
      }
      if (weight < 0.0) {
        weight = 0.0;
      }

      // Add the signal to the output value.
      value += (signal * weights[octave]);

      // Go to the next octave.
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }

    return (value * 1.25) - 1.0;
  }

  /**
   * Returns the exponent used between the frequencies of successive octaves.
   * 
   * @return the octave frequency weighting exponent.
   */
  public double getWeight () {
    return weight;
  }

  /**
   * Generates a hashcode for this multifractal module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return generator.hashCode () ^ octaves ^ Hash.hashDouble (lacunarity);
  }

  private final double   frequency;
  private final Gradient generator;
  private final double   lacunarity;
  private final int      octaves;

  private final double   weight;

  private final double[] weights;
}
