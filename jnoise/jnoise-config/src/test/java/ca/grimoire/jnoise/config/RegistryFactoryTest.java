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
package ca.grimoire.jnoise.config;

import java.util.Map;
import java.util.Properties;

import ca.grimoire.jnoise.config.beans.BeanRegistry;
import ca.grimoire.jnoise.config.beans.InvalidBeanException;
import ca.grimoire.jnoise.config.beans.UnknownBeanException;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import junit.framework.TestCase;

/**
 * Test cases for the RegistryFactory class.
 */
public final class RegistryFactoryTest extends TestCase {

  /**
   * Attempts to create a registry with a non-Element class, and checks for the
   * appropriate exception.
   * 
   * @throws ClassNotFoundException
   *           if the factory cannot locate java.lang.Object.
   */
  public void testClassCastException () throws ClassNotFoundException {
    Properties tags = new Properties ();
    tags.put ("object", "java.lang.Object");

    RegistryFactory testFactory = new RegistryFactory (tags);
    try {
      testFactory.createRegistry ();
      fail ();
    } catch (ClassCastException cce) {
      // Success case.
    }
  }

  /**
   * Attempts to create a registry using a nonexistant class and checks for the
   * appropriate exception.
   */
  public void testClassNotFoundException () {
    Properties tags = new Properties ();
    tags.put ("disgruntled", "non.existant.Type");

    RegistryFactory testFactory = new RegistryFactory (tags);
    try {
      testFactory.createRegistry ();
      fail ();
    } catch (ClassNotFoundException cnfe) {
      // Success case.
    }
  }

  /**
   * Tests that the registry factory creates registries with the correct class
   * associations by creating a registry with a known association.
   * 
   * @throws UnknownBeanException
   *           if the resulting registry fails.
   * @throws InvalidBeanException
   *           if the resulting registry fails.
   * @throws ClassNotFoundException
   *           if the factory cannot locate the test element class.
   */
  public void testLoadExisting () throws UnknownBeanException,
      InvalidBeanException, ClassNotFoundException {
    Properties tags = new Properties ();
    tags.setProperty("constant", ConstantBuilder.class.getCanonicalName());

    RegistryFactory testFactory = new RegistryFactory (tags);
    BeanRegistry<Element> product = testFactory.createRegistry ();
    assertEquals (ConstantBuilder.class, product.create ("constant")
        .getClass ());
  }

  /**
   * Verifies that the default property set can be loaded.
   */
  public void testLoadProperties () {
    Properties loaded = new RegistryFactory ().createProperties ();
    assertTrue (loaded.size () > 0);
  }

  /**
   * Verifies that the default registry and the default properties are
   * consistent.
   * 
   * @throws InvalidBeanException
   *           if one of the beans in the registry is invalid.
   * @throws UnknownBeanException
   *           if one of the beans described in the properties is unavailable in
   *           the registry.
   * @throws ClassNotFoundException
   *           if one of the bean classes in the default registry is not
   *           available.
   * @throws ClassCastException
   *           if one of the bean classes in the default registry is not an
   *           Element.
   */
  public void testLoadRegistry () throws UnknownBeanException,
      InvalidBeanException, ClassCastException, ClassNotFoundException {
    RegistryFactory testFactory = new RegistryFactory ();

    Properties loadedElements = testFactory.createProperties ();
    BeanRegistry<Element> loadedRegistry = testFactory.createRegistry ();

    for (Map.Entry<?, ?> element : loadedElements.entrySet ())
      assertNotNull (loadedRegistry.create (element.getKey ().toString ()));
  }
}
