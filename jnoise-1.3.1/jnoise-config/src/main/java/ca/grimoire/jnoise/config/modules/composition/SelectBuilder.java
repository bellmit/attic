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
import ca.grimoire.jnoise.modules.composition.Select;

/**
 * Module builder element for Select noise modules.
 */
public final class SelectBuilder implements ModuleBuilder {

  private static final int LEFT_CHILD        = 0;
  private static final int REQUIRED_CHILDREN = 3;
  private static final int RIGHT_CHILD       = 1;
  private static final int SELECTOR_CHILD    = 2;

  /**
   * Adds a child element to this element. The element must be a ModuleBuilder.
   * This element expects exactly three children.
   * 
   * @param child
   *          the element to register.
   * @throws BuilderException
   *           if <var>child</var> is not a ModuleBuilder or three child
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
   * Creates a new Select module. The child modules are used as the lower and
   * upper sources, and selector, in that order.
   * 
   * @return the completed Select module.
   * @throws BuilderException
   *           if the builder is missing children or required settings or if a
   *           source builder fails to build a module.
   */
  public Select createModule () throws BuilderException {
    checkFalloff ();
    checkThreshold ();

    List<ModuleBuilder> builders = checkSources ();

    Module lower = builders.get (LEFT_CHILD).createModule ();
    Module upper = builders.get (RIGHT_CHILD).createModule ();
    Module selector = builders.get (SELECTOR_CHILD).createModule ();

    return new Select (lower, upper, selector, threshold, falloff);
  }

  /**
   * Changes the falloff rate for created Select modules.
   * 
   * @param falloff
   *          the falloff for modules.
   */
  public void setFalloff (double falloff) {
    this.falloff = falloff;
    this.hasFalloff = true;
  }

  /**
   * Changes the transition threshold for created Select modules.
   * 
   * @param threshold
   *          the threshold for Select modules.
   */
  public void setThreshold (double threshold) {
    this.threshold = threshold;
    this.hasThreshold = true;
  }

  private void checkFalloff () throws BuilderException {
    if (falloff < 0.0 || !hasFalloff)
      throw new BuilderException (
          "Select module requires non-negative falloff rate.");
  }

  private List<ModuleBuilder> checkSources () throws BuilderException {
    List<ModuleBuilder> builders = sourceManager.getChildren ();
    if (builders.size () != REQUIRED_CHILDREN)
      throw new BuilderException (
          "Select module requires two sources and a selector module.");
    return builders;
  }

  private void checkThreshold () throws BuilderException {
    if (!hasThreshold)
      throw new BuilderException ("Select module requires threshold.");
  }

  private boolean             hasFalloff    = false;
  private boolean             hasThreshold  = false;

  private final ModuleElement sourceManager = new ModuleElement ();

  private double              threshold, falloff;
}
