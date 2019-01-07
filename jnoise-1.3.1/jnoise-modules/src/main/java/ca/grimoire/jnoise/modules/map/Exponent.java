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
package ca.grimoire.jnoise.modules.map;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.SingleSourceModule;
import ca.grimoire.jnoise.util.Hash;

/**
 * Noise module raising the output of a source module by a fixed exponent.
 * <p>
 * If you are using the included XML noise configuration system, Exponent
 * modules can be declared as
 * <p>
 * <blockquote> <code>&lt;exponent power="<var>exponent</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/exponent&gt;</code> </blockquote>
 */
public final class Exponent extends SingleSourceModule {

  private final double exponent;

  /**
   * Create a new Exponent module.
   * 
   * @param source
   *          the module to raise.
   * @param exponent
   *          the exponent to raise the module to.
   */
  public Exponent (Module source, double exponent) {
    super (source);

    this.exponent = exponent;
  }

  /**
   * Get the noise value at a location.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the noise value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return Math.pow (getSource ().getValue (x, y, z), exponent);
  }

  /**
   * Get the exponent to which noise values are raised.
   * 
   * @return the module's exponent.
   */
  public double getExponent () {
    return exponent;
  }

  /**
   * Compare the module for equality with another object. The module is equal to
   * an object if and only if the object is also an Exponent module with the
   * same exponent fed by an equal source module.
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
    else if (!(object instanceof Exponent))
      return false;

    Exponent other = (Exponent) object;
    return getSource ().equals (other.getSource ())
        && other.getExponent () == exponent;
  }

  /**
   * Calcuate a hashcode for the object according to the general
   * hashcode/equality contract for Object. The hash code is computed from the
   * hashcode of the underlying module and the exponent of the module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (exponent);
  }

}
