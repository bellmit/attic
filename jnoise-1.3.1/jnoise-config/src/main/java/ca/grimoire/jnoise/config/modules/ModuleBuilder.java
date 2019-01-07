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
package ca.grimoire.jnoise.config.modules;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.Element;
import ca.grimoire.jnoise.modules.Module;

/**
 * Common interface for builders and factories which create noise Module
 * implementations.
 * 
 * @see Module
 */
public interface ModuleBuilder extends Element {

  /**
   * Generates or creates a noise module.
   * 
   * @return a noise module.
   * @throws BuilderException
   *           if the builder is unable to create a module.
   */
  public Module createModule () throws BuilderException;

}
