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
package ca.grimoire.jnoise.config.modules.transform;

import ca.grimoire.jnoise.config.modules.SingleModuleElement;

/**
 * Base element used for transformations. Provides child element handling and
 * common attributes.
 */
public class TransformDescription extends SingleModuleElement {

  /**
   * Creates a new transformation with components initially all zero.
   */
  public TransformDescription () {
    // Do nothing, but allow no-args c'tor.
  }

  /**
   * Creates a new transformation with components.
   * 
   * @param x
   *          the X component of the transformation.
   * @param y
   *          the Y component of the transformation.
   * @param z
   *          the Z component of the transformation.
   */
  public TransformDescription (double x, double y, double z) {
    setX (x);
    setY (y);
    setZ (z);
  }

  /**
   * Returns the X component of the transformation.
   * 
   * @return the transform's X component.
   */
  public final double getX () {
    return x;
  }

  /**
   * Returns the Y component of the transformation.
   * 
   * @return the transform's Y component.
   */
  public final double getY () {
    return y;
  }

  /**
   * Returns the Z component of the transformation.
   * 
   * @return the transform's Z component.
   */
  public final double getZ () {
    return z;
  }

  /**
   * Changes the X component of the transformation.
   * 
   * @param x
   *          the transform's X component.
   */
  public final void setX (double x) {
    this.x = x;
  }

  /**
   * Changes the Y component of the transformation.
   * 
   * @param y
   *          the transform's Y component.
   */
  public final void setY (double y) {
    this.y = y;
  }

  /**
   * Changes the Z component of the transformation.
   * 
   * @param z
   *          the transform's Z component.
   */
  public final void setZ (double z) {
    this.z = z;
  }

  private double        x, y, z;
}
