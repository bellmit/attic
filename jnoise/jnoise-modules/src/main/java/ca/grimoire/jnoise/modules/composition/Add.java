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
package ca.grimoire.jnoise.modules.composition;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.MultiSourceModule;

/**
 * Noise module that generates the sum of noise from two or more source modules.
 * <p>
 * Add modules with one source (or even zero sources) work, but aren't
 * particularly useful.
 * <p>
 * If you are using the included XML noise configuration system, Add modules can
 * be declared as
 * <p>
 * <blockquote> <code>&lt;add&gt;<br>
 * &nbsp;<var>...source modules...</var><br>
 * &lt;/add&gt;</code> </blockquote>
 */
public final class Add extends MultiSourceModule {

  /**
   * Creates a new Add module, using the passed modules as sources.
   * 
   * @param sources
   *          the sources to add together.
   */
  public Add (Module... sources) {
    super (sources);
  }

  /**
   * Compares this module to another object. Add modules are equal to other add
   * modules with equal sources.
   * 
   * @param object
   *          the object to compare to.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (!super.equals (object))
      return false;

    return object instanceof Add;
  }

  /**
   * Generates the sum of the source noise modules at a location.
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
    double value = 0.0;

    for (Module source : getSources ())
      value += source.getValue (x, y, z);

    return value;
  }
}
