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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import ca.grimoire.jnoise.config.beans.BeanRegistry;

/**
 * Converts Properties objects mapping tags to class names into
 * {@link jnoise.config.beans.BeanRegistry} instances holding elements by name.
 * Also provides tools for loading a default tag registry and properties table.
 */
public final class RegistryFactory {
  private static final String         DEFAULT_PROPERTIES = "ca/grimoire/jnoise/config/tags.properties";
  private static final Class<Element> ELEMENT            = Element.class;

  /**
   * Loads a Properties table as a bean registry. For each property in the
   * table, the name is taken as the name to register, and the value as the name
   * of a class to load.
   * 
   * @param elements
   *          the Properties table defining the registry.
   * @return the complete registry.
   * @throws ClassCastException
   *           if a class is specified which does not implement {@link Element}
   * @throws ClassNotFoundException
   *           if a class is specified but unavailable.
   */
  public static BeanRegistry<Element> createRegistry (Properties elements)
      throws ClassCastException, ClassNotFoundException {
    return registerProperties (elements);
  }

  private static Properties loadDefaultProperties () {
    try {
      Properties defaults = new Properties ();

      InputStream input = Thread.currentThread ().getContextClassLoader ()
          .getResourceAsStream (DEFAULT_PROPERTIES);
      if (input != null)
        try {
          defaults.load (input);
        } finally {
          input.close ();
        }

      return defaults;
    } catch (IOException ioe) {
      throw new IllegalStateException (
          "Unable to load default element properties", ioe);
    }
  }

  private static void registerElement (BeanRegistry<Element> registry,
      Map.Entry<?, ?> element) throws ClassNotFoundException,
      ClassCastException {
    String elementClassName = element.getValue ().toString ();
    Class<?> clazz = Class.forName (elementClassName);
    Class<? extends Element> elementClass = clazz.asSubclass (ELEMENT);

    String elementName = element.getKey ().toString ();

    registry.registerBuilder (elementName, elementClass);
  }

  private static BeanRegistry<Element> registerProperties (Properties elements)
      throws ClassNotFoundException, ClassCastException {
    assert (elements != null);
    BeanRegistry<Element> registry = new BeanRegistry<Element> ();

    for (Map.Entry<?, ?> element : elements.entrySet ()) {
      registerElement (registry, element);
    }

    return registry;
  }

  /**
   * Creates a new factory that will create the default element set.
   */
  public RegistryFactory () {
    this (loadDefaultProperties ());
  }

  /**
   * Creates a new factory. The provided properties file will be used to create
   * registries using {@link #createRegistry(Properties)}.
   * 
   * @param elements
   *          the element properties table.
   */
  public RegistryFactory (Properties elements) {
    assert (elements != null);
    this.elements = elements;
  }

  /**
   * Creates a copy of the properties table provided to the factory. This
   * contains tag definitions for all included noise modules.
   * 
   * @return a properties table for the default tag set.
   */
  public Properties createProperties () {
    // Dear Sun: COVARIANT RETURN TYPES. Love, the world.
    return (Properties) elements.clone ();
  }

  /**
   * Creates a copy of the default element registry, which may safely be
   * subsequently modified. The default elements are defined in the Properties
   * returned from createProperties.
   * 
   * @return an element registry.
   * @throws ClassNotFoundException
   *           if the default registry specifies a nonexistant class.
   * @throws ClassCastException
   *           if the default registry specifies a non-element class.
   */
  public BeanRegistry<Element> createRegistry () throws ClassCastException,
      ClassNotFoundException {
    return createRegistry (createProperties ());
  }

  private final Properties elements;
}
