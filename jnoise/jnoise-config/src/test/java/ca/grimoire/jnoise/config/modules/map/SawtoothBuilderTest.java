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
package ca.grimoire.jnoise.config.modules.map;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.basic.AxisBuilder;
import ca.grimoire.jnoise.modules.basic.Axis;
import ca.grimoire.jnoise.modules.map.Sawtooth;
import junit.framework.TestCase;

/**
 * Test cases for the Sawtooth module builder.
 */
public final class SawtoothBuilderTest extends TestCase {

  /**
   * Tests the construction of a known noise tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testSawtooth () throws BuilderException {
    Sawtooth expected = new Sawtooth (new Axis (), 1.0, 2.3);

    SawtoothBuilder testBuilder = new SawtoothBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setAmplitude (2.3);
    testBuilder.setFrequency (1.0);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Verifies that the SawtoothBuilder under test doesn't allow sourceless
   * modules to be created.
   */
  public void testMissingChild () {
    SawtoothBuilder testBuilder = new SawtoothBuilder ();
    testBuilder.setAmplitude (2.3);
    testBuilder.setFrequency (1.0);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the SawtoothBuilder under test doesn't allow modules to be
   * created without amplitude defined.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingAmplitude () throws BuilderException {
    SawtoothBuilder testBuilder = new SawtoothBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setFrequency (1.0);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the SawtoothBuilder under test doesn't allow modules to be
   * created without frequency defined.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingFrequency () throws BuilderException {
    SawtoothBuilder testBuilder = new SawtoothBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setAmplitude (2.3);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the SawtoothBuilder under test doesn't allow modules to be
   * created with a negative frequency.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testNegativeFrequency () throws BuilderException {
    SawtoothBuilder testBuilder = new SawtoothBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setAmplitude (2.3);
    testBuilder.setFrequency (-1.0);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
