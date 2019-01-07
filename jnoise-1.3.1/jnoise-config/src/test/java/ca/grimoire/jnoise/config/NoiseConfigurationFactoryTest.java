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

import ca.grimoire.jnoise.config.modules.ModuleElement;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import junit.framework.TestCase;

/**
 * Tests for the element-converting noise configuration factory.
 */
public final class NoiseConfigurationFactoryTest extends TestCase {
  /**
   * Tests that the configuration factory can convert bare module builders into
   * noise configurations.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testModuleBuilder () throws BuilderException {
    ConstantBuilder builder = new ConstantBuilder (2.4);

    NoiseConfigurationFactory configFactory = new NoiseConfigurationFactory ();

    NoiseConfiguration config = configFactory.createConfiguration (builder);
    assertEquals (Arrays.asList (builder.createModule ()), config
        .getNoiseModules ());
  }

  /**
   * Tests that the configuration factory can convert module-containing elements
   * into noise configurations.
   * 
   * @throws BuilderException
   *           if the test fails.
   */
  public void testNoiseElement () throws BuilderException {
    ConstantBuilder builder = new ConstantBuilder (2.4);
    ModuleElement root = new ModuleElement ();
    root.addChild (builder);

    NoiseConfigurationFactory configFactory = new NoiseConfigurationFactory ();

    NoiseConfiguration config = configFactory.createConfiguration (root);
    assertEquals (Arrays.asList (builder.createModule ()), config
        .getNoiseModules ());
  }
}
