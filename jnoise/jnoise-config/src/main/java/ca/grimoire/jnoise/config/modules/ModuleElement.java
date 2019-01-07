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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.Element;

/**
 * Convenience class for elements which accept an arbitrary number of source
 * modules as children.
 */
public class ModuleElement implements Element {

  /**
   * Returns the children of this element. The child elements will be returned
   * in the order they were added.
   * 
   * @return a list of ModuleBuilders registered with this element.
   */
  public final List<ModuleBuilder> getChildren () {
    return Collections.unmodifiableList (sources);
  }

  /**
   * Adds a child element to this element. The child must be a ModuleBuilder;
   * otherwise, this element will reject it.
   * 
   * @param child
   *          the child element to add.
   * @throws BuilderException
   *           if the passed <var>child</var> is not a module builder.
   */
  public final void addChild (Element child) throws BuilderException {
    assert (child != null);

    if (!(child instanceof ModuleBuilder))
      throw new BuilderException ("Element only accepts modules as children.");

    sources.add ((ModuleBuilder) child);
  }

  private List<ModuleBuilder> sources = new LinkedList<ModuleBuilder> ();
}
