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
 * A generator creating noise values at integer boundaries.
 * <p>
 * If you are using the included XML noise configuration system, Integer modules
 * can be declared as
 * <p>
 * <blockquote> <code>&lt;integer seed="<var>seed</var>" /&gt;</code>
 * </blockquote>
 */
public final class Integer implements Module {

  private static final int SEED_NOISE_GEN = 1013;
  private static final int UNSIGN_MASK    = 0x7fffffff;
  private static final int X_NOISE_GEN    = 1619;
  // The funniest things are prime...
  private static final int Y_NOISE_GEN    = 31337;
  private static final int Z_NOISE_GEN    = 6971;

  private static int closest (double d) {
    return (int) Math.rint (d);
  }

  /**
   * Creates a new integer noise generator.
   * 
   * @param seed
   *          the random number seed.
   */
  public Integer (int seed) {
    this.seed = seed;
  }

  /**
   * Compares this generator module with another object. This generator is equal
   * to an object if and only if the object is also an Integer module with the
   * same seed value.
   * 
   * @param object
   *          the object to compare to.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof Integer))
      return false;

    Integer other = (Integer) object;
    return other.getSeed () == seed;
  }

  /**
   * Returns the seed value for the module. This is the seed used to generate
   * integer noise.
   * 
   * @return the generator's seed.
   */
  public int getSeed () {
    return seed;
  }

  /**
   * Generates the noise value at the nearest integer position.
   * 
   * @param x
   *          the position's <var>x</var> coordinate.
   * @param y
   *          the position's <var>y</var> coordinate.
   * @param z
   *          the position's <var>z</var> coordinate.
   * @return the noise value.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    // All constants are primes and must remain prime in order for this noise
    // function to work correctly.
    int n = (X_NOISE_GEN * closest (x) + Y_NOISE_GEN * closest (y)
        + Z_NOISE_GEN * closest (z) + SEED_NOISE_GEN * seed)
        & UNSIGN_MASK;
    n = (n >> 13) ^ n;
    return (n * (n * n * 60493 + 19990303) + 1376312589) & UNSIGN_MASK;
  }

  /**
   * Calcuates a hashcode for this generator. The hash code just the seed; as
   * the number of possible seeds is exactly the number of possible hashcodes,
   * this forms a perfect hash.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return seed;
  }

  /** The random number seed used to generate noise. */
  private final int seed;
}
