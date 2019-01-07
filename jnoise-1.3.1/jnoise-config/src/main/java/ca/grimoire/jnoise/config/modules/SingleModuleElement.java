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

/**
 * Convenience class for elements which accept a single source module as a
 * child.
 */
public class SingleModuleElement implements Element {

  /**
   * Adds an XML element as a child of this element if it is a ModuleBuilder and
   * no child has been set.
   * 
   * @param child
   *          the element to add as a child of this element.
   * @throws BuilderException
   */
  public void addChild (Element child) throws BuilderException {
    assert (child != null);

    if (source != null)
      throw new BuilderException ("Element can only have one source module.");
    if (!(child instanceof ModuleBuilder))
      throw new BuilderException (
          "Element can only have a module as a sources.");

    setSource ((ModuleBuilder) child);
  }

  /**
   * Returns the source module builder for this element.
   * 
   * @return the source module builder.
   * @throws BuilderException
   *           if no source has been configured.
   */
  public final ModuleBuilder getSource () throws BuilderException {
    if (source == null)
      throw new BuilderException ("Transformation missing source module.");
    return source;
  }

  /**
   * Changes the source module.
   * 
   * @param source
   *          the builder for the source module.
   */
  public final void setSource (ModuleBuilder source) {
    assert (source != null);
    this.source = source;
  }

  private ModuleBuilder source = null;

}
