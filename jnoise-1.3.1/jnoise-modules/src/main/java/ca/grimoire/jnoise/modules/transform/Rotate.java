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
package ca.grimoire.jnoise.modules.transform;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.SingleSourceModule;
import ca.grimoire.jnoise.util.Hash;

/**
 * Noise transformation module rotating the coordinate system of another module.
 * <p>
 * If you are using the included XML noise configuration system, Rotate modules
 * can be declared as
 * <p>
 * <blockquote><code>&lt;rotate x="<var>angle</var>" y="<var>angle</var>" z="<var>angle</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/rotate&gt;</code></blockquote>
 * <p>
 * Omitting the x, y, or z attribute will leave that axis un-rotated.
 */
public final class Rotate extends SingleSourceModule {
  /**
   * Creates a new rotation module.
   * 
   * @param source
   *          the source module.
   * @param xAngle
   *          the angle (in radians) to rotate around the X axis.
   * @param yAngle
   *          the angle (in radians) to rotate around the Y axis.
   * @param zAngle
   *          the angle (in radians) to rotate around the Z axis.
   */
  public Rotate (Module source, double xAngle, double yAngle, double zAngle) {
    super (source);

    // Stash the angles
    this.xAngle = xAngle;
    this.yAngle = yAngle;
    this.zAngle = zAngle;

    // compute transform matrix
    double sinX = Math.sin (xAngle);
    double cosX = Math.cos (xAngle);
    double sinY = Math.sin (yAngle);
    double cosY = Math.cos (yAngle);
    double sinZ = Math.sin (zAngle);
    double cosZ = Math.cos (zAngle);

    xToX = sinX * sinY * sinZ + cosY * cosZ;
    yToX = cosX * sinZ;
    zToX = sinY * cosZ - cosY * sinX * sinZ;

    xToY = sinY * sinX * cosZ - cosY * sinZ;
    yToY = cosX * cosZ;
    zToY = -cosY * sinX * cosZ - sinY * sinZ;

    xToZ = -sinY * cosX;
    yToZ = sinX;
    zToZ = cosY * cosX;
  }

  /**
   * Compares this module for equality with another object. This module is equal
   * to other Rotate modules with the same orientation and an equal source
   * module.
   * 
   * @param object
   *          the object to compare.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof Rotate))
      return false;

    Rotate other = (Rotate) object;
    return getSource ().equals (other.getSource ())
        && other.getXAngle () == xAngle && other.getYAngle () == yAngle
        && other.getZAngle () == zAngle;
  }

  /**
   * Gets a value from the module. The point will be rotated before being passed
   * to the source module.
   * 
   * @param x
   *          the X coordinate of the noise value.
   * @param y
   *          the Y coordinate of the noise value.
   * @param z
   *          the Z coordinate of the noise value.
   * @return the noise value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    double projectedX = x * xToX + y * yToX + z * zToX;
    double projectedY = x * xToY + y * yToY + z * zToY;
    double projectedZ = x * xToZ + y * yToZ + z * zToZ;
    return getSource ().getValue (projectedX, projectedY, projectedZ);
  }

  /**
   * Returns the angle the X axis of the module is being rotated. The returned
   * angle is guaranteed to be congruent to the constructed angle but not
   * necessarily identical to it.
   * 
   * @return the module's rotation around the X axis.
   */
  public double getXAngle () {
    return xAngle;
  }

  /**
   * Returns the angle the Y axis of the module is being rotated. The returned
   * angle is guaranteed to be congruent to the constructed angle but not
   * necessarily identical to it.
   * 
   * @return the module's rotation around the Y axis.
   */
  public double getYAngle () {
    return yAngle;
  }

  /**
   * Returns the angle the Z axis of the module is being rotated. The returned
   * angle is guaranteed to be congruent to the constructed angle but not
   * necessarily identical to it.
   * 
   * @return the module's rotation around the Z axis.
   */
  public double getZAngle () {
    return zAngle;
  }

  /**
   * Generates a hashcode for this rotate module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (xAngle)
        ^ Hash.hashDouble (yAngle) ^ Hash.hashDouble (zAngle);
  }

  private final double xAngle, yAngle, zAngle;
  private final double xToX, xToY, xToZ;
  private final double yToX, yToY, yToZ;
  private final double zToX, zToY, zToZ;
}
