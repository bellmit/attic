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
package ca.grimoire.jnoise.config.conversion;

import junit.framework.TestCase;

/**
 * Tests the wrapper type conversion strategy.
 */
public final class WrapperTypeStrategyTest extends TestCase {

  /**
   * Tests valid string conversion to Boolean instances.
   */
  public void testBooleanConversion () {
    Object converted = testStrategy.convert (Boolean.class, "true");
    assertEquals (Boolean.TRUE, converted);
  }

  /**
   * Tests valid string conversion to Byte instances.
   */
  public void testByteConversion () {
    Object converted = testStrategy.convert (Byte.class, "6");
    assertEquals (Byte.valueOf ((byte) 6), converted);
  }

  /**
   * Tests valid string conversion to Character instances.
   */
  public void testCharConversion () {
    Object converted = testStrategy.convert (Character.class, "X");
    assertEquals (Character.valueOf ('X'), converted);
  }

  /**
   * Tests valid string conversion to Double instances.
   */
  public void testDoubleConversion () {
    Object converted = testStrategy.convert (Double.class, "-200.3");
    assertEquals (Double.valueOf (-200.3), converted);
  }

  /**
   * Tests valid string conversion to Float instances.
   */
  public void testFloatConversion () {
    Object converted = testStrategy.convert (Float.class, "1.23e-4");
    assertEquals (Float.valueOf (1.23e-4f), converted);
  }

  /**
   * Tests valid string conversion to Integer instances.
   */
  public void testIntConversion () {
    Object converted = testStrategy.convert (Integer.class, "-205100");
    assertEquals (Integer.valueOf (-205100), converted);
  }

  /**
   * Tests that unsupported types generate an exception.
   */
  public void testInvalidConversion () {
    try {
      testStrategy.convert (Object.class, "as if");

      fail ();
    } catch (IllegalArgumentException iae) {
      // Success case
    }
  }

  /**
   * Tests valid string conversion to Long instances.
   */
  public void testLongConversion () {
    Object converted = testStrategy.convert (Long.class, "4572978527");
    assertEquals (Long.valueOf (4572978527L), converted);
  }

  /**
   * Tests valid string conversion to Short instances.
   */
  public void testShortConversion () {
    Object converted = testStrategy.convert (Short.class, "600");
    assertEquals (Short.valueOf ((short) 600), converted);
  }

  /**
   * Creates a WrapperTypeStrategy for the test.
   * 
   * @throws Exception
   *           if the base test setup fails.
   */
  @Override
  protected void setUp () throws Exception {
    super.setUp ();

    testStrategy = new WrapperTypeStrategy ();
  }

  private WrapperTypeStrategy testStrategy;
}
