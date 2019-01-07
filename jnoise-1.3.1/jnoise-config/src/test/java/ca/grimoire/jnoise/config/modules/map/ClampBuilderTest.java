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
import ca.grimoire.jnoise.modules.map.Clamp;
import junit.framework.TestCase;

/**
 * Test cases for the Clamp module builder.
 */
public final class ClampBuilderTest extends TestCase {

  /**
   * Tests the construction of a known noise tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testClamp () throws BuilderException {
    Clamp expected = new Clamp (new Axis (), 1.0, 2.3);

    ClampBuilder testBuilder = new ClampBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setLower (1.0);
    testBuilder.setUpper (2.3);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Verifies that the ClampBuilder under test doesn't allow sourceless modules
   * to be created.
   */
  public void testMissingChild () {
    ClampBuilder testBuilder = new ClampBuilder ();

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the ClampBuilder under test doesn't allow modules to be
   * created without both limits defined.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingLimit () throws BuilderException {
    ClampBuilder testBuilder = new ClampBuilder ();
    testBuilder.addChild (new AxisBuilder ());

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the ClampBuilder under test doesn't allow modules to be
   * created with the limits in the incorrect order.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testReverseLimit () throws BuilderException {
    ClampBuilder testBuilder = new ClampBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setUpper (1.0);
    testBuilder.setLower (2.3);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
