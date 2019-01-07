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
 * Type conversion strategy for turning Strings into instances of primitive
 * wrappers.
 */
final class PrimitiveTypeStrategy implements TypeConversionStrategy {

  /**
   * Converts a string to a Boolean.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Boolean.
   * @see java.lang.Boolean#valueOf(java.lang.String)
   */
  public static Boolean coerceBoolean (String value) {
    assert (value != null);

    try {
      return Boolean.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Byte.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Byte.
   * @see java.lang.Byte#valueOf(java.lang.String)
   */
  public static Byte coerceByte (String value) {
    assert (value != null);

    try {
      return Byte.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Character. The string must be one character long.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Character.
   * @see java.lang.Character#valueOf(char)
   */
  public static Character coerceCharacter (String value) {
    assert (value != null);

    if (value.length () != 1)
      throw new IllegalArgumentException (value);

    try {
      return Character.valueOf (value.charAt (0));
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Double.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Double.
   * @see java.lang.Double#valueOf(java.lang.String)
   */
  public static Double coerceDouble (String value) {
    assert (value != null);

    try {
      return Double.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Float.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Float.
   * @see java.lang.Float#valueOf(java.lang.String)
   */
  public static Float coerceFloat (String value) {
    assert (value != null);

    try {
      return Float.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Integer.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Integer.
   * @see java.lang.Integer#valueOf(java.lang.String)
   */
  public static Integer coerceInteger (String value) {
    assert (value != null);

    try {
      return Integer.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Long.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Long.
   * @see java.lang.Long#valueOf(java.lang.String)
   */
  public static Long coerceLong (String value) {
    assert (value != null);

    try {
      return Long.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * Converts a string to a Short.
   * 
   * @param value
   *          the string to convert.
   * @return the corresponding Short.
   * @see java.lang.Short#valueOf(java.lang.String)
   */
  public static Short coerceShort (String value) {
    assert (value != null);

    try {
      return Short.valueOf (value);
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException (value);
    }
  }

  /**
   * If the passed type is a primitive type, converts <var>value</var> into an
   * instance of the corresponding wrapper. Otherwise, this throws
   * IllegalArgumentException.
   * 
   * @param type
   *          the type to convert to.
   * @param value
   *          the string to convert.
   * @return an appropriate primitive wrapper with the converted value.
   * @throws IllegalArgumentException
   *           if the passed value is not convertible or the passed type is not
   *           a primitive type.
   */
  public Object convert (Class<?> type, String value) {

    if (byte.class.equals (type))
      return coerceByte (value);
    if (short.class.equals (type))
      return coerceShort (value);
    if (int.class.equals (type))
      return coerceInteger (value);
    if (long.class.equals (type))
      return coerceLong (value);

    if (float.class.equals (type))
      return coerceFloat (value);
    if (double.class.equals (type))
      return coerceDouble (value);

    if (char.class.equals (type))
      return coerceCharacter (value);

    if (boolean.class.equals (type))
      return coerceBoolean (value);

    throw new IllegalArgumentException (type.getName ());
  }

}
