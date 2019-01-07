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
package ca.grimoire.jnoise.modules.util;

import ca.grimoire.jnoise.modules.SingleSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Cache noise module.
 */
public final class CacheTest extends SingleSourceModuleFixture {

  private static final int SEED = 11;

  /**
   * Create a new cache module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public CacheTest () {
    super (new Cache (new Gradient (SEED, Gradient.Quality.HIGH)),
        new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test to verify that the accessor on the Cache module return something
   * useful.
   */
  public void testAccessors () {
    Cache test = new Cache (new Gradient (SEED, Gradient.Quality.HIGH));
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
  }

  /**
   * Test that the cache returns the same noise values as the source.
   */
  public void testCachedValue () {
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), source.getValue (
        TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Cache modules compare equal if constructed with equal
   * sources.
   */
  public void testEquals () {
    assertEquals (module,
        new Cache (new Gradient (SEED, Gradient.Quality.HIGH)));
  }

  /**
   * Test that two Cache modules with equal sources generate the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Cache (new Gradient (SEED,
        Gradient.Quality.HIGH)).hashCode ());
  }
}
