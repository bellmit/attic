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
 * Test cases for the basic Checker noise generator.
 */
public class CheckerTest extends ModuleFixture {

  private static final double BLACK    = 0.01;
  private static final double WHITE    = 1.0 - BLACK;

  private static final double VECTOR_X = 0.05;
  private static final double VECTOR_Y = 0.1;
  private static final double VECTOR_Z = 0.33;

  /**
   * Create a test. This configures the underlying test fixture with an Checker
   * noise module.
   */
  public CheckerTest () {
    super (new Checker (BLACK, WHITE));
  }

  /**
   * Test to verify that the accessors for the Checker generator return
   * something sensible.
   */
  public void testAccessors () {
    Checker test = new Checker (BLACK, WHITE);
    assertEquals (BLACK, test.getBlackValue ());
    assertEquals (WHITE, test.getWhiteValue ());
  }

  /**
   * Test that verifies that two points within the same "cell" of the Checker
   * noise field generate the same value.
   */
  public void testWithinCell () {
    double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    assertEquals (value, module.getValue (TEST_X1 + VECTOR_X, TEST_Y1
        + VECTOR_Y, TEST_Z1 + VECTOR_Z));
  }

  /**
   * Test that verifies that two points within the adjacent "cells" of the
   * Checker noise field generate distinct values.
   */
  public void testAdjacentCell () {
    double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    assertTrue (value != module.getValue (TEST_X1 + 1.0, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Checker modules compare equal if constructed with the same
   * cell values.
   */
  public void testEquals () {
    assertEquals (module, new Checker (BLACK, WHITE));
  }

  /**
   * Test that two Checker modules with the same cell values generate the same
   * hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Checker (BLACK, WHITE).hashCode ());
  }
}
