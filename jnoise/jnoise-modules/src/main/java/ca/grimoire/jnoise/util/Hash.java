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
package ca.grimoire.jnoise.util;

/**
 * Utility methods for hashing various data types.
 */
public final class Hash {

  /**
   * Generates a hashCode for a double-precision value. The value is converted
   * to a <code>long</code>, then the top four bytes and bottom four bytes
   * are XORed together to generate an <code>int</code>-sized hash value.
   * <p>
   * This implementation will generate discrete hash values for NaN, +Inf, and
   * -Inf.
   * 
   * @param value
   *          the value to hash.
   * @return a 32-bit hash of <code>value</code>.
   */
  public static int hashDouble (double value) {
    long bits = Double.doubleToRawLongBits (value);
    int hihalf = (int) ((bits >> 32) & 0xFFFFFFFFL);
    int lohalf = (int) (bits & 0xFFFFFFFFL);
    return hihalf ^ lohalf;
  }

}
