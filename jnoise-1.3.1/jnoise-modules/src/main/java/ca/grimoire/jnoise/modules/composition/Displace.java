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
package ca.grimoire.jnoise.modules.composition;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.SingleSourceModule;

/**
 * Module that takes the coordinates of a getValue request and displaces them
 * according to the values of three displacer modules (one per axis) at the
 * original point. The displaced point is used to generate the result value from
 * the source module.
 * <p>
 * This can be used with, eg., three Perlin modules as displacers to generate
 * coherent turbulence.
 * <p>
 * If you are using the included XML noise configuration system, Displace
 * modules can be declared as
 * <p>
 * <blockquote> <code>&lt;displace&gt;<br>
 * &nbsp;<var>&lt;source module /&gt;</var><br>
 * &nbsp;<var>&lt;X-axis displacement module /&gt;</var><br>
 * &nbsp;<var>&lt;Y-axis displacement module /&gt;</var><br>
 * &nbsp;<var>&lt;Z-axis displacement module /&gt;</var><br>
 * &lt;/displace&gt;</code> </blockquote>
 */
public final class Displace extends SingleSourceModule {

  /**
   * Creates a new Displace module, using the same displacer for all three axes.
   * 
   * @param source
   *          the module to generate noise from.
   * @param displacer
   *          the module displacing the axes.
   */
  public Displace (Module source, Module displacer) {
    this (source, displacer, displacer, displacer);
  }

  /**
   * Creates a new Displace module.
   * 
   * @param source
   *          the module to generate noise from.
   * @param xDisplacer
   *          the module displacing the X axis.
   * @param yDisplacer
   *          the module displacing the Y axis.
   * @param zDisplacer
   *          the module displacing the Z axis.
   */
  public Displace (Module source, Module xDisplacer, Module yDisplacer,
      Module zDisplacer) {
    super (source);

    assert (xDisplacer != null);
    assert (yDisplacer != null);
    assert (zDisplacer != null);

    this.xDisplacer = xDisplacer;
    this.yDisplacer = yDisplacer;
    this.zDisplacer = zDisplacer;
  }

  /**
   * Compares this displacer module for equality with another object. This
   * module is equal to other Displace modules fed by an equal source and
   * displacement modules.
   * 
   * @param object
   *          the object to compare.
   * @return <code>true</code> if <var>object</var> is an equal module.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object object) {
    if (object == null)
      return false;
    else if (!(object instanceof Displace))
      return false;

    Displace other = (Displace) object;
    return getSource ().equals (other.getSource ())
        && xDisplacer.equals (other.getXDisplacer ())
        && yDisplacer.equals (other.getYDisplacer ())
        && zDisplacer.equals (other.getZDisplacer ());
  }

  /**
   * Displaces the location and generates a noise value from the source module.
   * 
   * @param x
   *          the X coordinate of the location to displace.
   * @param y
   *          the Y coordinate of the location to displace.
   * @param z
   *          the Z coordinate of the location to displace.
   * @return the noise value at thie displaced location.
   * @see jnoise.modules.Module#getValue(double, double, double)
   */
  public double getValue (double x, double y, double z) {
    double displacedX = x + xDisplacer.getValue (x, y, z);
    double displacedY = y + yDisplacer.getValue (x, y, z);
    double displacedZ = z + zDisplacer.getValue (x, y, z);

    return getSource ().getValue (displacedX, displacedY, displacedZ);
  }

  /**
   * Returns the module used to displace the X axis.
   * 
   * @return the X axis displacement module.
   */
  public Module getXDisplacer () {
    return xDisplacer;
  }

  /**
   * Returns the module used to displace the Y axis.
   * 
   * @return the Y axis displacement module.
   */
  public Module getYDisplacer () {
    return yDisplacer;
  }

  /**
   * Returns the module used to displace the Z axis.
   * 
   * @return the Z axis displacement module.
   */
  public Module getZDisplacer () {
    return zDisplacer;
  }

  /**
   * Generates a hashcode for this displacer module.
   * 
   * @return the module's hashcode.
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return getSource ().hashCode () ^ xDisplacer.hashCode ()
        ^ yDisplacer.hashCode () ^ zDisplacer.hashCode ();
  }

  private final Module xDisplacer;
  private final Module yDisplacer;
  private final Module zDisplacer;
}
