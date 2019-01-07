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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * A type conversion strategy that can construct arbitrary enums from strings
 * using the language-supplied valueOf method.
 */
final class EnumTypeStrategy implements TypeConversionStrategy {

  private static final String FACTORY_NAME = "valueOf";
  private static final Class<?>[] FACTORY_SIGNATURE = new Class<?>[] {String.class};
  
  /**
   * Converts the passed string to an enum member.
   * 
   * @param type
   *          the enum type to convert to.
   * @param value
   *          the string to convert.
   * @return the equivalent enum member.
   * @throws IllegalArgumentException
   *           if the string is not convertable or the class is not an enum.
   * @see jnoise.config.conversion.TypeConversionStrategy#convert(java.lang.Class,
   *      java.lang.String)
   */
  public Object convert (Class<?> type, String value) {
    assert (type != null);

    Method converter = getStaticValueOf (type);

    return convert (converter, value);
  }

  private Object convert (Method converter, String value) {
    try {
      return converter.invoke (null, value);
    } catch (IllegalAccessException iae) {
      throw new IllegalArgumentException (iae);
    } catch (InvocationTargetException ite) {
      // Yig. The underlying conversion method threw an exception. What to do,
      // what to do... Unwrap it! No reason our caller should care about
      // introspection/reflection stuff, that's the whole point of this class.
      // Unfortunately this means we need throws Exception to allow wrapped
      // builders to throw domain-specific exceptions with impunity.
      Throwable cause = ite.getCause ();
      if (cause instanceof RuntimeException)
        throw (RuntimeException) cause;
      if (cause instanceof Exception)
        throw new IllegalArgumentException (cause);

      assert (cause instanceof Error);
      throw (Error) cause;
    }
  }
  private Method getStaticValueOf (Class<?> type) {
      Method[] methods = type.getMethods ();
      for (Method method : methods)
        if ((method.getModifiers () & Modifier.STATIC) != 0)
          if (FACTORY_NAME.equals(method.getName ()))
            if (Arrays.equals (FACTORY_SIGNATURE, method.getParameterTypes ()))
              return method;
      
      throw new IllegalArgumentException (type.getName ());
  }
}
