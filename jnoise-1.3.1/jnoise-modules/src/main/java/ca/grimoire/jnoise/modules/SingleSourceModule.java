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
package ca.grimoire.jnoise.modules;

/**
 * Base class for modules fed by a single "source" module.
 */
public abstract class SingleSourceModule implements Module {

  /**
   * Creates a new single-source module.
   * 
   * @param source
   *          the source module.
   */
  public SingleSourceModule (Module source) {
    assert (source != null);

    this.source = source;
  }

  /**
   * Returns the module's source module.
   * 
   * @return the module's source.
   */
  public final Module getSource () {
    return source;
  }

  private final Module source;
}
