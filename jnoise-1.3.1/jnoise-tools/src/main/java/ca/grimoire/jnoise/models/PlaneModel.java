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
package ca.grimoire.jnoise.models;

import ca.grimoire.jnoise.modules.Module;

/**
 * A coordinate model mapping from planar coordinates
 * <code>(<var>u</var>, <var>v</var>)</code> to module coordinates
 * <code>(<var>x</var>, <var>y</var>, <var>z</var>)</code>. The input
 * coordinates are mapped to module coordinates as
 * <code><var>position</var> = <var>basis</var> + <var>u</var> * <var>uvect</var> + <var>v</var> * <var>vvect</var></code>.
 */
public final class PlaneModel {

  /**
   * Creates a new plane model.
   * 
   * @param source
   *          the source module to extract values from.
   * @param basisX
   *          the X component of the basis vector for the model.
   * @param basisY
   *          the Y component of the basis vector for the model.
   * @param basisZ
   *          the Z component of the basis vector for the model.
   * @param ux
   *          the X component of the "X axis" vector for the model.
   * @param uy
   *          the Y component of the "X axis" vector for the model.
   * @param uz
   *          the Z component of the "X axis" vector for the model.
   * @param vx
   *          the X component of the "Y axis" vector for the model.
   * @param vy
   *          the Y component of the "Y axis" vector for the model.
   * @param vz
   *          the Z component of the "Y axis" vector for the model.
   */
  public PlaneModel (Module source, double basisX, double basisY,
      double basisZ, double ux, double uy, double uz, double vx, double vy,
      double vz) {
    assert (source != null);

    this.source = source;
    this.basisX = basisX;
    this.basisY = basisY;
    this.basisZ = basisZ;
    uX = ux;
    uY = uy;
    uZ = uz;
    vX = vx;
    vY = vy;
    vZ = vz;
  }

  /**
   * Gets a noise value from the model. The passed location is mapped to the
   * underlying noise module, and the noise value at that location returned.
   * 
   * @param u
   *          the "X-axis" component of the model location.
   * @param v
   *          the "Y-axis" component of the model location.
   * @return the noise value at the corresponding location on the underlying
   *         module.
   */
  public double getValue (double u, double v) {
    double x = basisX + u * uX + v * vX;
    double y = basisY + u * uY + v * vY;
    double z = basisZ + u * uZ + v * vZ;

    return source.getValue (x, y, z);
  }

  private final Module source;
  private final double basisX, basisY, basisZ;
  private final double uX, uY, uZ;
  private final double vX, vY, vZ;
}
