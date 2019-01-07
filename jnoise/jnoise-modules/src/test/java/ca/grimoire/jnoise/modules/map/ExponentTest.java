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
package ca.grimoire.jnoise.modules.map;

import ca.grimoire.jnoise.modules.SingleSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Exponent noise module.
 */
public final class ExponentTest extends SingleSourceModuleFixture {

  private static final int    SEED     = 11;
  private static final double EXPONENT = -1.0;

  /**
   * Create a new Exponent module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public ExponentTest () {
    super (new Exponent (new Gradient (SEED, Gradient.Quality.HIGH), EXPONENT),
        new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test that the output from the test module is where it should be.
   */
  public void testExponent () {
    assertEquals (Math.pow (new Gradient (SEED, Gradient.Quality.HIGH)
        .getValue (TEST_X1, TEST_Y1, TEST_Z1), EXPONENT), module.getValue (
        TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * Test to verify that the accessor on the Exponent module return something
   * useful.
   */
  public void testAccessors () {
    Exponent test = new Exponent (new Gradient (SEED, Gradient.Quality.HIGH),
        EXPONENT);
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
    assertEquals (EXPONENT, test.getExponent ());
  }

  /**
   * Test that two Exponent modules compare equal if constructed with equal
   * sources and the same exponent.
   */
  public void testEquals () {
    assertEquals (module, new Exponent (new Gradient (SEED,
        Gradient.Quality.HIGH), EXPONENT));
  }

  /**
   * Test that two Exponent modules with equal sources and exponent generate the
   * same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Exponent (new Gradient (SEED,
        Gradient.Quality.HIGH), EXPONENT).hashCode ());
  }
}
