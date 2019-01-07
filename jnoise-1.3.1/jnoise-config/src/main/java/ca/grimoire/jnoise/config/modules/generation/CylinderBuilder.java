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
package ca.grimoire.jnoise.config.modules.generation;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.generation.Cylinder;

/**
 * Element handler for Cylinder noise modules.
 * 
 * @see Cylinder
 */
public final class CylinderBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a Cylinder noise module using the child element as a source.
   * 
   * @return the created Cylinder module.
   * @throws BuilderException
   *           if the source module has not been provided.
   */
  public Cylinder createModule () throws BuilderException {
    if (!hasFrequency)
      throw new BuilderException ("Cylinder requires frequency.");
    if (!hasAmplitude)
      throw new BuilderException ("Cylinder requires amplitude.");
    if (frequency < 0.0)
      throw new BuilderException ("Cylinder requires non-negative frequency.");

    return new Cylinder (frequency, amplitude);
  }

  /**
   * Changes the amplitude of subsequent modules.
   * 
   * @param amplitude
   *          the amplitude for created modules.
   */
  public void setAmplitude (double amplitude) {
    this.amplitude = amplitude;
    this.hasAmplitude = true;
  }

  /**
   * Changes the frequency for subsequent modules.
   * 
   * @param frequency
   *          the frequency for subsequent modules.
   */
  public void setFrequency (double frequency) {
    this.frequency = frequency;
    this.hasFrequency = true;
  }

  private double frequency, amplitude;

  private boolean hasFrequency = false, hasAmplitude = false;
}
