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
import ca.grimoire.jnoise.config.modules.basic.RadiusBuilder;
import ca.grimoire.jnoise.modules.basic.Radius;
import ca.grimoire.jnoise.modules.map.Absolute;
import junit.framework.TestCase;

/**
 * Test cases for the Absolute module builder.
 */
public final class AbsoluteBuilderTest extends TestCase {

  /**
   * Tests the construction of a known noise tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAbsolute () throws BuilderException {
    Absolute expected = new Absolute (new Radius ());

    AbsoluteBuilder testBuilder = new AbsoluteBuilder ();
    testBuilder.addChild (new RadiusBuilder ());

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Verifies that the AbsoluteBuilder under test doesn't allow sourceless
   * modules to be created.
   */
  public void testMissingChild () {
    AbsoluteBuilder testBuilder = new AbsoluteBuilder ();

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
