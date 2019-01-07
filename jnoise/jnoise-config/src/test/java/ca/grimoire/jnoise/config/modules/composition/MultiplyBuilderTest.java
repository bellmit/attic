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
import ca.grimoire.jnoise.modules.basic.Constant;
import ca.grimoire.jnoise.modules.composition.Multiply;
import junit.framework.TestCase;

/**
 * Test cases for the Multiply module builder element.
 */
public final class MultiplyBuilderTest extends TestCase {
  /**
   * Tests the MultiplyBuilder module builder with no children, verifying that
   * it produces an Multiply module which has no parents.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMultiplyEmpty () throws BuilderException {
    MultiplyBuilder testBuilder = new MultiplyBuilder ();

    Multiply product = testBuilder.createModule ();
    assertEquals (new Multiply (), product);
  }

  /**
   * Tests the MultiplyBuilder module builder with sources which throw
   * exceptions, verifying that the exception is propagated.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMultiplyExceptionalSource () throws BuilderException {
    MultiplyBuilder testBuilder = new MultiplyBuilder ();

    testBuilder.addChild (new ConstantBuilder ());

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests the MultiplyBuilder module with sources, verifying that the product
   * is the correct module tree.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testMultiplySources () throws BuilderException {
    MultiplyBuilder testBuilder = new MultiplyBuilder ();

    testBuilder.addChild (new ConstantBuilder (3.2));
    testBuilder.addChild (new ConstantBuilder (-6.1e-2));

    Multiply product = testBuilder.createModule ();
    assertEquals (new Multiply (new Constant (3.2), new Constant (-6.1e-2)),
        product);
  }

  /**
   * Verifies that MultiplyBuilder rejects child elements that are not module
   * builders themselves.
   */
  public void testRejectNonModuleChild () {
    MultiplyBuilder testBuilder = new MultiplyBuilder ();

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
