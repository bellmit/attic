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
 * Module that translates coordinates before passing them to an underlying
 * source module.
 * <p>
 * If you are using the included XML noise configuration system, Translate
 * modules can be declared as
 * <p>
 * <blockquote><code>&lt;translate x="<var>offset</var>" y="<var>offset</var>" z="<var>offset</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/translate&gt;</code></blockquote>
 * <p>
 * Omitting the x, y, or z attribute will leave that axis un-translated.
 */
public final class Translate extends SingleSourceModule {

  /**
   * Creates a new translate module with a given source and translation.
   * 
   * @param source
   *          the source module.
   * @param x
   *          the X component of the translation to apply.
   * @param y
   *          the Y component of the translation to apply.
   * @param z
   *          the Z component of the translation to apply.
   */
  public Translate (Module source, double x, double y, double z) {
    super (source);

    this.xTranslation = x;
    this.yTranslation = y;
    this.zTranslation = z;
  }

  /**
   * Compares this module for equality with another object. Translate modules
   * are equal to other translate modules with the same translations to equal
   * sources.
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
    else if (!(object instanceof Translate))
      return false;

    Translate other = (Translate) object;
    return getSource ().equals (other.getSource ())
        && other.getXTranslation () == xTranslation
        && other.getYTranslation () == yTranslation
        && other.getZTranslation () == zTranslation;
  }

  /**
   * Returns a noise value from the module. The passed coordinates are
   * translated, then passed to the source module.
   * 
   * @param x
   *          the X coordinate on the module to generate.
   * @param y
   *          the X coordinate on the module to generate.
   * @param z
   *          the X coordinate on the module to generate.
   * @return the noise value at the translated location on the source module.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return getSource ().getValue (x + xTranslation, y + yTranslation,
        z + zTranslation);
  }

  /**
   * Returns the X component of the translation applied to positions.
   * 
   * @return the module's X translation.
   */
  public double getXTranslation () {
    return xTranslation;
  }

  /**
   * Returns the Y component of the translation applied to positions.
   * 
   * @return the module's Y translation.
   */
  public double getYTranslation () {
    return yTranslation;
  }

  /**
   * Returns the Z component of the translation applied to positions.
   * 
   * @return the module's Z translation.
   */
  public double getZTranslation () {
    return zTranslation;
  }

  /**
   * Generates a hashcode for this translation module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (xTranslation)
        ^ Hash.hashDouble (yTranslation) ^ Hash.hashDouble (zTranslation);
  }

  private final double xTranslation;
  private final double yTranslation;
  private final double zTranslation;
}
