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
 * Validates the default type conversion strategy's approach to
 * string-constructable classes.
 */
public final class DefaultTypeStrategyStringTest extends DefaultTypeStrategyFixture {

  /**
   * A class which does not meet the requirements for string constructability by
   * virtue of having no string-only constructors.
   */
  private static final class NonStringConstructable {
    /**
     * Creates a new NonStringConstructable.
     */
    public NonStringConstructable () {

    }

    /**
     * Creates a new NonStringConstructable with a value. The Object argument is
     * assignable from String but should not be accepted as a construction
     * method.
     * 
     * @param value
     *          unused object argument.
     */
    @SuppressWarnings ("unused")
    public NonStringConstructable (Object value) {

    }

    /**
     * Creates a new NonStringConstructable with some values. This has a String
     * argument first to catch a particular class of bug.
     * 
     * @param value
     *          unused string argument.
     * @param bonus
     *          unused boolean argument.
     */
    @SuppressWarnings ("unused")
    public NonStringConstructable (String value, boolean bonus) {

    }
  }

  private static final class StringConstructable {
    /**
     * Creates a new StringConstructable object with <code>null</code> as its
     * value.
     */
    public StringConstructable () {
      this.value = null;
    }

    /**
     * Creates a new StringConstructable object with the passed string as its
     * value.
     * 
     * @param value
     *          the string to use as a value.
     */
    public StringConstructable (String value) {
      assert (value != null);

      this.value = value;
    }

    /**
     * Compares this object for equality. StringConstructibles are only equal to
     * other StringConstructibles with equal values.
     * 
     * @param object
     *          the object to compare with.
     * @return <code>true</code> if <var>object</var> is equivalent to
     *         <var>this</var>.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (Object object) {
      if (object == null)
        return false;
      if (!(object instanceof StringConstructable))
        return false;

      StringConstructable other = (StringConstructable) object;

      if (value == null)
        return value == other.value;

      return value.equals (other.value);
    }

    private String value;
  }

  /**
   * Tests the string constructor strategy with an empty string to ensure it
   * invokes the correct constructor.
   */
  public void testEmptyStringConstructor () {
    Object converted = testStrategy.convert (StringConstructable.class, "");
    assertEquals (new StringConstructable (), converted);
  }

  /**
   * Tests the string constructor strategy with a non-constructible class. The
   * strategy implementation should fail with an IllegalArgumentException.
   */
  public void testNonStringConstructible () {
    try {
      testStrategy.convert (NonStringConstructable.class, "a string");
      fail ();
    } catch (IllegalArgumentException iae) {
      // Success case.
    }
  }

  /**
   * Tests the string constructor strategy with a known string.
   */
  public void testStringConstructor () {
    Object converted = testStrategy.convert (StringConstructable.class,
        "test string");
    assertEquals (new StringConstructable ("test string"), converted);
  }

}
