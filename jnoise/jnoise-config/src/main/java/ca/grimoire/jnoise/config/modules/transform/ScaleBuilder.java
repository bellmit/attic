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
package ca.grimoire.jnoise.config.modules.transform;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.transform.Scale;

/**
 * Builder element for the Scale transformation.
 * 
 * @see Scale
 */
public final class ScaleBuilder extends TransformDescription implements
    ModuleBuilder {

  private static final double IDENTITY = 1.0;

  /**
   * Creates a Scale builder which initially makes no change.
   */
  public ScaleBuilder () {
    super (IDENTITY, IDENTITY, IDENTITY);
  }

  /**
   * Creates a Scale builder with coefficients.
   * 
   * @param x
   *          the X coefficient.
   * @param y
   *          the Y coefficient.
   * @param z
   *          the Z coefficient.
   */
  public ScaleBuilder (double x, double y, double z) {
    super (x, y, z);
  }

  /**
   * Creates a Scale module using the child builder as a source and the X, Y,
   * and Z transformation parameters as scale coefficients.
   * 
   * @return a Scale module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Scale createModule () throws BuilderException {
    return new Scale (getSource ().createModule (), getX (), getY (), getZ ());
  }

}
