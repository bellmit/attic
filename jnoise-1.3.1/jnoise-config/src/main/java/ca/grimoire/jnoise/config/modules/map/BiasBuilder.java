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
import ca.grimoire.jnoise.modules.map.Bias;

/**
 * Element handler for Bias noise modules.
 * 
 * @see Bias
 */
public final class BiasBuilder extends SingleModuleElement implements
    ModuleBuilder {

  /**
   * Creates a Bias noise module using the child element as a source.
   * 
   * @return the created Bias module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Bias createModule () throws BuilderException {
    return new Bias (getSource ().createModule (), scale, translation);
  }

  /**
   * Changes the scale applied to the resulting Bias module.
   * 
   * @param scale
   *          the bias scale coefficient.
   */
  public final void setScale (double scale) {
    this.scale = scale;
  }

  /**
   * Changes the value offset applied to the resulting Bias module.
   * 
   * @param translation
   *          the bias translation value.
   */
  public final void setTranslation (double translation) {
    this.translation = translation;
  }

  private double scale = 1.0, translation = 0.0;
}
