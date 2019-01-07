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
 * Test cases for the Absolute noise module.
 */
public final class AbsoluteTest extends SingleSourceModuleFixture {

  private static final int SEED = 11;

  /**
   * Create a new Absolute module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public AbsoluteTest () {
    super (new Absolute (new Gradient (SEED, Gradient.Quality.HIGH)),
        new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test that the output from the test module is greater than or equal to zero,
   * as an absolute value ought to be.
   */
  public void testAbsolute () {
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) >= 0.0);
  }

  /**
   * Test to verify that the accessor on the Absolute module return something
   * useful.
   */
  public void testAccessors () {
    Absolute test = new Absolute (new Gradient (SEED, Gradient.Quality.HIGH));
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
  }

  /**
   * Test that two Absolute modules compare equal if constructed with equal
   * sources.
   */
  public void testEquals () {
    assertEquals (module, new Absolute (new Gradient (SEED,
        Gradient.Quality.HIGH)));
  }

  /**
   * Test that two Absolute modules with equal sources generate the same hash
   * code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Absolute (new Gradient (SEED,
        Gradient.Quality.HIGH)).hashCode ());
  }
}
