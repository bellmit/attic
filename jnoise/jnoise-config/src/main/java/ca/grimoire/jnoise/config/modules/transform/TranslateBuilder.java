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
import ca.grimoire.jnoise.modules.transform.Translate;

/**
 * Builder element for the Translate transformation.
 * 
 * @see Translate
 */
public final class TranslateBuilder extends TransformDescription implements
    ModuleBuilder {

  /**
   * Creates a Translate builder with all translations set to 0.
   */
  public TranslateBuilder () {
    super ();
  }

  /**
   * Creates a Translate builder with the angles configured.
   * 
   * @param x
   *          the X rotation in radians.
   * @param y
   *          the Y rotation in radians.
   * @param z
   *          the Z rotation in radians.
   */
  public TranslateBuilder (double x, double y, double z) {
    super (x, y, z);
  }

  /**
   * Creates a Translate module using the child builder as a source and the X,
   * Y, and Z transformation parameters as translations.
   * 
   * @return a Translate module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Translate createModule () throws BuilderException {
    return new Translate (getSource ().createModule (), getX (), getY (),
        getZ ());
  }

}
