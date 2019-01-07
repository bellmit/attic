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

/**
 * A coordinate mapping tool for mapping noise modules, which use a cartesian
 * coordinate system, to spherical
 * <code>(<var>rho</var>, <var>theta</var>)</code> coordinates along a unit
 * sphere. The angle <var>rho</var> is measured in radians from the Y axis, and
 * the angle <var>theta</var> is measured in radians from the X axis (towards
 * the Z axis).
 */
public final class SphereModel {
  /**
   * Creates a new sphere mapping for a given noise module.
   *
   * @param source
   *          the module to map.
   */
  public SphereModel (Module source) {
    assert (source != null);

    this.source = source;
  }

  private final Module source;

  /**
   * Maps spherical coordinates to the underlying noise module and returns the
   * corresponding noise value.
   * 
   * @param rho
   *          the angle from the Y axis, in radians.
   * @param theta
   *          the angle from the X axis, in radians.
   * @return the noise value at the position.
   */
  public double getValue (double rho, double theta) {
    double sinRho = Math.sin(rho);
    double x = Math.cos(theta) * sinRho;
    double y = Math.cos(rho);
    double z = Math.sin(theta) * sinRho;
    
    return source.getValue(x, y, z);
  }
}
