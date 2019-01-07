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

import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.config.modules.ModuleElement;

/**
 * A configuration factory which converts elements into noise configurations.
 * Can operate either on standalone ModuleBuilder elements or on NoiseElement
 * instances.
 */
final class NoiseConfigurationFactory {
  private static NoiseConfiguration createFromModuleBuilder (
      ModuleBuilder builder) throws BuilderException {
    assert (builder != null);

    NoiseConfiguration config = new NoiseConfiguration ();
    config.addModule (builder.createModule ());
    return config;
  }

  private static NoiseConfiguration createFromModuleElement (
      ModuleElement element) throws BuilderException {
    assert (element != null);

    NoiseConfiguration configuration = new NoiseConfiguration ();

    for (ModuleBuilder builder : element.getChildren ())
      configuration.addModule (builder.createModule ());

    return configuration;
  }

  /**
   * Creates a noise configuration corresponding to an element.
   * 
   * @param element
   *          the element to convert.
   * @return the corresponding noise configuration.
   * @throws BuilderException
   *           if the element can't be converted to a noise configuration.
   */
  public NoiseConfiguration createConfiguration (Element element)
      throws BuilderException {
    assert (element != null);

    // FIXME something less stupid. Visitor pattern?
    if (element instanceof ModuleBuilder)
      return createFromModuleBuilder ((ModuleBuilder) element);

    if (element instanceof ModuleElement)
      return createFromModuleElement ((ModuleElement) element);

    throw new BuilderException (
        "Can't convert element to noise configuration: " + element);
  }
}
