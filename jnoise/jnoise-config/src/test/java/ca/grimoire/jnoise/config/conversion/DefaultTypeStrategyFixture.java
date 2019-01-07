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
package ca.grimoire.jnoise.config.conversion;

import junit.framework.TestCase;

/**
 * Test fixture providing a default type strategy to other tests.
 */
public abstract class DefaultTypeStrategyFixture extends TestCase {

  /**
   * Initializes the test case with a DefaultTypeStrategy.
   * 
   * @throws Exception
   *           if the base class setup fails.
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp () throws Exception {
    super.setUp ();

    testStrategy = new DefaultTypeStrategy ();
  }

  /** A type strategy instance to test, initialized during setup. */
  protected DefaultTypeStrategy testStrategy;

}
