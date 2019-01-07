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
package ca.grimoire.jnoise.config.modules.generation;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.modules.generation.Cylinder;
import junit.framework.TestCase;

/**
 * Test cases for the Cylinder module builder.
 */
public final class CylinderBuilderTest extends TestCase {

  /**
   * Tests the construction of a known noise generator.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testCylinder () throws BuilderException {
    Cylinder expected = new Cylinder (1.0, 2.3);

    CylinderBuilder testBuilder = new CylinderBuilder ();
    testBuilder.setAmplitude (2.3);
    testBuilder.setFrequency (1.0);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Verifies that the CylinderBuilder under test doesn't allow modules to be
   * created without amplitude defined.
   */
  public void testMissingAmplitude () {
    CylinderBuilder testBuilder = new CylinderBuilder ();

    testBuilder.setFrequency (1.0);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that the CylinderBuilder under test doesn't allow modules to be
   * created without frequency defined.
   */
  public void testMissingFrequency () {
    CylinderBuilder testBuilder = new CylinderBuilder ();

    testBuilder.setAmplitude (2.3);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
