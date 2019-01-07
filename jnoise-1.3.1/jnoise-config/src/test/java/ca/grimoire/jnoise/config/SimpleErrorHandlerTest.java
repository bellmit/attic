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

import junit.framework.TestCase;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Tests the SimpleErrorHandler SAX error handler used to parse noise
 * configurations.
 */
public final class SimpleErrorHandlerTest extends TestCase {

  /**
   * Verifies that a handler which has had non-fatal errors applied to it throws
   * those exceptions.
   */
  public void testErrors () {
    ErrorHandler handler = new SimpleErrorHandler ();

    SAXParseException error = new SAXParseException ("Non-fatal", null);
    try {
      handler.error (error);
      fail ();
    } catch (SAXException saxe) {
      assertEquals (error, saxe);
    }
  }

  /**
   * Verifies that a handler which has had fatal errors applied to it throws
   * those exceptions.
   */
  public void testFatalErrors () {
    ErrorHandler handler = new SimpleErrorHandler ();

    SAXParseException error = new SAXParseException ("Fatal", null);
    try {
      handler.fatalError (error);
      fail ();
    } catch (SAXException saxe) {
      assertEquals (error, saxe);
    }
  }

  /**
   * Verifies that a handler which has warnings applied to it throws nothing.
   */
  public void testWarnings () {
    ErrorHandler handler = new SimpleErrorHandler ();

    SAXParseException warning = new SAXParseException ("Warning", null);
    try {
      handler.warning (warning);
    } catch (SAXException saxe) {
      fail (saxe.getLocalizedMessage ());
    }
  }
}
