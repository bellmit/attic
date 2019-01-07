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
package ca.grimoire.jnoise.modules.basic;

import ca.grimoire.jnoise.modules.ModuleFixture;

/**
 * Test cases for the basic gradient noise generator.
 */
public class GradientTest extends ModuleFixture {

  private static final int SEED = 2600;

  // private static final double VECTOR_X = 0.05;
  // private static final double VECTOR_Y = -0.1;
  // private static final double VECTOR_Z = 0.33;

  /**
   * Create a test. This configures the underlying test fixture with a Gradient
   * noise module.
   */
  public GradientTest () {
    super (new Gradient (SEED, Gradient.Quality.HIGH));
  }

  // TODO this class does not (yet) test random distribution.

  /**
   * Test to verify that the accessors for the Gradient generator return
   * something sensible.
   */
  public void testAccessors () {
    assertEquals (SEED, new Gradient (SEED, Gradient.Quality.HIGH).getSeed ());
  }

  // No longer appropriate, module now generates gradient noise.
  // /**
  // * Test that verifies that two points within the same "cell" of the integer
  // * noise field generate the same value.
  // */
  // public void testWithinCell () {
  // double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
  // assertEquals (value, module.getValue (TEST_X1 + VECTOR_X, TEST_Y1
  // + VECTOR_Y, TEST_Z1 + VECTOR_Z));
  // }

  /**
   * Test that two Gradient modules compare equal if constructed with the same
   * generation seed.
   */
  public void testEquals () {
    assertEquals (module, new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test that two Gradient modules with the same seed generate the same hash
   * code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (),
        new Gradient (SEED, Gradient.Quality.HIGH).hashCode ());
  }
}
