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
 * A strategy for converting string representations to objects appropriate for
 * the a Class.
 */
public interface TypeConversionStrategy {

  /**
   * Converts <var>value</var> to an instance of <var>type</var>. Type
   * conversion implementations are not required to support all types;
   * unsupported types will be indicated by an IllegalArgumentException.
   * 
   * @param type
   *          the type to coerce to.
   * @param value
   *          the string to coerce.
   * @return the coerced value.
   * @throws IllegalArgumentException
   *           if the value cannot be coerced.
   */
  public Object convert (Class<?> type, String value);

}
