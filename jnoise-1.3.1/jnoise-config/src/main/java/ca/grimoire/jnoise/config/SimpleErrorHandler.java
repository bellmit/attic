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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * A simple error handling strategy which treates both fatal and non-fatal
 * errors as fatal errors and ignores warnings.
 */
public final class SimpleErrorHandler implements ErrorHandler {

  /**
   * Handles warnings by silently ignoring them.
   * 
   * @param exception
   *          the exception describing the warning.
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  public void warning (SAXParseException exception) {
    // XXX log4j/commons logging/something -- dropping on the floor bad.
  }

  /**
   * Handles theoretically-recoverable errors as if they were fatal errors.
   * 
   * @param exception
   *          the exception describing the parse error.
   * @throws SAXParseException
   *           passed to it.
   * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
   */
  public void error (SAXParseException exception) throws SAXParseException {
    fatalError (exception);
  }

  /**
   * Handles fatal errors by throwing them.
   * 
   * @param exception
   *          the exception describing the parse error.
   * @throws SAXParseException
   *           passed to it.
   * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
   */
  public void fatalError (SAXParseException exception) throws SAXParseException {
    throw exception;
  }

}
