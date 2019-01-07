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
package ca.grimoire.jnoise.modules.map;

import ca.grimoire.jnoise.modules.SingleSourceModuleFixture;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * Test cases for the Bias noise module.
 */
public final class BiasTest extends SingleSourceModuleFixture {

  private static final double SCALE       = 0.75;
  private static final int    SEED        = 11;
  private static final double TRANSLATION = 0.5;

  /**
   * Create a new Bias module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public BiasTest () {
    super (new Bias (new Gradient (SEED, Gradient.Quality.HIGH), SCALE,
        TRANSLATION), new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test to verify that the accessor on the Bias module return something
   * useful.
   */
  public void testAccessors () {
    Bias test = new Bias (new Gradient (SEED, Gradient.Quality.HIGH), SCALE,
        TRANSLATION);
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
    assertEquals (TRANSLATION, test.getTranslation ());
    assertEquals (SCALE, test.getScale ());
  }

  /**
   * Test that the output from the test module is where the bias says it should
   * be.
   */
  public void testBias () {
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH).getValue (TEST_X1,
        TEST_Y1, TEST_Z1)
        * SCALE + TRANSLATION, module.getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * Test that two Bias modules compare equal if constructed with equal sources
   * and the same bias.
   */
  public void testEquals () {
    assertEquals (module, new Bias (new Gradient (SEED, Gradient.Quality.HIGH),
        SCALE, TRANSLATION));
  }

  /**
   * Test that two Bias modules with equal sources and biases generate the same
   * hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Bias (new Gradient (SEED,
        Gradient.Quality.HIGH), SCALE, TRANSLATION).hashCode ());
  }
}
