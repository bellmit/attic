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
package ca.grimoire.jnoise.config.modules.composition;

import java.util.List;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.config.modules.ModuleElement;
import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.composition.Add;

/**
 * A builder capable of creating Add noise modules. Add noise modules operate on
 * a collection of sources, whose builders should be passed to
 * <code>addChild</code>.
 * 
 * @see jnoise.modules.composition.Add
 */
public final class AddBuilder extends ModuleElement implements ModuleBuilder {

  /**
   * Generates an Add noise module using the child elements' modules as sources.
   * 
   * @return an Add noise module.
   * @throws BuilderException
   *           if any of the source modules throws an exception.
   * @see ModuleBuilder#createModule()
   */
  public Add createModule () throws BuilderException {
    return new Add (createSources ());
  }

  private Module[] createSources () throws BuilderException {
    List<ModuleBuilder> sources = getChildren ();
    Module[] modules = new Module[sources.size ()];

    int module = 0;
    for (ModuleBuilder builder : sources)
      modules[module++] = builder.createModule ();

    return modules;
  }
}
