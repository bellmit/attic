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
import ca.grimoire.jnoise.modules.basic.Integer;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Integer noise module builder.
 */
public final class IntegerBuilderTest extends TestCase {

  private static final int SEED_ONE = 5;
  private static final int SEED_TWO = -42;

  /**
   * Tests a fresh IntegerBuilder constructed with a seed and testing the
   * resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderConstructedWithSeed () throws BuilderException {
    IntegerBuilder builder = new IntegerBuilder (SEED_ONE);

    Integer module = builder.createModule ();
    assertEquals (new Integer (SEED_ONE), module);
  }

  /**
   * Tests a fresh IntegerBuilder by providing it with two seeds in order and
   * testing the resulting noise module. The resulting module should have the
   * second seed as its seed.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithMultipleSeeds () throws BuilderException {
    IntegerBuilder builder = new IntegerBuilder ();
    builder.setSeed (SEED_ONE);
    builder.setSeed (SEED_TWO);

    Integer module = builder.createModule ();
    assertEquals (new Integer (SEED_TWO), module);
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
   * Tests a fresh IntegerBuilder by providing it with a seed and testing the
   * resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithValue () throws BuilderException {
    IntegerBuilder builder = new IntegerBuilder ();
    builder.setSeed (SEED_ONE);

    Integer module = builder.createModule ();
    assertEquals (new Integer (SEED_ONE), module);
  }
}
