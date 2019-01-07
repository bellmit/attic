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
 * Noise module that generates a checkerboard pattern of alternating unit cubes.
 * Unit cubes (cells) are numbered, starting with the cell between
 * <code>(0, 0, 0)</code> inclusive and <code>(1, 1, 1)</code> exclusive as
 * cell 0; even cells are filled with the generator's black value and odd cells
 * with the white value.
 * <p>
 * If you are using the included XML noise configuration system, Checker modules
 * can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;checker black="<var>blackvalue</var>" white="<var>whitevalue</var>" /&gt;</code>
 * </blockquote>
 */
public final class Checker implements Module {

  /**
   * Creates a new Checker module. The values 0 and 1 will be used for black and
   * white cells, respectively.
   */
  public Checker () {
    this (0, 1);
  }

  /**
   * Create a new Checker generator.
   * 
   * @param black
   *          the value of black cells.
   * @param white
   *          the value of white cells.
   */
  public Checker (double black, double white) {
    this.black = black;
    this.white = white;
  }

  /**
   * Compares this module for equality with another object. This is equal to an
   * object if and only if the object is also a Checker module with the same
   * black and white values.
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
    else if (!(object instanceof Checker))
      return false;

    Checker other = (Checker) object;
    return other.getBlackValue () == black && other.getWhiteValue () == white;
  }

  /**
   * Returns the value filling black (even-numbered) cells of the checkerboard.
   * 
   * @return the checkerboard's white value.
   */
  public double getBlackValue () {
    return black;
  }

  /**
   * Calculates which unit cube the position is in and returns either this
   * module's white value (for even cubes) or black value (for odd cubes).
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the cell value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    double cube = Math.floor (x) + Math.floor (y) + Math.floor (z);
    // Cubes are black on an even number, white on an odd number.
    cube /= 2.0;
    if (cube == Math.floor (cube))
      return black;

    return white;
  }

  /**
   * Gets the value filling white (odd-numbered) cells of the checkerboard.
   * 
   * @return the checkerboard's white value.
   */
  public double getWhiteValue () {
    return white;
  }

  /**
   * Calcuate a hashcode for this module. The hash code is computed from the
   * black and white values of the module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return Hash.hashDouble (black) ^ Hash.hashDouble (white);
  }

  private final double black, white;
}
