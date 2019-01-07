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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Constant;
import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.generation.Perlin;
import junit.framework.TestCase;

/**
 * Tests for NoiseConfiguration.
 */
public final class NoiseConfigurationTest extends TestCase {
  /**
   * Verifies that the no-args constructor produces an empty configuration.
   */
  public void testEmptyConfiguration () {
    NoiseConfiguration testConfig = new NoiseConfiguration ();

    assertEquals (0, testConfig.getNoiseModules ().size ());
  }

  /**
   * Tests that an empty configuration can have modules added to it.
   */
  public void testAddFromEmpty () {
    NoiseConfiguration testConfig = new NoiseConfiguration ();

    List<? extends Module> modules = Arrays.asList (new Constant (2.0),
        new Constant (-1.2));

    for (Module module : modules)
      testConfig.addModule (module);

    assertEquals (modules, testConfig.getNoiseModules ());
  }

  /**
   * Verifies that creating a configuration with a list of modules makes those
   * modules available.
   */
  public void testNonEmpty () {
    List<Constant> modules = Arrays.asList (new Constant (-2.6e0),
        new Constant (-1.2e32));

    NoiseConfiguration testConfig = new NoiseConfiguration (modules);

    assertEquals (modules, testConfig.getNoiseModules ());
  }

  /**
   * Verifies that creating a configuration with a list of modules and then
   * adding subsequent modules makes all modules available in the correct order.
   */
  public void testAddToNonEmpty () {
    List<Constant> baseModules = Arrays.asList (new Constant (-0),
        new Constant (9.2e-22));
    NoiseConfiguration testConfig = new NoiseConfiguration (baseModules);

    List<Module> allModules = new LinkedList<Module> (baseModules);

    Perlin added = new Perlin (2, 8, 0.5, 0.5, Gradient.Quality.HIGH);
    testConfig.addModule (added);
    allModules.add (added);
  }
}
