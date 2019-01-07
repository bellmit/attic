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

import java.util.Arrays;
import java.util.List;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import junit.framework.TestCase;

/**
 * Tests the ModuleElement convenience class.
 */
public final class ModuleElementTest extends TestCase {

  /**
   * Tests that module builders added to the test module element can be
   * retrieved from it in order.
   * 
   * @throws BuilderException
   *           if the test element rejects the module builders.
   */
  public void testAddChildren () throws BuilderException {
    ModuleElement testElement = new ModuleElement ();

    List<? extends ModuleBuilder> children = Arrays.asList (
        new ConstantBuilder (1.0), new ConstantBuilder (1.25));

    for (ModuleBuilder child : children)
      testElement.addChild (child);

    assertEquals (children, testElement.getChildren ());
  }

  /**
   * Tests that ModuleElements reject non-ModuleBuilder children.
   */
  public void testAddNonBuilder () {
    ModuleElement testElement = new ModuleElement ();

    try {
      testElement.addChild (new ModuleElement ());
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
