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
package ca.grimoire.jnoise.modules.basic;

import ca.grimoire.jnoise.modules.Module;

/**
 * A generator creating smooth, continuous noise using a gradient-noise
 * generation algorithm.
 * <p>
 * If you are using the included XML noise configuration system, Gradient
 * modules can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;bumps seed="<var>seed</var>" quality="<var>QUALITY</var>" /&gt;</code>
 * </blockquote>
 * <p>
 * The <var>QUALITY</var> is one of <kbd>LOW</kbd>, <kbd>MEDIUM</kbd>, or
 * <kbd>HIGH</kbd>.
 */
public final class Gradient implements Module {

  /**
   * Gradient noise generation uses interpolation to smooth the peaks and
   * valleys of the generated noise. The various quality levels control the
   * interpolation methods used.
   */
  public static enum Quality {
    /**
     * High-quality noise uses quintic S-curves to select positions within a
     * cube, resulting in extremely smooth noise.
     */
    HIGH () {
      @Override
      double map (double v) {
        double v3 = v * v * v;
        double v4 = v3 * v;
        double v5 = v4 * v;
        return (6.0 * v5) - (15.0 * v4) + (10.0 * v3);
      }
    },
    /**
     * Low-quality noise uses linear interpolation to select positions within a
     * cube.
     */
    LOW () {
      @Override
      double map (double v) {
        return v;
      }
    },
    /**
     * Medium-quality noise uses cubic S-curves to select positions within a
     * cube, generating smoother transitions.
     */
    MEDIUM () {
      @Override
      double map (double v) {
        return v * v * (3.0 - 2.0 * v);
      }
    };

    /**
     * Remaps a value in the range [0..1) to another value according to a curve.
     * 
     * @param v
     *          the value to map.
     * @return the mapped value.
     */
    abstract double map (double v);
  }

  private static final int           SEED_NOISE_GEN  = 1013;

  private static final int           SHIFT_NOISE_GEN = 8;

  private static GradientVectorTable TABLE           = new GradientVectorTable ();

  private static final int           X_NOISE_GEN     = 1619;

  private static final int           Y_NOISE_GEN     = 31337;

  private static final int           Z_NOISE_GEN     = 6971;

  private static double lerp (double lower, double upper, double ratio) {
    return ((1.0 - ratio) * lower) + (ratio * upper);
  }

  /**
   * Creates a new gradient noise generator.
   * 
   * @param seed
   *          the seed for noise generation.
   * @param quality
   *          the quality level of the generated noise.
   */
  public Gradient (int seed, Quality quality) {
    assert (quality != null);

    this.seed = seed;
    this.quality = quality;
  }

  /**
   * Compares this generator for equality with another object. This module is
   * equal to an object if and only if the object is also an Gradient module
   * with the same seed value and quality.
   * 
   * @param object
   *          the object to compare.
   * @return <code>true</code> if <var>object</var> is an equal gradient
   *         module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof Gradient))
      return false;

    Gradient other = (Gradient) object;
    return other.getSeed () == seed && other.getQuality () == quality;
  }

  /**
   * Returns the quality level of this gradient noise generator.
   * 
   * @return the generator quality.
   */
  public Quality getQuality () {
    return quality;
  }

  /**
   * Returns the seed value for this gradient noise generator.
   * 
   * @return the generator's seed.
   */
  public int getSeed () {
    return seed;
  }

  /**
   * Generates this generator's noise value at position.
   * 
   * @param x
   *          the position's <var>x</var> coordinate.
   * @param y
   *          the position's <var>y</var> coordinate.
   * @param z
   *          the position's <var>z</var> coordinate.
   * @return the noise value at the position.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    // Get the coordinates of an integer cube surrounding the location.
    double ax = Math.floor (x);
    double ay = Math.floor (y);
    double az = Math.floor (z);

    // Far corner from A
    double bx = ax + 1;
    double by = ay + 1;
    double bz = az + 1;

    // Map the input point against the quality level
    double xs = quality.map (x - ax);
    double ys = quality.map (y - ay);
    double zs = quality.map (z - az);

    // Now calculate the noise values at each vertex of the cube. To generate
    // the coherent-noise value at the input point, interpolate these eight
    // noise values using the S-curve value as the interpolant (trilinear
    // interpolation.)
    double na, n1, iax, ibx, iay, iby;
    na = gradientNoise (x, y, z, ax, ay, az);
    n1 = gradientNoise (x, y, z, bx, ay, az);
    iax = lerp (na, n1, xs);
    na = gradientNoise (x, y, z, ax, by, az);
    n1 = gradientNoise (x, y, z, bx, by, az);
    ibx = lerp (na, n1, xs);
    iay = lerp (iax, ibx, ys);
    na = gradientNoise (x, y, z, ax, ay, bz);
    n1 = gradientNoise (x, y, z, bx, ay, bz);
    iax = lerp (na, n1, xs);
    na = gradientNoise (x, y, z, ax, by, bz);
    n1 = gradientNoise (x, y, z, bx, by, bz);
    ibx = lerp (na, n1, xs);
    iby = lerp (iax, ibx, ys);

    return lerp (iay, iby, zs);
  }

  /**
   * Generates a hashcode for this gradient generator.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return seed ^ quality.hashCode ();
  }

  private double gradientNoise (double fx, double fy, double fz, double bx,
      double by, double bz) {
    int ix = (int) bx;
    int iy = (int) by;
    int iz = (int) bz;

    // Randomly generate a gradient vector given the integer coordinates of the
    // input value. This implementation generates a random number and uses it
    // as an index into a normalized-vector lookup table.
    int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN
        * seed);
    vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
    vectorIndex &= 0xff;

    double[] gradientVector = TABLE.getVector (vectorIndex);
    double xvGradient = gradientVector[0];
    double yvGradient = gradientVector[1];
    double zvGradient = gradientVector[2];

    // Set up us another vector equal to the distance between the two vectors
    // passed to this function.
    double xvPoint = (fx - bx);
    double yvPoint = (fy - by);
    double zvPoint = (fz - bz);

    // Now compute the dot product of the gradient vector with the distance
    // vector. The resulting value is gradient noise. Apply a scaling value
    // so that this noise value ranges from -1.0 to 1.0.
    return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) * 2.12;
  }

  private final Quality quality;

  /** The random number seed used to generate noise. */
  private final int     seed;
}
