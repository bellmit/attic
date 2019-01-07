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

/**
 * Noise module that takes a source module and outputs the absolute value of its
 * output.
 * <p>
 * If you are using the included XML noise configuration system, Absolute
 * modules can be declared as
 * <p>
 * <blockquote><code>&lt;absolute&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/absolute&gt;</code></blockquote>
 */
public final class Absolute extends SingleSourceModule {

  /**
   * Creates a new Absolute module fed from a given source.
   * 
   * @param source
   *          the module to take the absolute value of.
   */
  public Absolute (Module source) {
    super (source);
  }

  /**
   * Gets the absolute value of the source module at a given location.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the absolute value of the noise at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return Math.abs (getSource ().getValue (x, y, z));
  }

  /**
   * Compares this module for equality with another object. This module is equal
   * to an object if and only if the object is also an Absolute module fed by an
   * equal source module.
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
    else if (!(object instanceof Absolute))
      return false;

    Absolute other = (Absolute) object;
    return getSource ().equals (other.getSource ());
  }

  /**
   * Generates a hashcode for this absolute module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    // Distinct from the source's hashCode but predictable.
    return getSource ().hashCode () + 1;
  }

}
