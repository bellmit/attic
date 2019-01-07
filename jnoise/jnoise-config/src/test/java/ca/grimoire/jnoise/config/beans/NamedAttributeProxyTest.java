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

import junit.framework.TestCase;

/**
 * Test cases for validating the behaviour of a class which exposes builder
 * bean-style properties by name.
 */
public final class NamedAttributeProxyTest extends TestCase {

  /**
   * A simple module builder with accessible and inaccessible attributes, which
   * constructs Constant modules.
   */
  private static final class TestBean {
    /** A name of a nonexistant attribute. */
    public static final String BOGUS_NAME             = "unvalue";

    /** The name of a non-exposed (private) attribute. */
    public static final String PRIVATE_ATTRIBUTE_NAME = "name";

    /** The name of the exposed attribute. */
    public static final String PUBLIC_ATTRIBUTE_NAME  = "value";

    /**
     * Makes the constructor visible to the test.
     */
    public TestBean () {
      // Be accessible.
    }

    /**
     * Returns the current setting of the "value" attribute.
     * 
     * @return the value used for modules.
     */
    public double getValue () {
      return value;
    }

    /**
     * Sets the "value" attribute used for subsequently-created Constant
     * modules.
     * 
     * @param value
     *          the value to use for subsequent modules.
     */
    public void setValue (double value) {
      this.value = value;
      setName (Double.toString (value));
    }

    private void setName (String name) {
      assert (name != null);
    }

    private double value = 0.0;
  }

  /**
   * Verifies that a proxy module builder creates equivalent modules to the
   * wrapped builder.
   * 
   * @throws Exception
   *           if the property setter throws an unexpected exception.
   */
  public void testCreatesModule () throws Exception {
    TestBean realBuilder = new TestBean ();
    NamedAttributeProxy proxy = new NamedAttributeProxy (realBuilder);

    proxy.setProperty (TestBean.PUBLIC_ATTRIBUTE_NAME, "0.54");

    assertEquals (0.54, realBuilder.getValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder forwards property changes to the
   * underlying builder.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testSetExistingAttribute () throws Exception {
    TestBean realBuilder = new TestBean ();
    NamedAttributeProxy proxy = new NamedAttributeProxy (realBuilder);

    proxy.setProperty (TestBean.PUBLIC_ATTRIBUTE_NAME, "0.45");

    assertEquals (0.45, realBuilder.getValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder fails to set a property given an
   * inconvertible value.
   * 
   * @throws Exception
   *           if the property setter throws an unexpected exception.
   */
  public void testSetInconvertibleAttribute () throws Exception {
    TestBean realBuilder = new TestBean ();
    NamedAttributeProxy proxy = new NamedAttributeProxy (realBuilder);

    try {
      proxy.setProperty (TestBean.PUBLIC_ATTRIBUTE_NAME, "not a double");
      fail ();
    } catch (UnknownPropertyException nbpe) {
      // Success case
    }
  }

  /**
   * Tests that a NamedAttributeProxyBuilder throws an exception when asked to
   * set a nonexistant property.
   * 
   * @throws Exception
   *           if the property setter throws an unexpected exception.
   */
  public void testSetNonexistantAttribute () throws Exception {
    TestBean realBuilder = new TestBean ();
    NamedAttributeProxy proxy = new NamedAttributeProxy (realBuilder);

    try {
      proxy.setProperty (TestBean.BOGUS_NAME, "a value");
      fail ();
    } catch (UnknownPropertyException nbpe) {
      // Success case
    }
  }

  /**
   * Tests that a NamedAttributeProxyBuilder throws an exception when asked to
   * set an inaccessible property.
   * 
   * @throws Exception
   *           if the property setter throws an unexpected exception.
   */
  public void testSetPrivateAttribute () throws Exception {
    TestBean realBuilder = new TestBean ();
    NamedAttributeProxy proxy = new NamedAttributeProxy (realBuilder);

    try {
      proxy.setProperty (TestBean.PRIVATE_ATTRIBUTE_NAME, "a name");
      fail ();
    } catch (UnknownPropertyException upe) {
      // Success case
    }
  }
}
