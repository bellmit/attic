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

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.Element;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.composition.Add;
import junit.framework.TestCase;

/**
 * Test cases for the Add module builder element.
 */
public final class AddBuilderTest extends TestCase {
  /**
   * Tests the AddBuilder module builder with no children, verifying that it
   * produces an Add module which has no parents.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddEmpty () throws BuilderException {
    AddBuilder testBuilder = new AddBuilder ();

    Add product = testBuilder.createModule ();
    assertFalse (product.getSources ().iterator ().hasNext ());
  }

  /**
   * Tests the AddBuilder module builder with sources which throw exceptions,
   * verifying that the exception is propagated.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddExceptionalSource () throws BuilderException {
    AddBuilder testBuilder = new AddBuilder ();

    testBuilder.addChild (new ConstantBuilder ());

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests the AddBuilder module with sources, verifying that the produced
   * module has the right sum.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddSources () throws BuilderException {
    AddBuilder testBuilder = new AddBuilder ();

    testBuilder.addChild (new ConstantBuilder (3.2));
    testBuilder.addChild (new ConstantBuilder (-6.1e-2));

    Add product = testBuilder.createModule ();
    assertEquals (3.2 + -6.1e-2, product.getValue (1, 4, 9));
  }

  /**
   * Verifies that AddBuilder rejects child elements that are not module
   * builders themselves.
   */
  public void testRejectNonModuleChild () {
    AddBuilder testBuilder = new AddBuilder ();

    Element nonModuleElement = new ChildlessElement () {
    };

    try {
      testBuilder.addChild (nonModuleElement);
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
