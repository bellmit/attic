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
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.generation.Perlin;

/**
 * Element handler for building Perlin noise modules.
 * 
 * @see Perlin
 */
public final class PerlinBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a Perlin noise module with the configured parameters.
   * 
   * @return a Perlin module.
   * @throws BuilderException
   *           if any of the attributes have not been set or have been set to
   *           invalid values.
   * @see jnoise.config.modules.ModuleBuilder#createModule()
   */
  public Perlin createModule () throws BuilderException {
    checkSeed ();
    checkOctaves ();
    checkLacunarity ();
    checkPersistance ();
    checkQuality ();

    return new Perlin (seed, octaves, lacunarity, persistence, quality);
  }

  /**
   * Sets the lacunarity coefficient of generated perlin modules.
   * 
   * @param lacunarity
   *          the lacunarity to use.
   */
  public void setLacunarity (double lacunarity) {
    this.lacunarity = lacunarity;
    this.hasLacunarity = true;
  }

  /**
   * Sets the number of octaves to generate.
   * 
   * @param octaves
   *          the number of octaves the created module will generate.
   */
  public void setOctaves (int octaves) {
    this.octaves = octaves;
    this.hasOctaves = true;
  }

  /**
   * Changes the persistence coefficient for the generated noise module.
   * 
   * @param persistance
   *          the persistence coefficient.
   */
  public void setPersistence (double persistance) {
    this.persistence = persistance;
    this.hasPersistence = true;
  }

  /**
   * Changes the quality level for generated modules.
   * 
   * @param quality
   *          the quality to use.
   */
  public void setQuality (Gradient.Quality quality) {
    assert (quality != null);

    this.quality = quality;
  }

  /**
   * Changes the seed for generated modules.
   * 
   * @param seed
   *          the random number seed to use.
   */
  public void setSeed (int seed) {
    this.seed = seed;
    this.hasSeed = true;
  }

  private void checkLacunarity () throws BuilderException {
    if (!hasLacunarity)
      throw new BuilderException (
          "Perlin module requires lacunarity coefficient.");
  }

  private void checkOctaves () throws BuilderException {
    if (!hasOctaves || octaves <= 0)
      throw new BuilderException (
          "Perlin module requires a positive octaves count.");
  }

  private void checkPersistance () throws BuilderException {
    if (!hasPersistence)
      throw new BuilderException (
          "Perlin module requires persistence coefficient.");
  }

  private void checkQuality () throws BuilderException {
    if (quality == null)
      throw new BuilderException ("Perlin module requires quality.");
  }

  private void checkSeed () throws BuilderException {
    if (!hasSeed)
      throw new BuilderException ("Perlin module requires a seed number.");
  }

  private boolean          hasLacunarity  = false;
  private boolean          hasOctaves     = false;
  private boolean          hasPersistence = false;
  private boolean          hasSeed        = false;
  private double           lacunarity;
  private int              octaves;
  private double           persistence;
  private Gradient.Quality quality        = null;
  private int              seed;
}
