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

/**
 * Validates the default type strategy's behaviour with regards to primitive
 * types.
 */
public final class DefaultTypeStrategyPrimitiveTest extends
    DefaultTypeStrategyFixture {

  /**
   * Tests valid string PrimitiveConversion to Boolean instances.
   */
  public void testBooleanPrimitiveConversion () {
    Object converted = testStrategy.convert (boolean.class, "true");
    assertEquals (Boolean.TRUE, converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Byte instances.
   */
  public void testBytePrimitiveConversion () {
    Object converted = testStrategy.convert (byte.class, "6");
    assertEquals (Byte.valueOf ((byte) 6), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Character instances.
   */
  public void testCharPrimitiveConversion () {
    Object converted = testStrategy.convert (char.class, "X");
    assertEquals (Character.valueOf ('X'), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Double instances.
   */
  public void testDoublePrimitiveConversion () {
    Object converted = testStrategy.convert (double.class, "-200.3");
    assertEquals (Double.valueOf (-200.3), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Float instances.
   */
  public void testFloatPrimitiveConversion () {
    Object converted = testStrategy.convert (float.class, "1.23e-4");
    assertEquals (Float.valueOf (1.23e-4f), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Integer instances.
   */
  public void testIntPrimitiveConversion () {
    Object converted = testStrategy.convert (int.class, "-205100");
    assertEquals (Integer.valueOf (-205100), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Long instances.
   */
  public void testLongPrimitiveConversion () {
    Object converted = testStrategy.convert (long.class, "4572978527");
    assertEquals (Long.valueOf (4572978527L), converted);
  }

  /**
   * Tests valid string PrimitiveConversion to Short instances.
   */
  public void testShortPrimitiveConversion () {
    Object converted = testStrategy.convert (short.class, "600");
    assertEquals (Short.valueOf ((short) 600), converted);
  }

}
