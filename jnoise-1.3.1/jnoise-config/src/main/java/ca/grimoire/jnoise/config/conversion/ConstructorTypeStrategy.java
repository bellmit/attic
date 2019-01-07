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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A type conversion strategy that uses the target type's constructors. This can
 * convert Strings to any type which has a declared, visible constructor taking
 * one String argument.
 * <p>
 * As a special case, <code>null</code> or <code>""</code> are converted to
 * the default constructor instead.
 */
final class ConstructorTypeStrategy implements TypeConversionStrategy {

  private static final Class[] NO_ARGS_SIGNATURE = new Class<?>[] {};
  private static final String NOARGS_STRING = "";
  private static final Class[] STRING_ARGS_SIGNATURE = new Class<?>[] {String.class};

  /**
   * Converts the <var>value</var> to <var>type</var> by constructing a new
   * instance of <var>type</var> using a single-argument constructor taking
   * String.
   * 
   * @param type
   *          the type to construct.
   * @param value
   *          the string to construct with.
   * @return a <var>type</var> constructed from <var>value</var>.
   * @throws IllegalArgumentException
   *           if the type is not constructable from the passed String.
   * @see jnoise.config.conversion.TypeConversionStrategy#convert(java.lang.Class,
   *      java.lang.String)
   */
  public Object convert (Class<?> type, String value) {
    assert (type != null);

    if (value == null)
      return constructWithNoArgs (type);

    if (value.length () == 0)
      return constructWithEmptyString (type);

    return constructWithString (type, value);
  }

  private Object constructWithEmptyString (Class<?> type) {
    Constructor<?> noArgs = getConstructor (type, NO_ARGS_SIGNATURE);
    if (noArgs == null)
      return constructWithString (type, NOARGS_STRING);
    
    return invokeConstructor (type, noArgs, new Object[] {});
  }

  private Object constructWithNoArgs (Class<?> type) {
    Constructor<?> noArgs = getConstructor (type, NO_ARGS_SIGNATURE);
    if (noArgs == null)
      throw new IllegalArgumentException (type.getName ());
    
    return invokeConstructor (type, noArgs, new Object[] {});
  }
  
  private Object constructWithString (Class<?> type, String value) {
    Constructor<?> stringArgs = getConstructor (type, STRING_ARGS_SIGNATURE);
    if (stringArgs == null)
      throw new IllegalArgumentException (type.getName ());

    return invokeConstructor (type, stringArgs, new Object[] {value});
  }

  private Constructor<?> getConstructor (Class<?> type, Class... signature) {
    try {
      return type.getDeclaredConstructor (signature);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  private Object invokeConstructor (Class<?> type, Constructor<?> constructor,
      Object[] args) {
    try {
      return constructor.newInstance (args);
    } catch (InstantiationException ie) {
      // Abstracts are illegal.
      throw new IllegalArgumentException (type.getName ());
    } catch (IllegalAccessException iae) {
      throw new IllegalArgumentException (type.getName ());
    } catch (InvocationTargetException ite) {
      // Yig. The underlying constructor threw an exception. What to do, what to
      // do... Unwrap it! No reason our caller should care about
      // introspection/reflection stuff, that's the whole point of this class.
      // Unfortunately this means we need throws Exception to allow wrapped
      // builders to throw domain-specific exceptions with impunity.
      Throwable cause = ite.getCause ();
      if (cause instanceof RuntimeException)
        throw (RuntimeException) cause;
      if (cause instanceof Exception)
        throw new IllegalArgumentException (type.getName (), cause);
      
      assert (cause instanceof Error);
      throw (Error) cause;
    }
  }
}
