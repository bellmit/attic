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
package ca.grimoire.jnoise.modules;

/**
 * Basic interface common to all noise modules. A noise module represents a
 * deterministic function in a 3-dimensional space returning a scalar value --
 * the "noise value" -- for each point within that space.
 * <p>
 * Noise modules may base the generation of noise values on anything at all,
 * including the output of other modules. However, the noise value of a module
 * at a given location <em>must</em> be deterministic.
 */
public interface Module {
  /**
   * Returns the noise value at a given point in the module. Noise values are
   * generally within the range <code>[-1.0, 1.0]</code> but this is not
   * required.
   * 
   * @param x
   *          the <var>x</var> coordinate of the point.
   * @param y
   *          the <var>y</var> coordinate of the point.
   * @param z
   *          the <var>z</var> coordinate of the point.
   * @return the noise value at
   *         <code>(<var>x</var>, <var>y</var>, <var>z</var>)</code> in
   *         the module.
   */
  double getValue (double x, double y, double z);
}
