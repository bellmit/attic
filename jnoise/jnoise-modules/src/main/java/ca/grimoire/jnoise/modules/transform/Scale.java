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
 * Noise transformation module that scales a source module's coordinate space.
 * The resulting noise is "denser" (eg., lower-frequency noise) for scales
 * greater than 1 and "sparser" (higher-frequency noise) for scales less than 1.
 * <p>
 * If you are using the included XML noise configuration system, Scale modules
 * can be declared as
 * <p>
 * <blockquote><code>&lt;scale x="<var>scale</var>" y="<var>scale</var>" z="<var>scale</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/scale&gt;</code></blockquote>
 * <p>
 * Omitting the x, y, or z attribute will leave that axis un-scaled.
 */
public final class Scale extends SingleSourceModule {

  /**
   * Creates a new scaler module around a given source module. The passed scale
   * is applied to all three axes.
   * 
   * @param source
   *          the module to scale.
   * @param scale
   *          the scale of the module.
   */
  public Scale (Module source, double scale) {
    this (source, scale, scale, scale);
  }

  /**
   * Creates a new scaler module around a given source module.
   * 
   * @param source
   *          the module to scale.
   * @param xScale
   *          the scale along the X axis of the module.
   * @param yScale
   *          the scale along the Y axis of the module.
   * @param zScale
   *          the scale along the Z axis of the module.
   */
  public Scale (Module source, double xScale, double yScale, double zScale) {
    super (source);

    this.xScale = xScale;
    this.yScale = yScale;
    this.zScale = zScale;
  }

  /**
   * Compares this module for equality with another object. Scale modules are
   * equal to other scale modules with equal sources and scale coefficients.
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
    else if (!(object instanceof Scale))
      return false;

    Scale other = (Scale) object;
    return getSource ().equals (other.getSource ())
        && other.getXScale () == xScale && other.getYScale () == yScale
        && other.getZScale () == zScale;
  }

  /**
   * Returns the noise value from the underlying module after scaling the
   * coordinates.
   * 
   * @param x
   *          the X coordinate to scale.
   * @param y
   *          the Y coordinate to scale.
   * @param z
   *          the Z coordinate to scale.
   * @return the noise value at the scaled coordinates.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return getSource ().getValue (x * xScale, y * yScale, z * zScale);
  }

  /**
   * Returns the scale along the X axis of the module.
   * 
   * @return the module's X scale.
   */
  public double getXScale () {
    return xScale;
  }

  /**
   * Returns the scale along the Y axis of the module.
   * 
   * @return the module's Y scale.
   */
  public double getYScale () {
    return yScale;
  }

  /**
   * Returns the scale along the Z axis of the module.
   * 
   * @return the module's Z scale.
   */
  public double getZScale () {
    return zScale;
  }

  /**
   * Calcuates a hashcode for this scale module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (xScale)
        ^ Hash.hashDouble (yScale) ^ Hash.hashDouble (zScale);
  }

  private final double xScale;
  private final double yScale;
  private final double zScale;
}
