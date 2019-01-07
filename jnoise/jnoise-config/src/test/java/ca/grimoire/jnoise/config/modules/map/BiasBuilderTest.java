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
import ca.grimoire.jnoise.modules.map.Bias;
import junit.framework.TestCase;

/**
 * Test cases for the Bias module builder.
 */
public final class BiasBuilderTest extends TestCase {

  /**
   * Tests the construction of a known noise tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testBias () throws BuilderException {
    Bias expected = new Bias (new Axis (), 1.0, 2.3);

    BiasBuilder testBuilder = new BiasBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.setScale (1.0);
    testBuilder.setTranslation (2.3);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Verifies that the BiasBuilder under test doesn't allow sourceless modules
   * to be created.
   */
  public void testMissingChild () {
    BiasBuilder testBuilder = new BiasBuilder ();

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
