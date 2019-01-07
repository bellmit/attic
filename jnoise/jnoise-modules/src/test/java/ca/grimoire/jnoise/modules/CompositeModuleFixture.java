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
 * Test fixture for verifying the properties of a composite module.
 */
public abstract class CompositeModuleFixture extends ModuleFixture {

  /**
   * Create a new fixture.
   * 
   * @param module
   *          the module to test.
   */
  public CompositeModuleFixture (CompositeModule module) {
    super (module);

    this.module = module;
    assert (super.module == this.module);
  }

  /**
   * Test that the module returned by getBase() generates the same values as the
   * composite module.
   */
  public void testComposition () {
    assertEquals (module.getValue (TEST_X1, TEST_Y1, TEST_Z1), module
        .getBase ().getValue (TEST_X1, TEST_Y1, TEST_Z1));
  }

  /**
   * The module under test. This adds type information to the basic
   * ModuleFixture module field but fills the same role (and contains the same
   * value).
   */
  @SuppressWarnings ("hiding")
  protected final CompositeModule module;
}
