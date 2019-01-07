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
package ca.grimoire.jnoise.modules;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Test fixture for verifying the properties of a module based on
 * MultiSourceModule.
 */
public abstract class MultiSourceModuleFixture extends ModuleFixture {
  /**
   * Create a new MultiSource module fixture.
   * 
   * @param module
   *          the module under test.
   * @param sources
   *          the source modules for the test module.
   */
  public MultiSourceModuleFixture (MultiSourceModule module, Module... sources) {
    super (module);

    this.module = module;
    this.sources = sources;

    assert (this.module == super.module);
  }

  /**
   * Test the getSources () accessor to ensure that it returns something
   * sensible.
   */
  public void testGetSources () {
    Iterator<? extends Module> sources = module.getSources ().iterator ();
    Iterator<Module> expected = Arrays.asList (this.sources).iterator ();

    while (sources.hasNext ()) {
      assertEquals (sources.hasNext (), expected.hasNext ());
      assertEquals (sources.next (), expected.next ());
    }

    assertEquals (sources.hasNext (), expected.hasNext ());
  }

  /**
   * The module under test. This narrows the type information of the basic
   * ModuleFixture <code>module</code> field but fills the same role (and
   * contains the same value).
   */
  @SuppressWarnings ("hiding")
  protected final MultiSourceModule module;

  /**
   * The sources for the module under test. Derived tests must not alter this
   * array.
   */
  protected final Module[]          sources;
}
