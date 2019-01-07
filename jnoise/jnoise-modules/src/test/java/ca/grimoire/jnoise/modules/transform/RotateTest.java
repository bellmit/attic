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
 * Test suite for the Rotate transformation module. This verifies the module
 * against an underlying module (currently a random-Gradient module).
 */
public final class RotateTest extends SingleSourceModuleFixture {
  private static final double ANGLE_X      = 0.0;
  private static final double ANGLE_Y      = Math.PI / 2;
  private static final double ANGLE_Z      = 0.0;
  private static final double EPSILON      = 1e-10;
  private static final double PROJECTED_X1 = TEST_Z1;
  private static final double PROJECTED_Y1 = TEST_Y1;
  private static final double PROJECTED_Z1 = -TEST_X1;
  private static final int    SEED         = 313233;

  /**
   * Create a new rotation test. The underlying fixture is seeded with a known
   * rotation engine.
   */
  public RotateTest () {
    super (new Rotate (new Gradient (SEED, Gradient.Quality.HIGH), ANGLE_X,
        ANGLE_Y, ANGLE_Z), new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test to verify that the accessors for the Rotate module return something
   * sensible.
   */
  public void testAccessors () {
    Rotate test = new Rotate (source, ANGLE_X, ANGLE_Y, ANGLE_Z);
    assertEquals (Math.IEEEremainder (ANGLE_X, 2 * Math.PI), Math
        .IEEEremainder (test.getXAngle (), 2 * Math.PI));
    assertEquals (Math.IEEEremainder (ANGLE_Y, 2 * Math.PI), Math
        .IEEEremainder (test.getYAngle (), 2 * Math.PI));
    assertEquals (Math.IEEEremainder (ANGLE_Z, 2 * Math.PI), Math
        .IEEEremainder (test.getZAngle (), 2 * Math.PI));
    assertEquals (source, test.getSource ());
  }

  /**
   * Test that two Rotate modules compare equal if constructed with the same
   * angles and equal sources.
   */
  public void testEquals () {
    assertEquals (module, new Rotate (source, ANGLE_X, ANGLE_Y, ANGLE_Z));
  }

  /**
   * Test that two Rotate modules with equal sources and angles generate the
   * same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Rotate (source, ANGLE_X, ANGLE_Y,
        ANGLE_Z).hashCode ());
  }

  /**
   * Test case to validate that the rotation module generates the correct value
   * relative to the underlying module.
   */
  public void testRotatedPoint () {
    assertEquals (source.getValue (PROJECTED_X1, PROJECTED_Y1, PROJECTED_Z1),
        module.getValue (TEST_X1, TEST_Y1, TEST_Z1), EPSILON);
  }

}
