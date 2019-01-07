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

import java.util.Arrays;

import ca.grimoire.jnoise.config.beans.BeanRegistry;
import ca.grimoire.jnoise.config.modules.ModuleElement;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.basic.Constant;
import junit.framework.TestCase;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Tests for the noise content handler class.
 */
public final class NoiseHandlerTest extends TestCase {

  private NoiseHandler testHandler;

  /**
   * Configures a noise handler with a known set of tags and handlers.
   * 
   * @throws Exception
   *           if setup fails.
   */
  @Override
  protected void setUp () throws Exception {
    super.setUp ();

    BeanRegistry<Element> tags = new BeanRegistry<Element> ();
    tags.registerBuilder ("noise", ModuleElement.class);
    tags.registerBuilder ("constant", ConstantBuilder.class);

    testHandler = new NoiseHandler (tags);
  }

  /**
   * Tests the noise content handler with a faked noise document containing a
   * single module definition.
   * 
   * @throws SAXException
   *           if the test handler generates an exception.
   * @throws BuilderException
   *           if the resulting elements do not form a valid noise
   *           configuration.
   */
  public void testSimpleNoise () throws SAXException, BuilderException {
    // Fake document
    testHandler.startDocument ();

    // <noise>
    testHandler.startElement ("", "noise", "noise", new AttributesImpl ());

    // <constant value="2.0" />
    AttributesImpl constantAttributes = new AttributesImpl ();
    constantAttributes.addAttribute ("", "value", "value", "CDATA", "2.0");
    testHandler.startElement ("", "constant", "constant", constantAttributes);
    testHandler.endElement ("", "constant", "constant");

    // </noise>
    testHandler.endElement ("", "noise", "noise");

    testHandler.endDocument ();
    // EOF

    Element root = testHandler.getRootElement ();

    NoiseConfigurationFactory factory = new NoiseConfigurationFactory ();
    NoiseConfiguration config = factory.createConfiguration (root);

    assertEquals (Arrays.asList (new Constant (2.0)), config.getNoiseModules ());
  }

  /**
   * Tests the noise content handler with a faked empty (and therefore invalid)
   * noise document.
   */
  public void testEmptyNoise () {
    // Fake document
    testHandler.startDocument ();

    try {
      testHandler.endDocument ();
      // EOF

      testHandler.getRootElement ();
      fail ();
    } catch (SAXException saxe) {
      // Success case.
    }
  }

  /**
   * Tests the noise content handler with a faked noise document containing no
   * module definitions.
   * 
   * @throws SAXException
   *           if the test handler generates an exception.
   * @throws BuilderException
   *           if the resulting elements do not form a valid noise
   *           configuration.
   */
  public void testSilence () throws SAXException, BuilderException {
    // Fake document
    testHandler.startDocument ();

    // <noise>
    testHandler.startElement ("", "noise", "noise", new AttributesImpl ());

    // </noise>
    testHandler.endElement ("", "noise", "noise");

    testHandler.endDocument ();
    // EOF

    Element root = testHandler.getRootElement ();

    NoiseConfigurationFactory factory = new NoiseConfigurationFactory ();
    NoiseConfiguration config = factory.createConfiguration (root);

    assertEquals (0, config.getNoiseModules ().size ());
  }

  /**
   * Tests the noise content handler with a faked noise document containing a
   * single module definition with an invalid attribute.
   * 
   * @throws SAXException
   *           if the test handler generates an unexpected exception.
   */
  public void testInvalidAttribute () throws SAXException {
    // Fake document
    testHandler.startDocument ();

    // <noise>
    testHandler.startElement ("", "noise", "noise", new AttributesImpl ());

    // <constant value="2.0" bogosity="on" />
    AttributesImpl constantAttributes = new AttributesImpl ();
    constantAttributes.addAttribute ("", "value", "value", "CDATA", "2.0");
    constantAttributes.addAttribute ("", "bogosity", "bogosity", "CDATA", "on");
    try {
      testHandler.startElement ("", "constant", "constant", constantAttributes);
      fail ();
    } catch (SAXException saxe) {
      // Success case.
    }
  }

  /**
   * Tests the noise content handler with a faked noise document containing a
   * single module definition missing a non-optional attribute.
   * 
   * @throws SAXException
   *           if the test handler generates an exception.
   */
  public void testMissingAttribute () throws SAXException {
    // Fake document
    testHandler.startDocument ();

    // <noise>
    testHandler.startElement ("", "noise", "noise", new AttributesImpl ());

    // <constant />
    AttributesImpl constantAttributes = new AttributesImpl ();
    testHandler.startElement ("", "constant", "constant", constantAttributes);
    testHandler.endElement ("", "constant", "constant");

    // </noise>
    testHandler.endElement ("", "noise", "noise");

    testHandler.endDocument ();
    // EOF

    Element root = testHandler.getRootElement ();

    NoiseConfigurationFactory factory = new NoiseConfigurationFactory ();
    try {
      factory.createConfiguration (root);
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }
}
