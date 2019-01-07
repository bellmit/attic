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
package ca.grimoire.jnoise.modules.util;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.SingleSourceModule;

/**
 * Utility module for caching a generated value. If the cached module appears
 * more than once in a generator tree and generates values for the same location
 * more than once in succession, Cache can speed it up by recording the value
 * for a location.
 * <p>
 * There is no mechanism for explicitly clearing the cache, so non-deterministic
 * source modules will not work correctly when cached. All of the jnoise modules
 * are deterministic; non-deterministic modules are of questionable use in the
 * context of coherent noise generation.
 * <p>
 * If you are using the included XML noise configuration system, Cache modules
 * can be declared as
 * <p>
 * <blockquote><code>&lt;cache&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/cache&gt;</code></blockquote>
 */
public final class Cache extends SingleSourceModule {

  /**
   * Creates a new cache for a given source.
   * 
   * @param source
   *          the module to cache values from.
   */
  public Cache (Module source) {
    super (source);
  }

  /**
   * Compares this module for equality with another object. All Cache modules
   * for equal sources are equal.
   * <p>
   * <b>Note:</b> Cache modules are not equal to their sources: this is
   * intentional. The Cache module itself doesn't necessarily provide all the
   * functionality of the source; furthermore, enforcing such a contract would
   * involve signifigant code in every module in order to recognize cached
   * instances of themselves to satisfy the reflexive property of the
   * equals(Object) method.
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
    else if (!(object instanceof Cache))
      return false;

    Cache other = (Cache) object;
    return getSource ().equals (other.getSource ());
  }

  /**
   * Gets the noise value for a location. If the location is the same as the
   * last location queried, this will forgo querying the source module and
   * return the cached value; otherwise, the request is passed on to the source
   * 
   * @param x
   *          the X coordinate of the location to generate noise for.
   * @param y
   *          the Y coordinate of the location to generate noise for.
   * @param z
   *          the Z coordinate of the location to generate noise for.
   * @return the noise value for the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    if (haveCache && x == cachedX && y == cachedY && z == cachedZ)
      return cachedValue;

    haveCache = true;
    cachedX = x;
    cachedY = y;
    cachedZ = z;
    return (cachedValue = getSource ().getValue (x, y, z));
  }

  /**
   * Calcuates a hashcode for this cache.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    // Distinct but predictable.
    return ~getSource ().hashCode ();
  }

  private double  cachedValue;
  private double  cachedX;
  private double  cachedY;
  private double  cachedZ;
  private boolean haveCache;
}
