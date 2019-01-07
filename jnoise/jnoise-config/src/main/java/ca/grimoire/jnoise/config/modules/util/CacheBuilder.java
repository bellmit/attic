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
package ca.grimoire.jnoise.config.modules.util;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.Element;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.util.Cache;

/**
 * A builder capable of creating Cache noise modules. Add noise modules operate
 * on a single source, whose builder should be passed to
 * {@link #addChild(Element)}.
 * 
 * @see Cache
 */
public final class CacheBuilder implements ModuleBuilder {

  /**
   * Generates a Cache noise module using the child element's module as a
   * source.
   * 
   * @return a Cache noise module.
   * @throws BuilderException
   *           if the source module builder throws an exception.
   * @see ModuleBuilder#createModule()
   */
  public Cache createModule () throws BuilderException {
    return new Cache (createSource ());
  }

  private Module createSource () throws BuilderException {
    if (source == null)
      throw new BuilderException ("No source module for cache.");
    return source.createModule ();
  }

  /**
   * Adds an element as a child of this element. Only one child element is
   * allowed, which must be a ModuleBuilder.
   * 
   * @param child
   *          the element to add.
   * @throws BuilderException
   *           if the element is not a ModuleBuilder.
   */
  public void addChild (Element child) throws BuilderException {
    assert (child != null);
    if (!(child instanceof ModuleBuilder))
      throw new BuilderException ("Source element must be a module.");
    if (source != null)
      throw new BuilderException ("Cache element can only have one source.");

    source = (ModuleBuilder) child;
  }

  private ModuleBuilder source = null;
}
