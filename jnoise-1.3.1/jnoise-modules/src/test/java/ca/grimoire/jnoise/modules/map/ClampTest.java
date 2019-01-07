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
 * Test cases for the Clamp noise module.
 */
public final class ClampTest extends SingleSourceModuleFixture {

  private static final int    SEED  = 11;
  private static final double LOWER = 0.5;
  private static final double UPPER = 0.75;

  /**
   * Create a new Clamp module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public ClampTest () {
    super (
        new Clamp (new Gradient (SEED, Gradient.Quality.HIGH), LOWER, UPPER),
        new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test that the output from the test module is within the clamp range.
   */
  public void testClamp () {
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) >= LOWER);
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) <= UPPER);
  }

  /**
   * Test to verify that the accessor on the Clamp module return something
   * useful.
   */
  public void testAccessors () {
    Clamp test = new Clamp (new Gradient (SEED, Gradient.Quality.HIGH), LOWER,
        UPPER);
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
    assertEquals (LOWER, test.getLowerBound ());
    assertEquals (UPPER, test.getUpperBound ());
  }

  /**
   * Test that two Clamp modules compare equal if constructed with equal sources
   * and the same ranges.
   */
  public void testEquals () {
    assertEquals (module, new Clamp (
        new Gradient (SEED, Gradient.Quality.HIGH), LOWER, UPPER));
  }

  /**
   * Test that two Clamp modules with equal sources and ranges generate the same
   * hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Clamp (new Gradient (SEED,
        Gradient.Quality.HIGH), LOWER, UPPER).hashCode ());
  }
}
