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
package ca.grimoire.jnoise.config.beans;

/**
 * Indicates that a builder property was requested that was locatable but not
 * writable.
 */
class PropertyUnwritableException extends BeanException {

  /**
   * Creates an exception with no detail message.
   */
  public PropertyUnwritableException () {
    super ();
  }

  /**
   * Creates an exception with the given detail message.
   * 
   * @param message
   *          the detail message to use for the exception.
   */
  public PropertyUnwritableException (String message) {
    super (message);
  }

  /**
   * Creates an exception caused by another throwable condition, with a detail
   * message.
   * 
   * @param message
   *          the detail message to use for the exception.
   * @param cause
   *          the throwable condition that caused this exception.
   */
  public PropertyUnwritableException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * Creates an exception caused by another throwable condition.
   * 
   * @param cause
   *          the throwable condition that caused this exception.
   */
  public PropertyUnwritableException (Throwable cause) {
    super (cause);
  }
}
