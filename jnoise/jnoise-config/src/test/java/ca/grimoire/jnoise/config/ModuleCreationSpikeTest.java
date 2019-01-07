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

import ca.grimoire.jnoise.config.beans.BeanRegistry;
import ca.grimoire.jnoise.config.beans.NamedAttributeProxy;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.config.modules.basic.ConstantBuilder;
import ca.grimoire.jnoise.modules.Module;
import junit.framework.TestCase;

/**
 * A spike solution for named module type creation, with named attributes.
 * Designed to be similar to the process used to parse an XML noise description,
 * to act as a starting point for further development.
 */
public final class ModuleCreationSpikeTest extends TestCase {
  /**
   * Fakes a parsing pass by setting up a registry, "parsing" a tag by name with
   * a named attribute, and producing the final builder.
   * 
   * @throws Exception
   *           if the test fails due to an exception.
   */
  public void testSingleModule () throws Exception {
    // Create a source for module builders
    BeanRegistry<ModuleBuilder> registry = new BeanRegistry<ModuleBuilder> ();

    // Register tags
    registry.registerBuilder ("constant", ConstantBuilder.class);

    // ... ok, parsing.
    // We saw a tag!
    ModuleBuilder builder = registry.create ("constant");

    // With an attribute.
    NamedAttributeProxy proxy = new NamedAttributeProxy (builder);
    proxy.setProperty ("value", "1.0");

    // Some stuff happens here.

    // Then we get around to creating the actual module.
    Module product = builder.createModule ();
    assertEquals (1.0, product.getValue (1, 2, 3));
  }
}
