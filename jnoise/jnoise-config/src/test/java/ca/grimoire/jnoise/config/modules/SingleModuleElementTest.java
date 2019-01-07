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
import ca.grimoire.jnoise.config.modules.basic.IntegerBuilder;
import ca.grimoire.jnoise.config.modules.transform.RotateBuilder;
import junit.framework.TestCase;

/**
 * Test cases for the single module element handler.
 */
public final class SingleModuleElementTest extends TestCase {

  /**
   * Verifies that the no argument constructor creates an element that contains
   * no source.
   */
  public void testSingleModuleElement () {
    SingleModuleElement testDesc = new SingleModuleElement ();

    try {
      testDesc.getSource ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Verifies that a module element passed to a single module element's addChild
   * method is accepted.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddChildElement () throws BuilderException {
    RotateBuilder child = new RotateBuilder ();

    SingleModuleElement testDesc = new SingleModuleElement ();
    testDesc.addChild (child);

    assertSame (child, testDesc.getSource ());
  }

  /**
   * Verifies that a module element passed to a single module element's addChild
   * method is accepted but subsequent children are rejected.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testAddTwoChildElements () throws BuilderException {
    RotateBuilder child = new RotateBuilder ();

    SingleModuleElement testDesc = new SingleModuleElement ();
    testDesc.addChild (child);

    try {
      testDesc.addChild (new IntegerBuilder ());
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }

    assertSame (child, testDesc.getSource ());
  }
}
