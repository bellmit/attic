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
package ca.grimoire.jnoise.modules.composition;

import ca.grimoire.jnoise.modules.ModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Tests verifying the properties of the Select noise module.
 */
public class SelectTest extends ModuleFixture {

  private static final int SEED_UPPER    = 22;
  private static final int SEED_LOWER    = -37;
  private static final int SEED_SELECTOR = 36123;

  /**
   * Create a new test suite for a known Select module.
   */
  public SelectTest () {
    super (new Select (new Gradient (SEED_LOWER, Gradient.Quality.HIGH),
        new Gradient (SEED_UPPER, Gradient.Quality.HIGH), new Gradient (
            SEED_SELECTOR, Gradient.Quality.HIGH), 0, 0));
  }

  /**
   * Test that two Select modules with equal sources and thresholds compare
   * equal.
   */
  public void testEquality () {
    assertEquals (module, new Select (new Gradient (SEED_LOWER,
        Gradient.Quality.HIGH),
        new Gradient (SEED_UPPER, Gradient.Quality.HIGH), new Gradient (
            SEED_SELECTOR, Gradient.Quality.HIGH), 0, 0));
  }

  /**
   * Test that two Select modules with equal sources and thresholds produce
   * equal hash codes.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Select (new Gradient (SEED_LOWER,
        Gradient.Quality.HIGH),
        new Gradient (SEED_UPPER, Gradient.Quality.HIGH), new Gradient (
            SEED_SELECTOR, Gradient.Quality.HIGH), 0, 0).hashCode ());
  }
}
