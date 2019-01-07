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
package ca.grimoire.jnoise.config;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ca.grimoire.jnoise.modules.Module;

/**
 * A collection of noise modules from a configuration.
 */
public final class NoiseConfiguration {
  private final List<Module> modules;

  /**
   * Creates a new, empty noise configuration.
   */
  NoiseConfiguration () {
    this.modules = new LinkedList<Module> ();
  }

  /**
   * Creates a new noise configuration with a collection of initial modules. The
   * modules will be supplied by the configuration in iteration order.
   * 
   * @param modules
   *          the modules to add.
   */
  NoiseConfiguration (Iterable<? extends Module> modules) {
    this ();

    assert (modules != null);

    for (Module module : modules)
      addModule (module);
  }

  /**
   * Adds a configured module to the end of the configuration.
   * 
   * @param module
   *          the module to add.
   */
  void addModule (Module module) {
    assert (module != null);

    modules.add (module);
  }

  /**
   * Returns the noise modules available in this configuration.
   * 
   * @return the modules in this configuration.
   */
  public List<Module> getNoiseModules () {
    return Collections.unmodifiableList (modules);
  }

  // TODO bolt in support for getting a module by ID and enumerating IDs.
}
