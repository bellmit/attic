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
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.Element;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.basic.Constant;
import ca.grimoire.jnoise.modules.util.Cache;
import junit.framework.TestCase;

/**
 * Test cases for the Cache utility module builder.
 */
public final class CacheBuilderTest extends TestCase {
  /**
   * Tests the AddBuilder module builder with no children, verifying that it
   * produces an exception.
   */
  public void testCacheEmpty () {
    CacheBuilder testBuilder = new CacheBuilder ();

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests the CacheBuilder module builder with sources which throw exceptions,
   * verifying that the exception is propagated.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testCacheExceptionalSource () throws BuilderException {
    CacheBuilder testBuilder = new CacheBuilder ();

    testBuilder.addChild (new ConstantBuilder ());

    try {
      testBuilder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests the CacheBuilder module with a source, verifying that the produced
   * module has the right structure.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testCacheSource () throws BuilderException {
    CacheBuilder testBuilder = new CacheBuilder ();

    testBuilder.addChild (new ConstantBuilder (3.2));

    Cache product = testBuilder.createModule ();
    assertEquals (new Cache (new Constant (3.2)), product);
  }

  /**
   * Tests the CacheBuilder module with multiple sources, verifying that the
   * second and subsequent are rejected with an exception.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testCacheMultiSource () throws BuilderException {
    CacheBuilder testBuilder = new CacheBuilder ();

    testBuilder.addChild (new ConstantBuilder (3.2));

    try {
      testBuilder.addChild (new ConstantBuilder (3.2));
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }

    Cache product = testBuilder.createModule ();
    assertEquals (new Cache (new Constant (3.2)), product);
  }

  /**
   * Verifies that a CacheBuilder rejects child elements that are not module
   * builders themselves.
   */
  public void testRejectNonModuleChild () {
    CacheBuilder testBuilder = new CacheBuilder ();

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
