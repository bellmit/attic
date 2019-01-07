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
import ca.grimoire.jnoise.modules.generation.Perlin;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Perlin noise module builder.
 */
public final class PerlinBuilderTest extends TestCase {
  /**
   * Tests that modules cannot be created without lacunarity set.
   */
  public void testMissingLacunarity () {
    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);

    testBuilder.setPersistence (0.5);
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
    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);

    testBuilder.setLacunarity (2.0);
    testBuilder.setPersistence (0.5);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests that modules cannot be created without persistence set.
   */
  public void testMissingPersistence () {
    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);
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
   * Tests that modules cannot be created without quality set.
   */
  public void testMissingQuality () {
    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setPersistence (0.5);

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
    PerlinBuilder testBuilder = new PerlinBuilder ();

    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setPersistence (0.5);
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
    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (-2);
    testBuilder.setLacunarity (2.0);
    testBuilder.setPersistence (0.5);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    try {
      testBuilder.createModule ();
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
  public void testPerlin () throws BuilderException {
    Perlin expected = new Perlin (-43, 8, 2.0, 0.5, Gradient.Quality.HIGH);

    PerlinBuilder testBuilder = new PerlinBuilder ();
    testBuilder.setSeed (-43);
    testBuilder.setOctaves (8);
    testBuilder.setLacunarity (2.0);
    testBuilder.setPersistence (0.5);
    testBuilder.setQuality (Gradient.Quality.HIGH);

    assertEquals (expected, testBuilder.createModule ());
  }

  /**
   * Tests that Perlin builders reject child elements.
   */
  public void testRejectsChildren () {
    PerlinBuilder testBuilder = new PerlinBuilder ();
    try {
      testBuilder.addChild (new PerlinBuilder ());
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
