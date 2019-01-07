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
package ca.grimoire.jnoise.palettes;

import java.awt.Color;

/**
 * Palette mapping the range [-1, 1] linearly to black and white respectively.
 * 
 * @deprecated All functionality offered by this class is also exposed through
 *             {@link jnoise.palettes.CorrectedMonochrome}.
 */
@Deprecated
public final class Monochrome implements Palette {

  /** Sharable instance. */
  public static final Monochrome PALETTE     = new Monochrome ();
  private static final double    BLACK_BOUND = -1.0;
  private static final double    WHITE_BOUND = 1.0;

  /**
   * Maps a noise value to black, grey, or white. Values between -1 and 1 are
   * mapped to shades of grey; values less than or equal to -1 are mapped to
   * black, and values greater than or equal to 1 to white.
   * 
   * @param value
   *          the value to map.
   * @return the corresponding color.
   * @see jnoise.palettes.Palette#getColor(double)
   */
  public Color getColor (double value) {
    value = Math.min (value, WHITE_BOUND);
    value = Math.max (value, BLACK_BOUND);

    value -= BLACK_BOUND;
    value /= (WHITE_BOUND - BLACK_BOUND);

    float channel = (float) value;
    return new Color (channel, channel, channel);
  }

}
