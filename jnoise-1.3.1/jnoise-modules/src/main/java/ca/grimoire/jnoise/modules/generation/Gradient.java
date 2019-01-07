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
package ca.grimoire.jnoise.modules.generation;

import ca.grimoire.jnoise.modules.CompositeModule;
import ca.grimoire.jnoise.modules.basic.Plane;
import ca.grimoire.jnoise.modules.map.Sawtooth;

/**
 * Noise module that generates a repeating gradient value along the X axis with
 * a given frequency and amplitude. This has nothing whatsoever to do with
 * {@link jnoise.modules.basic.Gradient}.
 * <p>
 * If you are using the included XML noise configuration system, Gradient
 * modules can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;gradient frequency="<var>frequency</var>" amplitude="<var>amplitude</var>" /&gt;</code>
 * </blockquote>
 */
public final class Gradient implements CompositeModule {

  private static Sawtooth composeGradient (double frequency, double amplitude) {
    return new Sawtooth (Plane.MODULE, frequency, amplitude);
  }

  /**
   * Creates a new Gradient module.
   * 
   * @param frequency
   *          the module frequency.
   * @param amplitude
   *          the module amplitude.
   */
  public Gradient (double frequency, double amplitude) {
    this.base = composeGradient (frequency, amplitude);
  }

  /**
   * Returns the amplitude of the gradient function.
   * 
   * @return the amplitude of the gradient function.
   */
  public double getAmplitude () {
    return base.getAmplitude ();
  }

  /**
   * Returns an equivalent Sawtooth module.
   * 
   * @return a Sawtooth module configured to generate the same pattern.
   * @see jnoise.modules.CompositeModule#getBase()
   */
  public Sawtooth getBase () {
    return base;
  }

  /**
   * Returns the frequency of the gradient function.
   * 
   * @return the gradient frequency.
   */
  public double getFrequency () {
    return base.getFrequency ();
  }

  /**
   * Returns the gradient value at a given location.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the gradient value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return getBase ().getValue (x, y, z);
  }

  /**
   * Compares this module for equality with another object. Gradient modules are
   * equal to other Gradient modules with the same frequency and amplitude.
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
    else if (!(object instanceof Gradient))
      return false;

    Gradient other = (Gradient) object;
    return base.equals (other.getBase ());
  }

  /**
   * Calcuates a hashcode for this gradient module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return base.hashCode ();
  }

  private final Sawtooth base;
}
