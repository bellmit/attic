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
 * Test cases for the Sawtooth noise module.
 */
public final class SawtoothTest extends SingleSourceModuleFixture {

  private static final int    SEED      = 11;
  private static final double FREQUENCY = 0.5;
  private static final double AMPLITUDE = 0.75;

  /**
   * Create a new Sawtooth module test, initialising the fixture with a known
   * default module (an Gradient module).
   */
  public SawtoothTest () {
    super (new Sawtooth (new Gradient (SEED, Gradient.Quality.HIGH), FREQUENCY,
        AMPLITUDE), new Gradient (SEED, Gradient.Quality.HIGH));
  }

  /**
   * Test that the output from the test module is within the Sawtooth wave's
   * range.
   */
  public void testSawtooth () {
    assertTrue (module.getValue (TEST_X1, TEST_Y1, TEST_Z1) <= AMPLITUDE);
  }

  /**
   * Test to verify that the accessor on the Sawtooth module return something
   * useful.
   */
  public void testAccessors () {
    Sawtooth test = new Sawtooth (new Gradient (SEED, Gradient.Quality.HIGH),
        FREQUENCY, AMPLITUDE);
    assertEquals (new Gradient (SEED, Gradient.Quality.HIGH), test.getSource ());
    assertEquals (FREQUENCY, test.getFrequency ());
    assertEquals (AMPLITUDE, test.getAmplitude ());
  }

  /**
   * Test that two Sawtooth modules compare equal if constructed with equal
   * sources and the same ranges.
   */
  public void testEquals () {
    assertEquals (module, new Sawtooth (new Gradient (SEED,
        Gradient.Quality.HIGH), FREQUENCY, AMPLITUDE));
  }

  /**
   * Test that two Sawtooth modules with equal sources and ranges generate the
   * same hash code.
   */
  public void testHashCode () {
    assertEquals (module.hashCode (), new Sawtooth (new Gradient (SEED,
        Gradient.Quality.HIGH), FREQUENCY, AMPLITUDE).hashCode ());
  }
}
