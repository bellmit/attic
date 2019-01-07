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
 * Copyright (C) 2006 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.modules.basic;

import junit.framework.TestCase;

/**
 * Test cases verifying the properties of the GradientVectorTable vector table
 * class.
 */
public final class GradientVectorTableTest extends TestCase {

  private static final double EPSILON = 0.000005;

  /**
   * Test that a sample of the available vectors are unit vectors.
   */
  public void testUnitVectors () {
    GradientVectorTable table = new GradientVectorTable ();
    for (int vector = 0; vector < 0xFF; ++vector) {
      double x = table.getX (vector);
      double z = table.getY (vector);
      double y = table.getZ (vector);

      assertEquals (1.0, x * x + y * y + z * z, EPSILON);
    }
  }
}
