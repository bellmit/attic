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
import ca.grimoire.jnoise.modules.map.Absolute;

/**
 * Element handler for Absolute noise modules.
 * 
 * @see Absolute
 */
public final class AbsoluteBuilder extends SingleModuleElement implements
    ModuleBuilder {

  /**
   * Creates an Absolute noise module using the child element as a source.
   * 
   * @return the created Absolute module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Absolute createModule () throws BuilderException {
    return new Absolute (getSource ().createModule ());
  }

}
