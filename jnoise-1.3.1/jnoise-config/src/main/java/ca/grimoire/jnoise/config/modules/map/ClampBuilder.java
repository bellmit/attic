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
package ca.grimoire.jnoise.config.modules.map;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.config.modules.SingleModuleElement;
import ca.grimoire.jnoise.modules.map.Clamp;

/**
 * Element handler for Clamp noise modules.
 * 
 * @see Clamp
 */
public final class ClampBuilder extends SingleModuleElement implements
    ModuleBuilder {

  /**
   * Creates a Clamp noise module using the child element as a source.
   * 
   * @return the created Clamp module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Clamp createModule () throws BuilderException {
    if (!hasLower)
      throw new BuilderException ("Clamp requires lower bound.");
    if (!hasUpper)
      throw new BuilderException ("Clamp requires upper bound.");
    if (lower > upper)
      throw new BuilderException (
          "Clamp requires lower bound less than or equal to upper bound.");

    return new Clamp (getSource ().createModule (), lower, upper);
  }

  /**
   * Sets the lower bound of the clamp range.
   * 
   * @param lower
   *          the lower clamp bound.
   */
  public final void setLower (double lower) {
    this.lower = lower;
    this.hasLower = true;
  }
  /**
   * Sets the upper bound of the clamp range.
   * 
   * @param upper
   *          the upper clamp bound.
   */
  public final void setUpper (double upper) {
    this.upper = upper;
    this.hasUpper = true;
  }
  private boolean hasLower = false;

  private boolean hasUpper = false;

  private double  lower, upper;

}
