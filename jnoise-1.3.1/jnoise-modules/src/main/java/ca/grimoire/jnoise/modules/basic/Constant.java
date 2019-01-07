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
package ca.grimoire.jnoise.modules.basic;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.util.Hash;

/**
 * "Noise" module that generates a constant value.
 * <p>
 * If you are using the included XML noise configuration system, Constant modules
 * can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;constant value="<var>value</var>" /&gt;</code>
 * </blockquote>
 */
public final class Constant implements Module {

  /**
   * Creates a new contant noise module. All calls to getValue will return the
   * constructed value.
   * 
   * @param value
   *          the constant noise value.
   */
  public Constant (double value) {
    this.value = value;
  }

  /**
   * Compares this module for equality with another object. The module is equal
   * to an object if and only if the object is also a Constant module returning
   * the same value.
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
    else if (!(object instanceof Constant))
      return false;

    Constant other = (Constant) object;
    return other.getValue () == value;
  }

  /**
   * Returns the constant noise value for this module.
   * 
   * @return the module's set noise value.
   */
  public double getValue () {
    return value;
  }

  /**
   * Generates a sample from the generator. The sample will always be the
   * initialised value.
   * 
   * @param x
   *          the x coordinate of the sample.
   * @param y
   *          the y coordinate of the sample.
   * @param z
   *          the z coordinate of the sample.
   * @return the constant noise value for this module.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return getValue ();
  }

  /**
   * Generates a hashcode for this constant module. The hash code is computed
   * from the constant value the module supplies.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return Hash.hashDouble (value);
  }

  private final double value;
}
