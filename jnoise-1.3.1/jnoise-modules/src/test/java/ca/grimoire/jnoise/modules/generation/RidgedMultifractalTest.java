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

import ca.grimoire.jnoise.modules.ModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the RidgedMultifractal noise module.
 * 
 * @deprecated The class under test,
 *             {@link jnoise.modules.generation.RidgedMultifractal}, is also
 *             deprecated.
 */
@Deprecated
public class RidgedMultifractalTest extends ModuleFixture {

  private static final int    SEED       = 2600;
  private static final double LACUNARITY = 2.0;
  private static final int    OCTAVES    = 8;

  /**
   * Creates a test. This configures the underlying test fixture with a known
   * RidgedMultifractal noise module.
   */
  public RidgedMultifractalTest () {
    super (new RidgedMultifractal (SEED, OCTAVES, LACUNARITY,
        Gradient.Quality.HIGH));
  }

  /**
   * Verifies that the accessors for the RidgedMultifractal generator return
   * something sensible.
   */
  public void testAccessors () {
    assertEquals (SEED, new RidgedMultifractal (SEED, OCTAVES, LACUNARITY,
        Gradient.Quality.HIGH).getSeed ());
    assertEquals (OCTAVES, new RidgedMultifractal (SEED, OCTAVES, LACUNARITY,
        Gradient.Quality.HIGH).getOctaves ());
    assertEquals (LACUNARITY, new RidgedMultifractal (SEED, OCTAVES,
        LACUNARITY, Gradient.Quality.HIGH).getLacunarity ());
    assertEquals (Gradient.Quality.HIGH, new RidgedMultifractal (SEED, OCTAVES,
        LACUNARITY, Gradient.Quality.HIGH).getQuality ());

  }

  /**
   * Verifies that two RidgedMultifractal modules compare equal if constructed
   * with the same parameters.
   */
  public void testEquals () {
    assertEquals (module, new RidgedMultifractal (SEED, OCTAVES, LACUNARITY,
        Gradient.Quality.HIGH));
  }

  /**
   * Verifies that two RidgedMultifractal modules with the same parameters
   * generate the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new RidgedMultifractal (SEED, OCTAVES,
        LACUNARITY, Gradient.Quality.HIGH).hashCode ());
  }
}
