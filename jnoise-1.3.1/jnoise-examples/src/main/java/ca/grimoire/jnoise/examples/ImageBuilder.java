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
package ca.grimoire.jnoise.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import ca.grimoire.jnoise.models.PlaneModel;
import ca.grimoire.jnoise.palettes.CorrectedMonochrome;
import ca.grimoire.jnoise.palettes.Palette;

/**
 * Tool for generating Images from plane maps and noise modules.
 */
public final class ImageBuilder {
  private static double interpolate (int value, int limit, double extent) {
    double ratio = value;
    ratio /= limit;
    return ratio * extent;
  }

  /**
   * Creates a new builder for a given plane map and extents. The image extents
   * refer to the plane model's coordinate space and do not affect the size of
   * the image.
   * 
   * @param model
   *          the model mapping the module to draw to a plane.
   * @param u
   *          the size of the image's coordinate space along the Y axis.
   * @param v
   *          the size of the image's coordinate space along the X axis.
   */
  public ImageBuilder (PlaneModel model, double u, double v) {
    assert (model != null);

    this.model = model;
    this.uExtent = u;
    this.vExtent = v;
  }

  /**
   * Creates an image from the noise module of given dimensions.
   * 
   * @param width
   *          the width of the image.
   * @param height
   *          the height of the image.
   * @return a new image of the module.
   */
  public Image createImage (int width, int height) {
    BufferedImage image = new BufferedImage (width, height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics ();

    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    double total = 0.0;

    double[][] samples = new double[height][width];

    for (int row = 0; row < height; ++row) {
      double y = interpolate (row, height, uExtent);
      for (int col = 0; col < width; ++col) {
        double x = interpolate (col, width, vExtent);
        double v = model.getValue (x, y);
        min = Math.min (min, v);
        max = Math.max (max, v);

        total += v;

        samples[row][col] = v;
      }
    }

    double average = total / (height * width);
    double gamma = (average - min) / (max - average);

    Palette palette = new CorrectedMonochrome (min, max, gamma);

    for (int row = 0; row < height; ++row) {
      for (int col = 0; col < width; ++col) {
        Color color = palette.getColor (samples[row][col]);
        g.setColor (color);
        g.drawLine (col, row, col, row); // putpixel
      }
    }

    return image;
  }

  private final PlaneModel model;
  private final double     uExtent, vExtent;
}
