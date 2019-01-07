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
 * Test cases verifying the behaviour of the constant "noise" module.
 */
public final class ConstantTest extends ModuleFixture {

  private static final double LARGE_VECTOR_X = -0.3e8;
  private static final double LARGE_VECTOR_Y = 0.001e9;
  private static final double LARGE_VECTOR_Z = 0.2e12;

  private static final double SMALL_VECTOR_X = -0.3;
  private static final double SMALL_VECTOR_Y = 0.001;
  private static final double SMALL_VECTOR_Z = 0.2;

  private static final double VALUE          = 0.667;

  /**
   * Create a test. The underlying fixture will be initialised with a constant
   * noise module with a known noise value.
   */
  public ConstantTest () {
    super (new Constant (VALUE));
  }

  /**
   * Test that the instance accessors return something sensible (eg., the
   * constructed value).
   */
  public void testAccessors () {
    assertEquals (VALUE, new Constant (VALUE).getValue ());
  }

  /**
   * Test case to verify that the constant module returns the correct value.
   */
  public void testCorrectValue () {
    assertEquals (VALUE, module.getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Constant modules compare equal if constructed with the same
   * value.
   */
  public void testEquals () {
    assertEquals (module, new Constant (VALUE));
  }

  /**
   * Test that two Constant modules with the same value generate the same hash
   * code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Constant (VALUE).hashCode ());
  }

  /**
   * Test that two distant points on the module generate the same value. This
   * depends on testCorretValue completing to be valid.
   */
  public void testSameDistant () {
    testCorrectValue ();
    double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    assertEquals (value, module.getValue (TEST_X1 + LARGE_VECTOR_X, TEST_Y1
        + LARGE_VECTOR_Y, TEST_Z1 + LARGE_VECTOR_Z));
  }

  /**
   * Test case to verify that two nearby points on the module generate the same
   * value. This depends on testCorrectValue completing to be valid.
   */
  public void testSameNearby () {
    testCorrectValue ();
    double value = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    assertEquals (value, module.getValue (TEST_X1 + SMALL_VECTOR_X, TEST_Y1
        + SMALL_VECTOR_Y, TEST_Z1 + SMALL_VECTOR_Z));
  }
}
