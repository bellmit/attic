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
 package ca.grimoire.jnoise.config.modules.basic;

import ca.grimoire.jnoise.modules.basic.Plane;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Plane noise module builder.
 */
public final class PlaneBuilderTest extends TestCase {

  /**
   * Compares a fresh PlaneBuilder's product against the Plane.MODULE singleton
   * module.
   */
  public void testAgainstSingletonInstance () {
    PlaneBuilder builder = new PlaneBuilder ();

    Plane module = builder.createModule ();
    assertEquals (Plane.MODULE, module);
  }

  /**
   * Compares a fresh PlaneBuilder's product against a new, clean Plane module.
   */
  public void testAgainstNewInstance () {
    PlaneBuilder builder = new PlaneBuilder ();

    Plane module = builder.createModule ();
    assertEquals (new Plane (), module);
  }
}
