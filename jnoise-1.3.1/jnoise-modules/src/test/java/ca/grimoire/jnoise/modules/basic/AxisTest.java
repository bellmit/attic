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
 * Test cases verifying the behaviour of the Axis "noise" module.
 */
public final class AxisTest extends ModuleFixture {

  private static final double LARGE_X      = -0.3e8;
  private static final double LARGE_Y      = 0.001e9;
  private static final double LARGE_Z      = 0.2e12;
  private static final double LARGE_RADIUS = Math.sqrt (LARGE_Y * LARGE_Y
                                               + LARGE_Z * LARGE_Z);

  private static final double SMALL_X      = -0.3;
  private static final double SMALL_Y      = 0.001;
  private static final double SMALL_Z      = 0.2;
  private static final double SMALL_RADIUS = Math.sqrt (SMALL_Y * SMALL_Y
                                               + SMALL_Z * SMALL_Z);

  /**
   * Create a test. The underlying fixture will be initialised with an Axis
   * noise module.
   */
  public AxisTest () {
    super (new Axis ());
  }

  /**
   * Test case to verify that the constant module returns the correct value.
   */
  public void testCorrectValue () {
    assertEquals (SMALL_RADIUS, module.getValue (SMALL_X, SMALL_Y, SMALL_Z));
    assertEquals (LARGE_RADIUS, module.getValue (LARGE_X, LARGE_Y, LARGE_Z));
  }

  /**
   * Test that two Axis modules compare equal.
   */
  public void testEquals () {
    assertEquals (module, Axis.MODULE);
  }

  /**
   * Test that two Axis modules generate the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), Axis.MODULE.hashCode ());
  }

}
