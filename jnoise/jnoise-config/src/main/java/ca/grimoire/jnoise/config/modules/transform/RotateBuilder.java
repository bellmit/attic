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
import ca.grimoire.jnoise.modules.transform.Rotate;

/**
 * Builder element for the Rotate transformation.
 * 
 * @see Rotate
 */
public final class RotateBuilder extends TransformDescription implements
    ModuleBuilder {

  /**
   * Creates a rotate builder with no rotation.
   */
  public RotateBuilder () {
    super ();
  }

  /**
   * Creates a rotate builder with the angles configured.
   * 
   * @param x
   *          the X rotation in radians.
   * @param y
   *          the Y rotation in radians.
   * @param z
   *          the Z rotation in radians.
   */
  public RotateBuilder (double x, double y, double z) {
    super (x, y, z);
  }

  /**
   * Creates a Rotate module using the child builder as a source and the X, Y,
   * and Z transformation parameters as rotation angles in radians.
   * 
   * @return a Rotate module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Rotate createModule () throws BuilderException {
    return new Rotate (getSource ().createModule (), getX (), getY (), getZ ());
  }

}
