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
 * Copyright (C) 2006 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.palettes;

import junit.framework.TestCase;

/**
 * Test fixture for verifying an arbitrary Palette implementation.
 */
public abstract class PaletteFixture extends TestCase {

  /** An arbitrary test value. */
  protected static final double TEST_VALUE = 0.2;

  /**
   * Create a new test fixture for a given palette implementation.
   * 
   * @param palette
   *          the palette implementation under test.
   */
  public PaletteFixture (Palette palette) {
    assert (palette != null);
    this.palette = palette;
  }

  /**
   * Ensure that sequential calls to getColor return equal colours.
   */
  public final void testRepeatable () {
    assertEquals (palette.getColor (TEST_VALUE), palette.getColor (TEST_VALUE));
  }

  /** The palette implementation under test. */
  protected final Palette palette;
}
