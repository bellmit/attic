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

/**
 * A convenience class for leaf elements, which do not accept any children.
 */
public abstract class ChildlessElement implements Element {

  /**
   * Rejects a child element. This method always throws a BuilderException when
   * called.
   * 
   * @param child
   *          the child element to add.
   * @throws BuilderException
   *           always.
   */
  public final void addChild (Element child) throws BuilderException {
    throw new BuilderException ("Element does not accept children.");
  }

}
