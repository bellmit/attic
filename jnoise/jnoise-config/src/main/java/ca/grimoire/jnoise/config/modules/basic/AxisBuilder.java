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

import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Axis;

/**
 * A builder capable of creating Axis noise modules. Axis modules are
 * non-configurable.
 * 
 * @see Axis
 */
public final class AxisBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a new AxisBuilder.
   */
  public AxisBuilder () {

  }

  /**
   * Generates an Axis module.
   * 
   * @return an Axis noise module.
   * @see ModuleBuilder#createModule()
   */
  public Axis createModule () {
    return Axis.MODULE;
  }
}
