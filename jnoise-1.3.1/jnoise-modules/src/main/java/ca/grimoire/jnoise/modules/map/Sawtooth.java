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
package ca.grimoire.jnoise.modules.map;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.SingleSourceModule;
import ca.grimoire.jnoise.util.Hash;

/**
 * Noise module which maps a source module's output to a sawtooth wave. The
 * values from a source module are mapped into the range
 * <code>[0,<var>amplitude</var>)</code> once per <var>frequency</var>.
 * <p>
 * If you are using the included XML noise configuration system, Sawtooth
 * modules can be declared as
 * <p>
 * <blockquote>
 * <code>&lt;sawtooth frequency="<var>frequency</var>" amplitude="<var>amplitude</var>"&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &lt;/sawtooth&gt;</code> </blockquote>
 */
public final class Sawtooth extends SingleSourceModule {

  /**
   * Create a new Sawtooth module. The frequency and amplitude of the module
   * will both be set to <code>1.0</code>.
   * 
   * @param source
   *          the source module for the sawtooth wave.
   */
  public Sawtooth (Module source) {
    this (source, 1.0, 1.0);
  }

  /**
   * Create a new Sawtooth module with a given frequency and amplitude.
   * 
   * @param source
   *          the source module for the sawtooth wave.
   * @param frequency
   *          the frequency of the generated wave.
   * @param amplitude
   *          the amplitude of the generated wave.
   */
  public Sawtooth (Module source, double frequency, double amplitude) {
    super (source);

    this.frequency = frequency;
    this.amplitude = amplitude;
  }

  /**
   * Compare the module for equality with another object. The module is equal to
   * an object if and only if the object is also a Sawtooth module with the same
   * frequency and amplitude fed by an equal source.
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
    else if (!(object instanceof Sawtooth))
      return false;

    Sawtooth other = (Sawtooth) object;
    return getSource ().equals (other.getSource ())
        && other.getFrequency () == frequency
        && other.getAmplitude () == amplitude;
  }

  /**
   * Get the amplitude reached by the sawtooth wave.
   * 
   * @return the wave amplitude of the module.
   */
  public double getAmplitude () {
    return amplitude;
  }

  /**
   * Get the frequency of the sawtooth wave.
   * 
   * @return the wave frequency of the module.
   */
  public double getFrequency () {
    return frequency;
  }

  /**
   * Generate a noise value for a given location. The source module's noise
   * value is mapped to a sawtooth wave with the configured frequency and
   * amplitude.
   * 
   * @param x
   *          the X coordinate of the location.
   * @param y
   *          the Y coordinate of the location.
   * @param z
   *          the Z coordinate of the location.
   * @return the sawtooth value at the location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    return sawtooth (getSource ().getValue (x, y, z));
  }

  /**
   * Calcuate a hashcode for the object according to the general
   * hashcode/equality contract for Object. The hash code is computed from the
   * hashcode of the underlying module and the wave parameters of the module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ Hash.hashDouble (frequency)
        ^ Hash.hashDouble (amplitude);
  }

  /**
   * Computes the function S(<var>x</var>) = <var>a</var>(<var>f</var><var>x</var> - [<var>f</var><var>x</var>])
   * generating a sawtooth wave with the module's frequency <var>f</var> and
   * amplitude <var>a</var>.
   * 
   * @param value
   *          the parameter <var>x</var> to the sawtooth function.
   * @return the result of the sawtooth function.
   */
  private double sawtooth (double value) {
    return amplitude * (value - Math.floor (value * frequency));
  }

  private final double amplitude;

  private final double frequency;

}
