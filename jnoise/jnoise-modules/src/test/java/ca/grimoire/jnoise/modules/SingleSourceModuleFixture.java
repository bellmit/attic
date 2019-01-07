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

/**
 * Test fixture that verifies the properties of a given single-sourced module.
 */
public abstract class SingleSourceModuleFixture extends ModuleFixture {

  /**
   * Create a new test fixture. The passed test module <var>module</var> must
   * have a source module equal to (or identical to) the given <var>source</var>
   * module.
   * 
   * @param module
   *          the module under test.
   * @param source
   *          the source for the module under test.
   */
  public SingleSourceModuleFixture (SingleSourceModule module, Module source) {
    super (module);
    assert (module != null);

    this.module = module;
    this.source = source;

    assert (this.module == super.module);
  }

  /**
   * Test that the test module's getSource method returns something equal to the
   * test source module.
   */
  public void testGetSource () {
    assertEquals (source, module.getSource ());
  }

  /** The source module for the module under test. */
  protected final Module             source;
  /**
   * The module under test. This adds type information to the basic
   * ModuleFixture module field but fills the same role (and contains the same
   * value).
   */
  @SuppressWarnings ("hiding")
  protected final SingleSourceModule module;
}
