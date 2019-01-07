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

import ca.grimoire.jnoise.modules.CompositeModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Perlin noise module.
 */
public class PerlinTest extends CompositeModuleFixture {

  private static final int    SEED        = 2600;
  private static final double LACUNARITY  = 2.0;
  private static final double PERSISTENCE = 0.5;
  private static final int    OCTAVES     = 8;

  /**
   * Create a test. This configures the underlying test fixture with a known
   * Perlin noise module.
   */
  public PerlinTest () {
    super (new Perlin (SEED, OCTAVES, LACUNARITY, PERSISTENCE,
        Gradient.Quality.HIGH));
  }

  // TODO this class does not (yet) test random distribution.

  /**
   * Test to verify that the accessors for the Perlin generator return something
   * sensible.
   */
  public void testAccessors () {
    assertEquals (SEED, new Perlin (SEED, OCTAVES, LACUNARITY, PERSISTENCE,
        Gradient.Quality.HIGH).getSeed ());
    assertEquals (OCTAVES, new Perlin (SEED, OCTAVES, LACUNARITY, PERSISTENCE,
        Gradient.Quality.HIGH).getOctaves ());
    assertEquals (LACUNARITY, new Perlin (SEED, OCTAVES, LACUNARITY,
        PERSISTENCE, Gradient.Quality.HIGH).getLacunarity ());
    assertEquals (PERSISTENCE, new Perlin (SEED, OCTAVES, LACUNARITY,
        PERSISTENCE, Gradient.Quality.HIGH).getPersistence ());
    assertEquals (Gradient.Quality.HIGH, new Perlin (SEED, OCTAVES, LACUNARITY,
        PERSISTENCE, Gradient.Quality.HIGH).getQuality ());

  }

  /**
   * Test that two Perlin modules compare equal if constructed with the same
   * parameters.
   */
  public void testEquals () {
    assertEquals (module, new Perlin (SEED, OCTAVES, LACUNARITY, PERSISTENCE,
        Gradient.Quality.HIGH));
  }

  /**
   * Test that two Perlin modules with the same parameters generate the same
   * hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Perlin (SEED, OCTAVES, LACUNARITY,
        PERSISTENCE, Gradient.Quality.HIGH).hashCode ());
  }
}
