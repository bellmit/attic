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
 * Test cases for the Gradient noise module.
 */
public final class GradientTest extends CompositeModuleFixture {
  private static final double AMPLITUDE = 0.8;
  private static final double FREQUENCY = 1.5;

  /**
   * Create a new Gradient test. The fixture is initialised with a known module.
   */
  public GradientTest () {
    super (new Gradient (FREQUENCY, AMPLITUDE));
  }

  /**
   * Test to verify that the accessor on the Gradient module return something
   * useful.
   */
  public void testAccessors () {
    Gradient test = new Gradient (FREQUENCY, AMPLITUDE);
    assertEquals (FREQUENCY, test.getFrequency ());
    assertEquals (AMPLITUDE, test.getAmplitude ());
  }

  /**
   * Test that two Gradient modules compare equal if constructed with equal
   * sources and the same ranges.
   */
  public void testEquals () {
    assertEquals (module, new Gradient (FREQUENCY, AMPLITUDE));
  }

  /**
   * Test that values along a line perpendicular to the XY plane are different.
   */
  public void testGradientAcrossPlane () {
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) != module.getValue (
        TEST_X1, TEST_Y1, TEST_Z1 + 0.5));
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) != module.getValue (
        TEST_X1, TEST_Y1, TEST_Z1 - 0.23));
  }

  /**
   * Test that values along a line parallel to the XY plane are the same.
   */
  public void testGradientAlongPlane () {
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), module.getValue (
        TEST_X1, TEST_Y1 + 5e23, TEST_Z1));
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), module.getValue (
        TEST_X1 - 0.23, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Gradient modules with equal sources and ranges generate the
   * same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Gradient (FREQUENCY, AMPLITUDE)
        .hashCode ());
  }

}
