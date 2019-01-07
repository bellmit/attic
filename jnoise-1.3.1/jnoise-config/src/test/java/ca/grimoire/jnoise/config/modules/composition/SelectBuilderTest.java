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
import ca.grimoire.jnoise.config.modules.basic.AxisBuilder;
import ca.grimoire.jnoise.config.modules.basic.GradientBuilder;
import ca.grimoire.jnoise.config.modules.basic.RadiusBuilder;
import ca.grimoire.jnoise.modules.basic.Axis;
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.basic.Radius;
import ca.grimoire.jnoise.modules.composition.Select;
import junit.framework.TestCase;

/**
 * Tests for the Select element builder.
 */
public final class SelectBuilderTest extends TestCase {

  /**
   * Creates a select element with a known, valid collection of settings and
   * compares the result with what it should be.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testSelect () throws BuilderException {
    Select expected = new Select (new Axis (), new Radius (), new Gradient (5,
        Gradient.Quality.HIGH), 0.0, 0.2);

    SelectBuilder testBuilder = new SelectBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    testBuilder.addChild (new GradientBuilder (5, Gradient.Quality.HIGH));
    testBuilder.setThreshold (0.0);
    testBuilder.setFalloff (0.2);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Creates a select element with a missing child and ensures it correctly
   * fails to create a module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingSource () throws BuilderException {
    SelectBuilder testBuilder = new SelectBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    // oops!
    testBuilder.setThreshold (0.0);
    testBuilder.setFalloff (0.2);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Creates a select element with no selection threshold and ensures it fails
   * to create a module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingThreshold () throws BuilderException {
    SelectBuilder testBuilder = new SelectBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    testBuilder.addChild (new GradientBuilder (5, Gradient.Quality.HIGH));
    // oops!
    testBuilder.setFalloff (0.2);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Creates a select element with no falloff and ensures it fails to create a
   * module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMissingFalloff () throws BuilderException {
    SelectBuilder testBuilder = new SelectBuilder ();
    testBuilder.addChild (new AxisBuilder ());
    testBuilder.addChild (new RadiusBuilder ());
    testBuilder.addChild (new GradientBuilder (5, Gradient.Quality.HIGH));
    testBuilder.setThreshold (0.0);
    // oops!

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

}
