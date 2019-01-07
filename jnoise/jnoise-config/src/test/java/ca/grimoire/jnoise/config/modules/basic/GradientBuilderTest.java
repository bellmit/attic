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
package ca.grimoire.jnoise.config.modules.basic;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Gradient;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Gradient noise module builder.
 */
public final class GradientBuilderTest extends TestCase {

  private static final int              SEED_ONE    = 5;
  private static final int              SEED_TWO    = -42;

  private static final Gradient.Quality QUALITY_ONE = Gradient.Quality.MEDIUM;
  private static final Gradient.Quality QUALITY_TWO = Gradient.Quality.HIGH;

  /**
   * Tests a fresh GradientBuilder constructed with a seed and quality and
   * testing the resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderConstructedWithSettings () throws BuilderException {
    GradientBuilder builder = new GradientBuilder (SEED_ONE, QUALITY_ONE);

    Gradient module = builder.createModule ();
    assertEquals (new Gradient (SEED_ONE, QUALITY_ONE), module);
  }

  /**
   * Tests a fresh GradientBuilder by providing it with two seeds in order and
   * testing the resulting noise module. The resulting module should have the
   * second seed as its seed.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithMultipleSeeds () throws BuilderException {
    GradientBuilder builder = new GradientBuilder ();

    builder.setSeed (SEED_ONE);
    builder.setQuality (QUALITY_ONE);

    builder.setSeed (SEED_TWO);

    Gradient module = builder.createModule ();
    assertEquals (new Gradient (SEED_TWO, QUALITY_ONE), module);
  }

  /**
   * Tests a fresh GradientBuilder by providing it with two quality in order and
   * testing the resulting noise module. The resulting module should have the
   * second gradient as its seed.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithMultipleQualities () throws BuilderException {
    GradientBuilder builder = new GradientBuilder ();

    builder.setSeed (SEED_ONE);
    builder.setQuality (QUALITY_ONE);

    builder.setQuality (QUALITY_TWO);

    Gradient module = builder.createModule ();
    assertEquals (new Gradient (SEED_ONE, QUALITY_TWO), module);
  }

  /**
   * Tests a fresh IntegerBuilder by providing it with no values and checking
   * that it throws an exception.
   */
  public void testBuilderWithNoSeed () {
    ModuleBuilder builder = new IntegerBuilder ();
    try {
      builder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests a fresh GradientBuilder by providing it with a seed and quality and
   * testing the resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithValue () throws BuilderException {
    GradientBuilder builder = new GradientBuilder ();
    builder.setSeed (SEED_ONE);
    builder.setQuality (QUALITY_ONE);

    Gradient module = builder.createModule ();
    assertEquals (new Gradient (SEED_ONE, QUALITY_ONE), module);
  }
}
