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

import ca.grimoire.jnoise.modules.ModuleFixture;

/**
 * Test cases for the basic integer noise generator.
 */
public class IntegerTest extends ModuleFixture {

  private static final int    SEED     = 2600;

  private static final double VECTOR_X = 0.05;
  private static final double VECTOR_Y = -0.1;
  private static final double VECTOR_Z = 0.33;

  /**
   * Create a test. This configures the underlying test fixture with an Integer
   * noise module.
   */
  public IntegerTest () {
    super (new Integer (SEED));
  }

  // TODO this class does not (yet) test random distribution.

  /**
   * Test to verify that the accessors for the Integer generator return
   * something sensible.
   */
  public void testAccessors () {
    assertEquals (SEED, new Integer (SEED).getSeed ());
  }

  /**
   * Test that verifies that two points within the same "cell" of the integer
   * noise field generate the same value.
   */
  public void testWithinCell () {
    double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    assertEquals (value, module.getValue (TEST_X1 + VECTOR_X, TEST_Y1
        + VECTOR_Y, TEST_Z1 + VECTOR_Z));
  }

  /**
   * Test that two Integer modules compare equal if constructed with the same
   * generation seed.
   */
  public void testEquals () {
    assertEquals (module, new Integer (SEED));
  }

  /**
   * Test that two Integer modules with the same value generate the same hash
   * code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Integer (SEED).hashCode ());
  }
}
