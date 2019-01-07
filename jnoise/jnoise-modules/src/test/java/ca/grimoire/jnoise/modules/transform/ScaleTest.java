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
package ca.grimoire.jnoise.modules.transform;

import ca.grimoire.jnoise.modules.SingleSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Scale noise module.
 */
public final class ScaleTest extends SingleSourceModuleFixture {
  private static final double SCALE_X = 0.1;
  private static final double SCALE_Y = 2.5;
  private static final double SCALE_Z = 25;
  private static final int    SEED    = -151;

  /**
   * Create a new Scale test. The underlying fixture is fed a known noise
   * module, for testing.
   */
  public ScaleTest () {
    super (new Scale (new Gradient (SEED, Gradient.Quality.HIGH), SCALE_X,
        SCALE_Y, SCALE_Z), new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test to verify that the accessors on the Scale module return something
   * useful.
   */
  public void testAccessors () {
    Scale test = new Scale (source, SCALE_X, SCALE_Y, SCALE_Z);
    assertEquals (SCALE_X, test.getXScale ());
    assertEquals (SCALE_Y, test.getYScale ());
    assertEquals (SCALE_Z, test.getZScale ());
    assertEquals (source, test.getSource ());
  }

  /**
   * Test that two Scale modules compare equal if constructed with the same
   * scales and equal sources.
   */
  public void testEquals () {
    assertEquals (module, new Scale (source, SCALE_X, SCALE_Y, SCALE_Z));
  }

  /**
   * Test that two Scale modules with the same scales and equal sources generate
   * the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Scale (source, SCALE_X, SCALE_Y,
        SCALE_Z).hashCode ());
  }

  /**
   * Test that the module produces the correct value for point compared to the
   * same (scaled) location on the base module.
   */
  public void testScaledPoint () {
    assertEquals (source.getValue (TEST_X1 * SCALE_X, TEST_Y1 * SCALE_Y,
        TEST_Z1 * SCALE_Z), module.getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }
}
