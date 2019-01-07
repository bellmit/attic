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

import java.awt.Color;

/**
 * Test cases to verify the properties of the CorrectedMonochrome color palette.
 */
public final class CorrectedMonochromeTest extends PaletteFixture {
  private static final float  HALFTONE    = (float) 0.5;
  private static final double BLACK_VALUE = -1;
  private static final double WHITE_VALUE = 1;
  private static final double GREY_VALUE  = 0;
  private static final double GAMMA       = 1.0;
  private static final Color  GRAY        = new Color (HALFTONE, HALFTONE,
                                              HALFTONE);

  /**
   * Create a test case, initialising the underlying fixture with the Monochrome
   * palette.
   */
  public CorrectedMonochromeTest () {
    super (new CorrectedMonochrome (BLACK_VALUE, WHITE_VALUE, GAMMA));
  }

  /**
   * Test that an arbitrarily-large "black" value generates the correct colour.
   */
  public void testBlack () {
    assertEquals (Color.BLACK, palette.getColor (BLACK_VALUE));
  }

  /**
   * Test that an arbitrarily-large "white" value generates the correct colour.
   */
  public void testWhite () {
    assertEquals (Color.WHITE, palette.getColor (WHITE_VALUE));
  }

  /**
   * Test that the halfway point along the palette generates a halftone gray.
   */
  public void testGray () {
    assertEquals (GRAY, palette.getColor (GREY_VALUE));
  }
}
