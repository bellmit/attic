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

import java.util.Arrays;
import java.util.Iterator;

/**
 * Abstract class containing common code for noise modules based on arbitrary
 * collections of source modules.
 */
public abstract class MultiSourceModule implements Module {

  /**
   * Creates a new multi-source module.
   * 
   * @param sources
   *          the modules to use as sources for the module.
   */
  public MultiSourceModule (Module... sources) {
    assert (sources != null);

    // Safety net in case we're passed an externally-retained array, to ensure
    // that the module is immutable.
    this.sources = sources.clone ();
  }

  /**
   * Compares this module for equality with another object. This module is equal
   * to an object if and only if the object is also a MultiSource module fed by
   * equal source modules. MultiSource modules with the same sources in a
   * different order do not compare equal, as their outputs may be slightly (or
   * even drastically) different.
   * <p>
   * Derived classes should generally override this further; this implementation
   * will incorrectly declare two modules of incompatible classes derived from
   * this base to be equal if they use the same modules.
   * 
   * @param object
   *          the object to compare.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof MultiSourceModule))
      return false;

    MultiSourceModule other = (MultiSourceModule) object;
    Iterator<? extends Module> mine = getSources ().iterator ();
    Iterator<? extends Module> others = other.getSources ().iterator ();
    while (mine.hasNext ()) {
      if (!others.hasNext ())
        // Other has fewer modules.
        return false;
      if (!(mine.next ().equals (others.next ())))
        return false;
    }
    if (others.hasNext ())
      // Other has more modules.
      return false;

    return true;
  }

  /**
   * Gets an iterable collection of the source modules. The returned collection
   * is immutable.
   * 
   * @return an iterable collection of source modules.
   */
  public final Iterable<? extends Module> getSources () {
    return Arrays.asList (sources);
  }

  /**
   * Generates a hashcode for this multisource module.
   * 
   * @return this module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    int hashCode = 0;

    for (Module source : sources) {
      assert (source != null);
      hashCode ^= source.hashCode ();
    }

    return hashCode;
  }

  private final Module[] sources;
}
