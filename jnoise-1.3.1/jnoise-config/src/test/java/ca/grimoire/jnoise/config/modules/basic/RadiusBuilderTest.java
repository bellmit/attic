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

import ca.grimoire.jnoise.modules.basic.Radius;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Radius noise module builder.
 */
public final class RadiusBuilderTest extends TestCase {

  /**
   * Compares a fresh RadiusBuilder's product against the Radius.MODULE singleton
   * module.
   */
  public void testAgainstSingletonInstance () {
    RadiusBuilder builder = new RadiusBuilder ();

    Radius module = builder.createModule ();
    assertEquals (Radius.MODULE, module);
  }

  /**
   * Compares a fresh RadiusBuilder's product against a new, clean Radius module.
   */
  public void testAgainstNewInstance () {
    RadiusBuilder builder = new RadiusBuilder ();

    Radius module = builder.createModule ();
    assertEquals (new Radius (), module);
  }
}
