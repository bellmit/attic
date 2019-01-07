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

import ca.grimoire.jnoise.modules.basic.Constant;
import junit.framework.TestCase;

/**
 * Test cases for validating the behaviour of a class which exposes builder
 * bean-style primitive properties by name.
 */
public final class NamedAttributeProxyPrimitivesTest extends TestCase {

  /**
   * A simple module builder with attributes of primitive types.
   */
  private static final class TestBuilder {

    /** The name of the boolean property. */
    public static final String BOOL_PROPERTY   = "boolValue";
    /** The name of the byte property. */
    public static final String BYTE_PROPERTY   = "byteValue";
    /** The name of the char property. */
    public static final String CHAR_PROPERTY   = "charValue";
    /** The name of the double property. */
    public static final String DOUBLE_PROPERTY = "doubleValue";
    /** The name of the float property. */
    public static final String FLOAT_PROPERTY  = "floatValue";
    /** The name of the int property. */
    public static final String INT_PROPERTY    = "intValue";
    /** The name of the long property. */
    public static final String LONG_PROPERTY   = "longValue";
    /** The name of the short property. */
    public static final String SHORT_PROPERTY  = "shortValue";

    /**
     * Makes the constructor visible to the test.
     */
    public TestBuilder () {
      // Be accessible.
    }

    /**
     * Creates a new Constant module based on the double property.
     * 
     * @return a constant module.
     */
    public Constant createModule () {
      return new Constant (getDoubleValue ());
    }

    /**
     * Get half of the byte property.
     * 
     * @return the byteValue property.
     */
    public byte getByteValue () {
      return byteValue;
    }

    /**
     * Get half of the char property.
     * 
     * @return the charValue property.
     */
    public char getCharValue () {
      return charValue;
    }

    /**
     * Get half of the double property.
     * 
     * @return the doubleValue property.
     */
    public double getDoubleValue () {
      return doubleValue;
    }

    /**
     * Get half of the float property.
     * 
     * @return the floatValue property.
     */
    public float getFloatValue () {
      return floatValue;
    }

    /**
     * Get half of the int property.
     * 
     * @return the intValue property.
     */
    public int getIntValue () {
      return intValue;
    }

    /**
     * Get half of the long property.
     * 
     * @return the longValue property.
     */
    public long getLongValue () {
      return longValue;
    }

    /**
     * Get half of the short property.
     * 
     * @return the shortValue property.
     */
    public short getShortValue () {
      return shortValue;
    }

    /**
     * Get half of the boolean property.
     * 
     * @return the boolValue property.
     */
    public boolean isBoolValue () {
      return boolValue;
    }

    /**
     * Set half of the boolean property.
     * 
     * @param boolValue
     *          the new property value.
     */
    public void setBoolValue (boolean boolValue) {
      this.boolValue = boolValue;
    }

    /**
     * Set half of the byte property.
     * 
     * @param byteValue
     *          the new property value.
     */
    public void setByteValue (byte byteValue) {
      this.byteValue = byteValue;
    }

    /**
     * Set half of the char property.
     * 
     * @param charValue
     *          the new property value.
     */
    public void setCharValue (char charValue) {
      this.charValue = charValue;
    }

    /**
     * Set half of the double property.
     * 
     * @param doubleValue
     *          the new property value.
     */
    public void setDoubleValue (double doubleValue) {
      this.doubleValue = doubleValue;
    }

    /**
     * Set half of the float property.
     * 
     * @param floatValue
     *          the new property value.
     */
    public void setFloatValue (float floatValue) {
      this.floatValue = floatValue;
    }

    /**
     * Set half of the int property.
     * 
     * @param intValue
     *          the new property value.
     */
    public void setIntValue (int intValue) {
      this.intValue = intValue;
    }

    /**
     * Set half of the long property.
     * 
     * @param longValue
     *          the new property value.
     */
    public void setLongValue (long longValue) {
      this.longValue = longValue;
    }

    /**
     * Set half of the short property.
     * 
     * @param shortValue
     *          the new property value.
     */
    public void setShortValue (short shortValue) {
      this.shortValue = shortValue;
    }

    private boolean boolValue   = false;
    private byte    byteValue   = 0;
    private char    charValue   = ' ';
    private double  doubleValue = 0.0;
    private float   floatValue  = 0.0f;
    private int     intValue    = 0;
    private long    longValue   = 0;
    private short   shortValue  = 0;
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts boolean values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testBoolAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.BOOL_PROPERTY, "true");
    assertEquals (true, realBuilder.isBoolValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts byte values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testByteAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.BYTE_PROPERTY, "1");
    assertEquals (1, realBuilder.getByteValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts char values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testCharAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.CHAR_PROPERTY, "c");
    assertEquals ('c', realBuilder.getCharValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts double values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testDoubleAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.DOUBLE_PROPERTY, "0.45");
    assertEquals (0.45, realBuilder.getDoubleValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts float values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testFloatAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.FLOAT_PROPERTY, "-2e5");
    assertEquals (-2e5f, realBuilder.getFloatValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts int values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testIntAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.INT_PROPERTY, "200");
    assertEquals (200, realBuilder.getIntValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts long values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testLongAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.LONG_PROPERTY, "-50241500");
    assertEquals (-50241500, realBuilder.getLongValue ());
  }

  /**
   * Tests that a NamedAttributeProxyBuilder correctly converts short values.
   * 
   * @throws Exception
   *           if the property setter throws an exception.
   */
  public void testShortAttribute () throws Exception {
    testProxy.setProperty (TestBuilder.SHORT_PROPERTY, "2");
    assertEquals (2, realBuilder.getShortValue ());
  }

  /**
   * Sets up the test case by creating a new, blank TestBuilder and
   * NamedAttributeProxyBuilder.
   * 
   * @throws Exception
   *           if the base test case's setup method fails.
   * @see TestCase#setUp()
   */
  @Override
  protected void setUp () throws Exception {
    super.setUp ();

    realBuilder = new TestBuilder ();
    testProxy = new NamedAttributeProxy (realBuilder);
  }

  private TestBuilder         realBuilder;
  private NamedAttributeProxy testProxy;
}
