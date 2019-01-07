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
package ca.grimoire.jnoise.config.modules.basic;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Gradient;

/**
 * A builder capable of creating basic Gradient noise modules. Gradient noise
 * modules require a random number seed and an interpolation quality, which can
 * be configured through {@link #setSeed(int)} and
 * {@link #setQuality(Gradient.Quality)} respectively.
 * 
 * @see Gradient
 */
public final class GradientBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a new GradientBuilder with no seed or quality set. Both must be
   * provided before modules can be created.
   */
  public GradientBuilder () {

  }

  /**
   * Creates a new IntegerBuilder with a seed and a quality already set.
   * 
   * @param seed
   *          the initial seed for created modules.
   * @param quality
   *          the interpolation quality for created modules.
   */
  public GradientBuilder (int seed, Gradient.Quality quality) {
    setSeed (seed);
    setQuality (quality);
  }

  /**
   * Generates an Gradient noise module using the configured seed and quality.
   * If either hasn't been set this throws a BuilderException instead.
   * 
   * @return a Gradient noise module.
   * @throws BuilderException
   *           if no random number seed has been configured.
   * @see #setSeed(int)
   * @see ModuleBuilder#createModule()
   */
  public Gradient createModule () throws BuilderException {
    if (!seedSet)
      throw new BuilderException ("No seed for gradient module.");
    if (quality == null)
      throw new BuilderException ("No quality for gradient module.");

    return new Gradient (seed, quality);
  }

  /**
   * Changes the random number seed used in subsequent modules.
   * 
   * @param seed
   *          the seed to use in subsequent modules.
   */
  public void setSeed (int seed) {
    this.seed = seed;
    this.seedSet = true;
  }

  /**
   * Changes the interpolation quality used in subsequent modules.
   * 
   * @param quality
   *          the quality level to use in subsequent modules.
   */
  public void setQuality (Gradient.Quality quality) {
    assert (quality != null);

    this.quality = quality;
  }

  private int              seed;
  private boolean          seedSet = false;
  private Gradient.Quality quality = null;
}
