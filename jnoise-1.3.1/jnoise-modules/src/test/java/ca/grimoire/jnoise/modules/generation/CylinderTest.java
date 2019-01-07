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
package ca.grimoire.jnoise.modules.generation;

import ca.grimoire.jnoise.modules.CompositeModuleFixture;

/**
 * Test cases for the Cylinder noise module.
 */
public final class CylinderTest extends CompositeModuleFixture {
  private static final double AMPLITUDE = 0.8;
  private static final double FREQUENCY = 1.5;

  /**
   * Create a new Cylinder test. The fixture is initialised with a known module.
   */
  public CylinderTest () {
    super (new Cylinder (FREQUENCY, AMPLITUDE));
  }

  /**
   * Test to verify that the accessor on the Cylinder module return something
   * useful.
   */
  public void testAccessors () {
    Cylinder test = new Cylinder (FREQUENCY, AMPLITUDE);
    assertEquals (FREQUENCY, test.getFrequency ());
    assertEquals (AMPLITUDE, test.getAmplitude ());
  }

  /**
   * Test that values along a line perpendicular to the axis are different.
   */
  public void testCylinderAcrossAxis () {
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) != module.getValue (
        TEST_X1, TEST_Y1 + 0.5, TEST_Z1));
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) != module.getValue (
        TEST_X1, TEST_Y1 - 0.23, TEST_Z1));
  }

  /**
   * Test that values along a line parallel to the cylinder axis are the same.
   */
  public void testCylinderAlongAxis () {
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), module.getValue (
        TEST_X1 + 5e23, TEST_Y1, TEST_Z1));
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), module.getValue (
        TEST_X1 - 0.23, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Cylinder modules compare equal if constructed with the same
   * frequency and amplitude.
   */
  public void testEquals () {
    assertEquals (module, new Cylinder (FREQUENCY, AMPLITUDE));
  }

  /**
   * Test that two Cylinder modules with the same frequency and amplitude
   * generate the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Cylinder (FREQUENCY, AMPLITUDE)
        .hashCode ());
  }

}
