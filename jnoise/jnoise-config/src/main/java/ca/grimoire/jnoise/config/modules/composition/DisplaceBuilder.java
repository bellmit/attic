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
import ca.grimoire.jnoise.config.Element;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.config.modules.ModuleElement;
import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.composition.Displace;

/**
 * Module builder element for Select noise modules.
 */
public final class DisplaceBuilder implements ModuleBuilder {

  private static final int SOURCE_CHILD      = 0;
  private static final int REQUIRED_CHILDREN = 4;
  private static final int X_CHILD           = 1;
  private static final int Y_CHILD           = 2;
  private static final int Z_CHILD           = 3;

  /**
   * Adds a child element to this element. The element must be a ModuleBuilder.
   * This element expects exactly four children: source, and X, Y, and Z
   * displacements.
   * 
   * @param child
   *          the element to register.
   * @throws BuilderException
   *           if <var>child</var> is not a ModuleBuilder or four child
   *           elements have already been registered.
   */
  public void addChild (Element child) throws BuilderException {
    assert (child != null);

    assert (sourceManager.getChildren ().size () <= REQUIRED_CHILDREN);
    if (sourceManager.getChildren ().size () == REQUIRED_CHILDREN)
      throw new BuilderException (
          "Select module requires three source modules.");

    sourceManager.addChild (child);
  }

  /**
   * Creates a new Displace module. The child modules are used as the source and
   * X, Y, and Z displacements, in that order.
   * 
   * @return the completed Displace module.
   * @throws BuilderException
   *           if the builder is missing children or required settings or if a
   *           source builder fails to build a module.
   */
  public Displace createModule () throws BuilderException {
    List<ModuleBuilder> builders = checkSources ();

    Module source = builders.get (SOURCE_CHILD).createModule ();
    Module x = builders.get (X_CHILD).createModule ();
    Module y = builders.get (Y_CHILD).createModule ();
    Module z = builders.get (Z_CHILD).createModule ();

    return new Displace (source, x, y, z);
  }

  private List<ModuleBuilder> checkSources () throws BuilderException {
    List<ModuleBuilder> builders = sourceManager.getChildren ();
    if (builders.size () != REQUIRED_CHILDREN)
      throw new BuilderException (
          "Displace module requires one sources and three displacement modules.");
    return builders;
  }

  private final ModuleElement sourceManager = new ModuleElement ();
}
