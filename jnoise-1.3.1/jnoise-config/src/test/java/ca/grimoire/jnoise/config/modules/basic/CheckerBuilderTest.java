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
import ca.grimoire.jnoise.modules.basic.Checker;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Checker noise module builder.
 */
public final class CheckerBuilderTest extends TestCase {

  private static final double BLACK_VALUE_ONE = 1e99;
  private static final double BLACK_VALUE_TWO = 0.0;
  private static final double WHITE_VALUE_ONE = 1e-99;

  /**
   * Tests a fresh CheckerBuilder constructed with values and testing the
   * resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderConstructedWithValue () throws BuilderException {
    CheckerBuilder builder = new CheckerBuilder (BLACK_VALUE_ONE,
        WHITE_VALUE_ONE);

    Checker module = builder.createModule ();
    assertEquals (new Checker (BLACK_VALUE_ONE, WHITE_VALUE_ONE), module);
  }

  /**
   * Tests a fresh CheckerBuilder by providing it with initial values, then
   * overriding the black value, and testing the resulting noise module. The
   * resulting module should have the second black value as its black value.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithMultipleValues () throws BuilderException {
    CheckerBuilder builder = new CheckerBuilder ();
    builder.setBlack (BLACK_VALUE_ONE);
    builder.setWhite (WHITE_VALUE_ONE);

    assertEquals (new Checker (BLACK_VALUE_ONE, WHITE_VALUE_ONE), builder
        .createModule ());

    builder.setBlack (BLACK_VALUE_TWO);

    assertEquals (new Checker (BLACK_VALUE_TWO, WHITE_VALUE_ONE), builder
        .createModule ());
  }

  /**
   * Tests a fresh CheckerBuilder by providing it with no values and checking
   * that it throws an exception.
   */
  public void testBuilderWithNoValues () {
    ModuleBuilder builder = new CheckerBuilder ();
    try {
      builder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests a fresh CheckerBuilder by providing it with colour values and testing
   * the resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithValues () throws BuilderException {
    CheckerBuilder builder = new CheckerBuilder ();
    builder.setBlack (BLACK_VALUE_ONE);
    builder.setWhite (WHITE_VALUE_ONE);

    Checker module = builder.createModule ();
    assertEquals (new Checker (BLACK_VALUE_ONE, WHITE_VALUE_ONE), module);
  }
}
