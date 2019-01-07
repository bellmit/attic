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
 * A module that performs linear adjustments on a source module's output (by
 * multiplying it by a value and adding a translation offset). The generated
 * value <var>v</var> for a given source value <var>s</var> is
 * <code><var>v</var> = <var>s</var> * scale + translation</code>.
 * <p>
 * If you are using the included XML noise configuration system, Bias modules
 * can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;bias scale="<var>scale</var>" translation="<var>translation</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/bias&gt;</code> </blockquote>
 * <p>
 * Both scale and translation attributes are optional; both default to identity
 * transformations: scale of 1.0, translation by 0.0.
 */
public final class Bias extends SingleSourceModule {

  /**
   * Create a new bias module. The resulting module has zero translation bias.
   * 
   * @param source
   *          the module to bias.
   * @param scale
   *          the scale bias for the noise samples.
   */
  public Bias (Module source, double scale) {
    this (source, scale, 0.0);
  }

  /**
   * Create a new bias module.
   * 
   * @param source
   *          the module to bias.
   * @param scale
   *          the scale bias for noise samples.
   * @param translation
   *          the translation bias for noise samples.
   */
  public Bias (Module source, double scale, double translation) {
    super (source);

    this.scale = scale;
    this.translation = translation;
  }

  /**
   * Compare the module for equality with another object. The module is equal to
   * an object if and only if the object is also a Bias module with the same
   * bias fed by an equal source module.
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
    else if (!(object instanceof Bias))
      return false;

    Bias other = (Bias) object;
    return getSource ().equals (other.getSource ())
        && other.getScale () == scale && other.getTranslation () == translation;
  }

  /**
   * Get the scale applied to noise samples.
   * 
   * @return the scale bias.
   */
  public double getScale () {
    return scale;
  }

  /**
   * Get the translation applied to noise samples.
   * 
   * @return the translation bias.
   */
  public double getTranslation () {
    return translation;
  }

  /**
   * Generate a biased noise sample for a location.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the biased noise value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return getSource ().getValue (x, y, z) * scale + translation;
  }

  /**
   * Calcuate a hashcode for the object according to the general
   * hashcode/equality contract for Object. The hash code is computed from the
   * hashcode of the underlying module and the bounds of the module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (scale)
        ^ Hash.hashDouble (translation);
  }

  private final double scale;
  private final double translation;
}
