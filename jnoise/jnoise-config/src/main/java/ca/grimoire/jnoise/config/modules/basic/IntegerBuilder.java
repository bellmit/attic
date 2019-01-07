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
import ca.grimoire.jnoise.modules.basic.Integer;

/**
 * A builder capable of creating Integer noise modules. Integer noise modules
 * require a random number seed, which can be configured through
 * {@link #setSeed(int)}.
 * 
 * @see Integer
 */
public final class IntegerBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a new IntegerBuilder with no seed set. The seed for the created
   * modules must be passed to {@link #setSeed(int)} before calling
   * {@link #createModule()}.
   */
  public IntegerBuilder () {

  }

  /**
   * Creates a new IntegerBuilder with a seed.
   * 
   * @param seed
   *          the initial seed for created modules.
   */
  public IntegerBuilder (int seed) {
    setSeed (seed);
  }

  /**
   * Generates an Integer noise module using the configured seed. If no seed has
   * been configured yet this throws a BuilderException instead.
   * 
   * @return an Integer noise module.
   * @throws BuilderException
   *           if no random number seed has been configured.
   * @see #setSeed(int)
   * @see ModuleBuilder#createModule()
   */
  public Integer createModule () throws BuilderException {
    if (!seedSet)
      throw new BuilderException ("No seed for integer module.");

    return new Integer (seed);
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

  private int     seed;
  private boolean seedSet = false;
}
