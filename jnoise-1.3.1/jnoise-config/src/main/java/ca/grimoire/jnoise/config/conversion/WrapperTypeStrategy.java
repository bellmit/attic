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

import java.util.HashMap;
import java.util.Map;

/**
 * Type conversion strategy for turning Strings into instances of wrapper types
 * directly.
 */
final class WrapperTypeStrategy implements TypeConversionStrategy {

  private static final Map<Class<?>, Class<?>> primitives;

  static {
    primitives = new HashMap<Class<?>, Class<?>> ();

    primitives.put (Byte.class, Byte.TYPE);
    primitives.put (Short.class, Short.TYPE);
    primitives.put (Integer.class, Integer.TYPE);
    primitives.put (Long.class, Long.TYPE);

    primitives.put (Float.class, Float.TYPE);
    primitives.put (Double.class, Double.TYPE);

    primitives.put (Boolean.class, Boolean.TYPE);

    primitives.put (Character.class, Character.TYPE);
  }

  /**
   * If the passed type is a wrapper type, converts <var>value</var> into an
   * instance of the wrapper. Otherwise, this throws IllegalArgumentException.
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
    assert (type != null);
    assert (value != null);

    Class<?> primitive = primitives.get (type);
    if (primitive == null)
      throw new IllegalArgumentException (type.getName ());

    return baseStrategy.convert (primitive, value);
  }

  private final PrimitiveTypeStrategy baseStrategy = new PrimitiveTypeStrategy ();
}
