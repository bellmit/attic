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
package ca.grimoire.jnoise.config.modules.composition;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.basic.*;
import ca.grimoire.jnoise.modules.basic.*;
import ca.grimoire.jnoise.modules.composition.Displace;
import junit.framework.TestCase;

/**
 * Tests for the Displace element builder.
 */
public final class DisplaceBuilderTest extends TestCase {

  /**
   * Creates a Displace element with a known, valid collection of settings and
   * compares the result with what it should be.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testDisplace () throws BuilderException {
    Displace expected = new Displace (new Axis (), new Radius (), new Gradient (
        5, Gradient.Quality.HIGH), new Plane ());

    DisplaceBuilder testBuilder = new DisplaceBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    testBuilder.addChild (new GradientBuilder (5, Gradient.Quality.HIGH));
    testBuilder.addChild (new PlaneBuilder ());

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Creates a Displace element with a missing child and ensures it correctly
   * fails to create a module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingSource () throws BuilderException {
    DisplaceBuilder testBuilder = new DisplaceBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    // oops!
    testBuilder.addChild (new PlaneBuilder ());

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
