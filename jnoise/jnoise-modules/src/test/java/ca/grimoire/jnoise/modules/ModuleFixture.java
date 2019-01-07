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
package ca.grimoire.jnoise.modules;

import junit.framework.TestCase;

/**
 * Test fixture that will perform extremely basic tests against any arbitrary
 * module implementation.
 */
public abstract class ModuleFixture extends TestCase {
  /** X coordinate of first test point. */
  public static final double TEST_X1       = 2;
  /** Y coordinate of first test point. */
  public static final double TEST_Y1       = 4;
  /** Z coordinate of first test point. */
  public static final double TEST_Z1       = 6;

  private static final int   VALUE_REPEATS = 5;

  /**
   * Create a new fixture around a given module.
   * 
   * @param module
   *          the module under test.
   */
  public ModuleFixture (Module module) {
    assert (module != null);
    this.module = module;
  }

  /**
   * Test case verifying that the module under test produces the same result
   * under repeated calls to getValue(double,double,double).
   */
  public void testRepeatableResults () {
    double initialValue = module.getValue (TEST_X1, TEST_Y1, TEST_Z1);
    for (int i = 0; i < VALUE_REPEATS; ++i)
      assertEquals (initialValue, module.getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }

  /** The module under test by the fixture. */
  protected final Module module;
}
