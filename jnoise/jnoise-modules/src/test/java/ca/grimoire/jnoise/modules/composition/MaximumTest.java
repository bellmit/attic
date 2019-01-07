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
package ca.grimoire.jnoise.modules.composition;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.MultiSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Maximum composition module.
 */
public final class MaximumTest extends MultiSourceModuleFixture {
  private static final int SEED_1 = 13;
  private static final int SEED_2 = -58;

  /**
   * Create a new Maximum module test, populating the fixture with a known
   * module.
   */
  public MaximumTest () {
    super (new Maximum (new Gradient (SEED_1, Gradient.Quality.HIGH),
        new Gradient (SEED_2, Gradient.Quality.HIGH)), new Gradient (SEED_1,
        Gradient.Quality.HIGH), new Gradient (SEED_2, Gradient.Quality.HIGH));
  }

  /**
   * Test that the result of max()ing the outputs of all source modules is
   * equivalent to the output of the test module.
   */
  public void testMaximumition () {
    double value1 = new Gradient (SEED_1, Gradient.Quality.HIGH).getValue (
        TEST_X1, TEST_Y1, TEST_Z1);
    double value2 = new Gradient (SEED_2, Gradient.Quality.HIGH).getValue (
        TEST_X1, TEST_Y1, TEST_Z1);
    assertEquals (Math.max (value1, value2), module.getValue (TEST_X1, TEST_Y1,
        TEST_Z1));
  }

  /**
   * Test that the accessors for the Maximum module return useful information.
   */
  public void testAccessors () {
    for (Module source : new Maximum (new Gradient (SEED_1,
        Gradient.Quality.HIGH), new Gradient (SEED_2, Gradient.Quality.HIGH))
        .getSources ()) {
      // Yeah yeah, this shits all over the heap. It's a test, and it's correct,
      // just inefficient.
      assertTrue (source.equals (new Gradient (SEED_1, Gradient.Quality.HIGH))
          || source.equals (new Gradient (SEED_2, Gradient.Quality.HIGH)));
    }
  }

  /**
   * Test that two modules with equal source modules compare equal to each
   * other.
   */
  public void testEquals () {
    assertEquals (module, new Maximum (new Gradient (SEED_1,
        Gradient.Quality.HIGH), new Gradient (SEED_2, Gradient.Quality.HIGH)));
  }

  /**
   * Test that two modules with equal sources generate equal hashcodes.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Maximum (new Gradient (SEED_1,
        Gradient.Quality.HIGH), new Gradient (SEED_2, Gradient.Quality.HIGH))
        .hashCode ());
  }
}
