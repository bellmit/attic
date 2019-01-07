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
 * A color palette which maps an arbitrary range to black and white, with
 * simplistic gamma correction.
 */
public final class CorrectedMonochrome implements Palette {

  private final double blackBound;
  private final double whiteBound;
  private final double gamma;

  /**
   * Creates a new color palette.
   * 
   * @param blackBound
   *          the value to map to black.
   * @param whiteBound
   *          the value to map to white.
   * @param gamma
   *          the exponent of the mapping curve.
   */
  public CorrectedMonochrome (double blackBound, double whiteBound, double gamma) {
    assert (blackBound <= whiteBound);

    this.blackBound = blackBound;
    this.whiteBound = whiteBound;
    this.gamma = gamma;
  }

  /**
   * Maps a noise value to black, grey, or white.
   * 
   * @param value
   *          the value to map.
   * @return the corresponding color.
   * @see jnoise.palettes.Palette#getColor(double)
   */
  public Color getColor (double value) {
    value = Math.min (value, whiteBound);
    value = Math.max (value, blackBound);

    value -= blackBound;
    value /= (whiteBound - blackBound);

    value = Math.pow (value, gamma);
    assert (value >= 0.0);
    assert (value <= 1.0);

    float channel = (float) value;
    return new Color (channel, channel, channel);
  }

}
