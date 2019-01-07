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
package ca.grimoire.jnoise.models;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Integer;
import junit.framework.TestCase;

/**
 * Test cases for the PlaneModel coordinate mapping tool.
 */
public class PlaneModelTest extends TestCase {

  private static final double     ORIGIN_X = 1, ORIGIN_Y = -2, ORIGIN_Z = 30;
  private static final int        SEED     = 523;
  private static final int        STEPS    = 500;
  private static final double     U        = 1.0, V = 1.0;
  private static final double     U_X      = 1, U_Y = 0.5, U_Z = -0.5;
  private static final double     V_X      = -0.25, V_Y = 0.5, V_Z = 0.25;
  private static final Module     MODULE   = new Integer (SEED);
  private static final PlaneModel MODEL    = new PlaneModel (MODULE, ORIGIN_X,
                                               ORIGIN_Y, ORIGIN_Z, U_X, U_Y,
                                               U_Z, V_X, V_Y, V_Z);

  /**
   * Test that points at arbitrary points in the model map to correct values
   * from the underlying module.
   */
  public void testAxes () {
    for (int uStep = 0; uStep < STEPS; ++uStep)
      for (int vStep = 0; vStep < STEPS; ++vStep) {
        double uCoordinate = uStep * U;
        double vCoordinate = vStep * V;
        assertEquals (MODULE.getValue (ORIGIN_X + uCoordinate * U_X
            + vCoordinate * V_X, ORIGIN_Y + uCoordinate * U_Y + vCoordinate
            * V_Y, ORIGIN_Z + uCoordinate * U_Z + vCoordinate * V_Z), MODEL
            .getValue (uCoordinate, vCoordinate));
      }
  }

  /**
   * Test that points along the U axis of the model map to correct values from
   * the underlying module.
   */
  public void testAxisU () {
    for (int i = 0; i < STEPS; ++i)
      assertEquals (MODULE.getValue (ORIGIN_X + i * U * U_X, ORIGIN_Y + i * U
          * U_Y, ORIGIN_Z + i * U * U_Z), MODEL.getValue (i * U, 0));
  }

  /**
   * Test that points along the V axis of the model map to correct values from
   * the underlying module.
   */
  public void testAxisV () {
    for (int i = 0; i < STEPS; ++i)
      assertEquals (MODULE.getValue (ORIGIN_X + i * V * V_X, ORIGIN_Y + i * V
          * V_Y, ORIGIN_Z + i * V * V_Z), MODEL.getValue (0, i * V));
  }

  /**
   * Test that the point at the origin of the model maps to the basis point on
   * the underlying module.
   */
  public void testOrigin () {
    assertEquals (MODULE.getValue (ORIGIN_X, ORIGIN_Y, ORIGIN_Z), MODEL
        .getValue (0, 0));
  }
}
