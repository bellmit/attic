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

import junit.framework.TestCase;

/**
 * Test cases for the Hash utility class.
 */
public class HashTest extends TestCase {

  private static final double VALUE_1 = 5.2;
  private static final double VALUE_2 = 5.2e42;

  /**
   * Test that the hashDouble method produces distinct hash values for a variety
   * of values.
   */
  public void testHashDouble () {
    assertTrue (Hash.hashDouble (VALUE_1) != Hash.hashDouble (VALUE_2));
    assertTrue (Hash.hashDouble (Double.NEGATIVE_INFINITY) != Hash
        .hashDouble (Double.NaN));
    assertTrue (Hash.hashDouble (Double.POSITIVE_INFINITY) != Hash
        .hashDouble (Double.NaN));
    assertTrue (Hash.hashDouble (Double.NEGATIVE_INFINITY) != Hash
        .hashDouble (Double.POSITIVE_INFINITY));
  }

}
