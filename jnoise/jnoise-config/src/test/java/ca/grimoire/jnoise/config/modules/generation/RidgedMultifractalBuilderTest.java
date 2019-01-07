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
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.generation.RidgedMultifractal;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the RidgedMultifractal noise module
 * builder.
 */
public final class RidgedMultifractalBuilderTest extends TestCase {
  /**
   * Tests that the builder fails if the frequency has not been set but weight
   * has.
   */
  public void testMissingFrequency () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setWeight (-1.2);

    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created without lacunarity set.
   */
  public void testMissingLacunarity () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);

    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created without an octave count set.
   */
  public void testMissingOctaves () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);

    testBuilder.setLacunarity (2.0);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created without quality set.
   */
  public void testMissingQuality () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created without a seed set.
   */
  public void testMissingSeed () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();

    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that the builder fails if the weight has not been set but frequency
   * has.
   */
  public void testMissingWeight () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);

    testBuilder.setFrequency (0.8);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created with a negative octave count set.
   */
  public void testNegativeOctaves () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (-2);
    testBuilder.setLacunarity (2.0);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that RidgedMultifractal builders reject child elements.
   */
  public void testRejectsChildren () {
    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    try {
      testBuilder.addChild (new RidgedMultifractalBuilder ());
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests the creation of a known noise module.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  @SuppressWarnings ("deprecation")
  public void testRidgedMultifractal () throws BuilderException {
    RidgedMultifractal expected = new RidgedMultifractal (-43, 8, 2.0,
        Gradient.Quality.HIGH);

    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Tests the creation of a known noise module with weight and frequency.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testRidgedMultifractalFrequency () throws BuilderException {
    RidgedMultifractal expected = new RidgedMultifractal (-43, 8, 2.0, -1.2,
        0.8, Gradient.Quality.HIGH);

    RidgedMultifractalBuilder testBuilder = new RidgedMultifractalBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setWeight (-1.2);
    testBuilder.setFrequency (0.8);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    assertEquals (expected, testBuilder.createModule ());
  }
}
