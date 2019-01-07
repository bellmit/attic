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
package ca.grimoire.jnoise.config.modules.transform;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.basic.IntegerBuilder;
import junit.framework.TestCase;

/**
 * Test cases for the transformation description base class.
 */
public final class TransformDescriptionTest extends TestCase {

  private static final double X = -7;
  private static final double Y = 1.5;
  private static final double Z = 23.7;

  /**
   * Tests a description created with no values after setting a value.
   */
  public void testConfiguredTransformDescription () {
    TransformDescription testDesc = new TransformDescription ();

    testDesc.setX (X);
    assertEquals (X, testDesc.getX ());
  }

  /**
   * Verifies that the no argument constructor creates the right configuration.
   */
  public void testTransformDescription () {
    TransformDescription testDesc = new TransformDescription ();

    assertEquals (0.0, testDesc.getX ());
    assertEquals (0.0, testDesc.getY ());
    assertEquals (0.0, testDesc.getZ ());

    try {
      testDesc.getSource ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that arguments passed to the constructor create an initially
   * readable description.
   */
  public void testTransformDescriptionCompsConstructor () {
    TransformDescription testDesc = new TransformDescription (X, Y, Z);
    assertEquals (X, testDesc.getX ());
    assertEquals (Y, testDesc.getY ());
    assertEquals (Z, testDesc.getZ ());
  }

  /**
   * Verifies that a module element passed to a transformation element's
   * addChild method is accepted.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddModuleElement () throws BuilderException {
    IntegerBuilder child = new IntegerBuilder ();

    TransformDescription testDesc = new TransformDescription ();
    testDesc.addChild (child);

    assertSame (child, testDesc.getSource ());
  }
}
