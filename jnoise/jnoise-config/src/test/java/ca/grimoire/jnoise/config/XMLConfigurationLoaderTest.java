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
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import ca.grimoire.jnoise.config.beans.BeanRegistry;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Constant;
import ca.grimoire.jnoise.modules.composition.Add;
import junit.framework.TestCase;

/**
 * Test cases for the XML streaming noise configuration loader.
 */
public final class XMLConfigurationLoaderTest extends TestCase {
  /**
   * Tests the noise configuration loader with a string document containing an
   * unknown noise element.
   * 
   * @throws IOException
   *           if the reader can't read a string.
   * @throws ConfigurationException
   *           if the configuration loader throws an unexpected exception.
   */
  public void testBogusElement () throws IOException, ConfigurationException {
    final String document = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<noise><xivits /></noise>";

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    try {
      testLoader.loadConfiguration (reader);
      fail ();
    } catch (ConfigurationException ce) {
      // Success case.
    }
  }

  /**
   * Tests the noise configuration loader with a string document containing a
   * single noise element defined by a custom element bean registry.
   * 
   * @throws ClassNotFoundException
   *           if the default registry cannot be loaded.
   * @throws ClassCastException
   *           if the default registry cannot be loaded.
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testCustomBeansDocument () throws ClassCastException,
      ClassNotFoundException, ConfigurationException, IOException {
    final String document = "<noise><k value=\"2.1\"/></noise>";
    final List<? extends Module> expected = Arrays.asList (new Constant (2.1));

    BeanRegistry<Element> tags = new RegistryFactory ().createRegistry ();
    tags.registerBuilder ("k", ConstantBuilder.class);

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader (tags);

    StringReader reader = new StringReader (document);
    NoiseConfiguration config = testLoader.loadConfiguration (reader);

    assertEquals (expected, config.getNoiseModules ());
  }

  /**
   * Tests the noise configuration loader with a string document containing a
   * single noise element and an XML declaration.
   * 
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testDocumentWithXmlDeclaration () throws ConfigurationException,
      IOException {
    final String document = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<noise><constant value=\"0.1\"/></noise>";
    final List<? extends Module> expected = Arrays.asList (new Constant (0.1));

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    NoiseConfiguration config = testLoader.loadConfiguration (reader);

    assertEquals (expected, config.getNoiseModules ());
  }

  /**
   * Tests the noise configuration loader with a string document containing an
   * element missing a mandatory attribute.
   * 
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testMissingAttribute () throws IOException,
      ConfigurationException {
    final String document = "<noise><constant /></noise>";

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    try {
      testLoader.loadConfiguration (reader);
      fail ();
    } catch (ConfigurationException ce) {
      // Success case.
    }
  }

  /**
   * Tests the noise configuration loader with a string document containing a
   * single noise element.
   * 
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testOneElementDocument () throws ConfigurationException,
      IOException {
    final String document = "<noise><constant value=\"2.1\"/></noise>";
    final List<? extends Module> expected = Arrays.asList (new Constant (2.1));

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    NoiseConfiguration config = testLoader.loadConfiguration (reader);

    assertEquals (expected, config.getNoiseModules ());
  }

  /**
   * Tests the noise configuration loader with a string document containing a
   * pair of noise elements.
   * 
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testTwoElementDocument () throws ConfigurationException,
      IOException {
    final String document = "<noise>" + "<constant value=\"2.4\"/>"
        + "<add><constant value=\"2.2\"/><constant value=\"2.3\"/></add>"
        + "</noise>";
    final List<? extends Module> expected = Arrays.asList (new Constant (2.4),
        new Add (new Constant (2.2), new Constant (2.3)));

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    NoiseConfiguration config = testLoader.loadConfiguration (reader);

    assertEquals (expected, config.getNoiseModules ());
  }

  /**
   * Tests the noise configuration loader with a string document containing an
   * unclosed noise element.
   * 
   * @throws ConfigurationException
   *           if the test fails.
   * @throws IOException
   *           if the loader is unable to read the document.
   */
  public void testUnclosedElement () throws ConfigurationException, IOException {
    final String document = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<noise><constant value=\"0.1\"></noise>";

    XMLConfigurationLoader testLoader = new XMLConfigurationLoader ();

    StringReader reader = new StringReader (document);
    try {
      testLoader.loadConfiguration (reader);
      fail ();
    } catch (ConfigurationException ce) {
      // Success case.
    }
  }
}
