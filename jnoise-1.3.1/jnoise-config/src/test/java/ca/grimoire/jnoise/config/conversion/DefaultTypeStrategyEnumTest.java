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
 * Validates the default type strategy's approach to enumerated types.
 */
public final class DefaultTypeStrategyEnumTest extends DefaultTypeStrategyFixture {

  private static enum TestEnum {
    /** Element three. */
    FIVE,
    /** Element one. */
    ONE,
    /** Element two. */
    TWO
  }

  /**
   * Tests that the enum strategy can convert to an element of a known
   * enumeration.
   */
  public void testConvertsFive () {
    Object converted = testStrategy.convert (TestEnum.class, "FIVE");
    assertEquals (TestEnum.FIVE, converted);
  }

  /**
   * Tests that the enum strategy can convert to an element of a known
   * enumeration.
   */
  public void testConvertsOne () {
    Object converted = testStrategy.convert (TestEnum.class, "ONE");
    assertEquals (TestEnum.ONE, converted);
  }

  /**
   * Tests that the enum strategy can convert to an element of a known
   * enumeration.
   */
  public void testConvertsTwo () {
    Object converted = testStrategy.convert (TestEnum.class, TestEnum.TWO
        .toString ());
    assertEquals (TestEnum.TWO, converted);
  }

  /**
   * Tests that the enum strategy correctly rejects an unknown element of a
   * known enumeration.
   */
  public void testDoesntConvertInvalidEnumElements () {
    try {
      testStrategy.convert (TestEnum.class, "THREE");
      fail ();
    } catch (IllegalArgumentException iae) {
      // Success case
    }
  }

}
