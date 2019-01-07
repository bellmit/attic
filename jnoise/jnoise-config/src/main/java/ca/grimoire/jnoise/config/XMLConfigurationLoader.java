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
import java.io.Reader;

import ca.grimoire.jnoise.config.beans.BeanRegistry;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Loads XML documents and produces NoiseConfigurations. Uses SAX under the
 * hood; applications may use the usual SAX system properties to customize SAX's
 * behaviour.
 */
public final class XMLConfigurationLoader {

  private static final NoiseConfigurationFactory ELEMENT_CONVERTER = new NoiseConfigurationFactory ();
  private static final ErrorHandler ERROR_HANDLER = new SimpleErrorHandler ();

  private static BeanRegistry<Element> createDefaultTags ()
      throws ConfigurationException {
    try {
      return new RegistryFactory ().createRegistry ();
    } catch (ClassCastException e) {
      throw new ConfigurationException (e);
    } catch (ClassNotFoundException e) {
      throw new ConfigurationException (e);
    }
  }

  /**
   * Creates a configuration loader using a default set of tags included with
   * JNoise. The default tags are provided by
   * {@link RegistryFactory#createRegistry()}.
   * 
   * @throws ConfigurationException
   *           if the default tags can't be loaded.
   */
  public XMLConfigurationLoader () throws ConfigurationException {
    this (createDefaultTags ());
  }

  /**
   * Creates an XML configuration loader. This is provided for applications
   * wishing to support custom noise elements. Recommended practice is to obtain
   * a copy of the default registry using a {@link RegistryFactory}, then add
   * the custom elements to that registry.
   * 
   * @param tags
   *          the element registry defining tags for this loader.
   */
  public XMLConfigurationLoader (BeanRegistry<Element> tags) {
    assert (tags != null);

    this.tags = tags;
  }

  /**
   * Loads an XML document from a stream and constructs the corresponding noise
   * configuration.
   * 
   * @param reader
   *          the XML source stream.
   * @return the complete noise configuration.
   * @throws ConfigurationException
   *           if the configuration XML is invalid or the XML engine fails.
   * @throws IOException
   *           if there is a problem reading the stream.
   */
  public NoiseConfiguration loadConfiguration (Reader reader)
      throws ConfigurationException, IOException {
    assert (reader != null);

    try {
      XMLReader parser = XMLReaderFactory.createXMLReader ();
      NoiseHandler handler = new NoiseHandler (tags);
      parser.setContentHandler (handler);
      parser.setErrorHandler(ERROR_HANDLER);
      
      parser.parse (new InputSource (reader));

      return ELEMENT_CONVERTER.createConfiguration (handler.getRootElement ());

    } catch (SAXException e) {
      throw new ConfigurationException (e);

    }
  }

  private BeanRegistry<Element> tags;

}
