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
import ca.grimoire.jnoise.modules.map.Exponent;

/**
 * Element handler for Exponent noise modules.
 * 
 * @see Exponent
 */
public final class ExponentBuilder extends SingleModuleElement implements
    ModuleBuilder {

  /**
   * Creates a Exponent noise module using the child element as a source.
   * 
   * @return the created Exponent module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Exponent createModule () throws BuilderException {
    if (!hasPower)
      throw new BuilderException ("Exponent requires power.");

    return new Exponent (getSource ().createModule (), power);
  }

  /**
   * Sets the power to raise the source module to.
   * 
   * @param power
   *          the exponent to raise the source value to.
   */
  public final void setPower (double power) {
    this.power = power;
    this.hasPower = true;
  }

  private boolean hasPower = false;
  private double  power;
}
