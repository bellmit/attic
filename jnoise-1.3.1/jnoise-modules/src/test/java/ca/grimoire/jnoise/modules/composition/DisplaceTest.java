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

import ca.grimoire.jnoise.modules.SingleSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Displace composition module.
 */
public final class DisplaceTest extends SingleSourceModuleFixture {
  private static final int SEED_SOURCE = 2;
  private static final int SEED_X      = 142448;
  private static final int SEED_Y      = -68163;
  private static final int SEED_Z      = 1311;

  /**
   * Create a new Displace module test, populating the fixture with a known
   * module.
   */
  public DisplaceTest () {
    super (new Displace (new Gradient (SEED_SOURCE, Gradient.Quality.HIGH),
        new Gradient (SEED_X, Gradient.Quality.HIGH), new Gradient (SEED_Y,
            Gradient.Quality.HIGH),
        new Gradient (SEED_Z, Gradient.Quality.HIGH)), new Gradient (
        SEED_SOURCE, Gradient.Quality.HIGH));
  }

  /**
   * Test that the result of displacing the source module by the appropriate
   * value along each axis results in the same value as the Displace module
   * under test.
   */
  public void testDisplacement () {
    double x = TEST_X1
        + new Gradient (SEED_X, Gradient.Quality.HIGH).getValue (TEST_X1,
            TEST_Y1, TEST_Z1);
    double y = TEST_Y1
        + new Gradient (SEED_Y, Gradient.Quality.HIGH).getValue (TEST_X1,
            TEST_Y1, TEST_Z1);
    double z = TEST_Z1
        + new Gradient (SEED_Z, Gradient.Quality.HIGH).getValue (TEST_X1,
            TEST_Y1, TEST_Z1);

    assertEquals (new Gradient (SEED_SOURCE, Gradient.Quality.HIGH).getValue (
        x, y, z), module.getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that the accessors for the Displace module return useful information.
   */
  public void testAccessors () {
    assertEquals (new Gradient (SEED_X, Gradient.Quality.HIGH), new Displace (
        new Gradient (SEED_SOURCE, Gradient.Quality.HIGH), new Gradient (
            SEED_X, Gradient.Quality.HIGH), new Gradient (SEED_Y,
            Gradient.Quality.HIGH),
        new Gradient (SEED_Z, Gradient.Quality.HIGH)).getXDisplacer ());
    assertEquals (new Gradient (SEED_Y, Gradient.Quality.HIGH), new Displace (
        new Gradient (SEED_SOURCE, Gradient.Quality.HIGH), new Gradient (
            SEED_X, Gradient.Quality.HIGH), new Gradient (SEED_Y,
            Gradient.Quality.HIGH),
        new Gradient (SEED_Z, Gradient.Quality.HIGH)).getYDisplacer ());
    assertEquals (new Gradient (SEED_Z, Gradient.Quality.HIGH), new Displace (
        new Gradient (SEED_SOURCE, Gradient.Quality.HIGH), new Gradient (
            SEED_X, Gradient.Quality.HIGH), new Gradient (SEED_Y,
            Gradient.Quality.HIGH),
        new Gradient (SEED_Z, Gradient.Quality.HIGH)).getZDisplacer ());
  }

  /**
   * Test that two modules with equal source modules compare equal to each
   * other.
   */
  public void testEquals () {
    assertEquals (module, new Displace (new Gradient (SEED_SOURCE,
        Gradient.Quality.HIGH), new Gradient (SEED_X, Gradient.Quality.HIGH),
        new Gradient (SEED_Y, Gradient.Quality.HIGH), new Gradient (SEED_Z,
            Gradient.Quality.HIGH)));
  }

  /**
   * Test that two modules with equal sources generate equal hashcodes.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Displace (new Gradient (SEED_SOURCE,
        Gradient.Quality.HIGH), new Gradient (SEED_X, Gradient.Quality.HIGH),
        new Gradient (SEED_Y, Gradient.Quality.HIGH), new Gradient (SEED_Z,
            Gradient.Quality.HIGH)).hashCode ());
  }
}
