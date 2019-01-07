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
package ca.grimoire.jnoise.config.modules.basic;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Checker;

/**
 * A builder capable of creating Checker noise modules. Constant noise modules
 * require a pair of values (the "black" and "white" values), which can be
 * configured through setBlack and setWhite. Both must be provided before a
 * module can be produced.
 * 
 * @see Checker
 */
public final class CheckerBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a new CheckerBuilder with no values set. The black and white value
   * for the created modules must be passed to {@link #setBlack(double)} and
   * {@link #setWhite(double)} before calling {@link #createModule()}.
   */
  public CheckerBuilder () {

  }

  /**
   * Creates a new CheckerBuilder with black and white values set.
   * 
   * @param black
   *          the initial black value for the module.
   * @param white
   *          the initial white value for the module.
   */
  public CheckerBuilder (double black, double white) {
    setBlack (black);
    setWhite (white);
  }

  /**
   * Generates a Checker noise module using the configured black and white
   * values. If either value has not been configured yet this throws a
   * BuilderException instead.
   * 
   * @return a Checker noise module.
   * @throws BuilderException
   *           if either the black or white value has not been configured.
   * @see #setBlack(double)
   * @see #setWhite(double)
   * @see ModuleBuilder#createModule()
   */
  public Checker createModule () throws BuilderException {
    if (!blackSet)
      throw new BuilderException ("No black value for checker module.");
    if (!whiteSet)
      throw new BuilderException ("No white value for checker module.");

    return new Checker (black, white);
  }

  /**
   * Changes the "black" cube noise value used to configure subsequently-created
   * modules.
   * 
   * @param blackValue
   *          the black value to use in subsequent checkerboard modules.
   */
  public void setBlack (double blackValue) {
    this.black = blackValue;
    this.blackSet = true;
  }

  /**
   * Changes the "white" cube noise value used to configure subsequently-created
   * modules.
   * 
   * @param whiteValue
   *          the white value to use in subsequent checkerboard modules.
   */
  public void setWhite (double whiteValue) {
    this.white = whiteValue;
    this.whiteSet = true;
  }

  private double black, white;
  private boolean blackSet = false, whiteSet = false;
}
