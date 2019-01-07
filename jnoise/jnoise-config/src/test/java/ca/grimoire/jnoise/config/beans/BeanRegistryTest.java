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
package ca.grimoire.jnoise.config.beans;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Tests for the bean factory registry class.
 */
public final class BeanRegistryTest extends TestCase {

  private static final class ExceptionalBean {
    /**
     * Always throws a runtime exception.
     * 
     * @throws RuntimeException
     *           when invoked.
     */
    public ExceptionalBean () throws RuntimeException {
      throw new RuntimeException ("constructor exception");
    }
  }

  /**
   * Tests that calls to <code>create</code> create distinct bean instances.
   * Registers a bean type, then uses that type to create a large number of
   * beans and checks that no two are the same bean.
   * 
   * @throws UnknownBeanException
   *           if the test fails because of registration problems.
   * @throws InvalidBeanException
   *           if the test fails because of problems creating the test bean.
   */
  public void testCreatesDistinctBuilders () throws UnknownBeanException,
      InvalidBeanException {
    BeanRegistry<Object> registry = new BeanRegistry<Object> ();
    registry.registerBuilder ("constant", Object.class);

    Set<Object> created = new HashSet<Object> ();
    for (int i = 0; i < 100; ++i) {
      Object product = registry.create ("constant");

      assertFalse (created.contains (product));
      created.add (product);
    }
  }

  /**
   * Tests that registering a bean which violates the expectations of bean
   * classes causes the correct exception.
   * 
   * @throws UnknownBeanException
   *           if the registry is unable to locate the registered bean class.
   */
  public void testThrowsOnExceptionalBuilderConstructor ()
      throws UnknownBeanException {
    BeanRegistry<Object> registry = new BeanRegistry<Object> ();

    try {
      registry.registerBuilder ("broken", ExceptionalBean.class);

      registry.create ("broken");
      fail ();
    } catch (InvalidBeanException ibe) {
      // Success case.
    }
  }

  /**
   * Tests that attempting to create a bean using an unregistered name throws an
   * exception.
   * 
   * @throws InvalidBeanException
   *           if an invalid builder is found instead of no builder.
   */
  public void testThrowsOnUnregistered () throws InvalidBeanException {
    BeanRegistry registry = new BeanRegistry ();

    try {
      registry.create ("unregistered");
      fail ();
    } catch (UnknownBeanException nbe) {
      // Success case.
    }
  }
}
