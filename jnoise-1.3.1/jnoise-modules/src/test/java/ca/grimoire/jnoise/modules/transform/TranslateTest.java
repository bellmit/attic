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
 * Test cases for the Scale noise module.
 */
public final class TranslateTest extends SingleSourceModuleFixture {
  private static final int    SEED        = -1;
  private static final double TRANSLATE_X = 0.1;
  private static final double TRANSLATE_Y = 2.5;
  private static final double TRANSLATE_Z = 25;

  /**
   * Create a new Scale test. The underlying fixture is fed a known noise
   * module, for testing.
   */
  public TranslateTest () {
    super (new Translate (new Gradient (SEED, Gradient.Quality.HIGH),
        TRANSLATE_X, TRANSLATE_Y, TRANSLATE_Z), new Gradient (SEED,
        Gradient.Quality.HIGH));
  }

  /**
   * Test to verify that the accessors on the Scale module return something
   * useful.
   */
  public void testAccessors () {
    Translate test = new Translate (source, TRANSLATE_X, TRANSLATE_Y,
        TRANSLATE_Z);
    assertEquals (TRANSLATE_X, test.getXTranslation ());
    assertEquals (TRANSLATE_Y, test.getYTranslation ());
    assertEquals (TRANSLATE_Z, test.getZTranslation ());
    assertEquals (source, test.getSource ());
  }

  /**
   * Test that two Scale modules compare equal if constructed with the same
   * scales and equal sources.
   */
  public void testEquals () {
    assertEquals (module, new Translate (source, TRANSLATE_X, TRANSLATE_Y,
        TRANSLATE_Z));
  }

  /**
   * Test that two Scale modules with the same scales and equal sources generate
   * the same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Translate (source, TRANSLATE_X,
        TRANSLATE_Y, TRANSLATE_Z).hashCode ());
  }

  /**
   * Test that the module produces the correct value for point compared to the
   * same (scaled) location on the base module.
   */
  public void testTranslatedPoint () {
    assertEquals (source.getValue (TEST_X1 + TRANSLATE_X,
        TEST_Y1 + TRANSLATE_Y, TEST_Z1 + TRANSLATE_Z), module.getValue (
        TEST_X1, TEST_Y1, TEST_Z1));
  }

}
