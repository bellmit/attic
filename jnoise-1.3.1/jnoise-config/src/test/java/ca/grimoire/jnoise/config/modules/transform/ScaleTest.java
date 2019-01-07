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
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.basic.Constant;
import ca.grimoire.jnoise.modules.transform.Scale;
import junit.framework.TestCase;

/**
 * Tests for the Scale element builder.
 */
public final class ScaleTest extends TestCase {
  private static final double V = 1.0;
  private static final double X = 2.0;
  private static final double Y = 3.0;
  private static final double Z = 4.0;

  /**
   * Tests that a Scale builder with a missing attribute constructs the right
   * module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingAttribute () throws BuilderException {
    ScaleBuilder testBuilder = new ScaleBuilder ();

    testBuilder.addChild (new ConstantBuilder (V));
    testBuilder.setX (X);
    testBuilder.setY (Y);

    assertEquals (new Scale (new Constant (V), X, Y, 1.0), testBuilder
        .createModule ());
  }

  /**
   * Tests that a Scale builder with no source cannot construct modules.
   */
  public void testMissingChild () {
    ScaleBuilder testBuilder = new ScaleBuilder ();

    testBuilder.setX (X);
    testBuilder.setY (Y);
    testBuilder.setZ (Z);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests a known valid configuration and ensures it produces the correct
   * module tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testScale () throws BuilderException {
    Scale expected = new Scale (new Constant (V), X, Y, Z);

    ScaleBuilder testBuilder = new ScaleBuilder ();

    testBuilder.addChild (new ConstantBuilder (V));
    testBuilder.setX (X);
    testBuilder.setY (Y);
    testBuilder.setZ (Z);

    assertEquals (expected, testBuilder.createModule ());
  }
}
