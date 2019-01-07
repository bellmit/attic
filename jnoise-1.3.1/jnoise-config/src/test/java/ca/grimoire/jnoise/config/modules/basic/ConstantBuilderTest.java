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

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Constant;
import junit.framework.TestCase;

/**
 * Test cases for verifying the behaviour of the Constant noise module builder.
 */
public final class ConstantBuilderTest extends TestCase {

  private static final double CONSTANT_VALUE_ONE = 47.25;
  private static final double CONSTANT_VALUE_TWO = 1.2345e-3;
  private static final String VALUE_PROP_NAME    = "value";

  /**
   * Tests a fresh ConstantBuilder constructed with a value and testing the
   * resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderConstructedWithValue () throws BuilderException {
    ConstantBuilder builder = new ConstantBuilder (CONSTANT_VALUE_ONE);

    Constant module = builder.createModule ();
    assertEquals (CONSTANT_VALUE_ONE, module.getValue ());
  }

  /**
   * Tests a fresh ConstantBuilder by providing it with two value and testing
   * the resulting noise module. The resulting module should have the second
   * value as its noise value.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithMultipleValues () throws BuilderException {
    ConstantBuilder builder = new ConstantBuilder ();
    builder.setValue (CONSTANT_VALUE_ONE);
    builder.setValue (CONSTANT_VALUE_TWO);
    Constant module = builder.createModule ();
    assertEquals (CONSTANT_VALUE_TWO, module.getValue ());
  }

  /**
   * Tests a fresh ConstantBuilder by providing it with no values and checking
   * that it throws an exception.
   */
  public void testBuilderWithNoValues () {
    ModuleBuilder builder = new ConstantBuilder ();
    try {
      builder.createModule ();
      fail ();
    } catch (BuilderException be) {
      // Success case.
    }
  }

  /**
   * Tests a fresh ConstantBuilder by providing it with a value and testing the
   * resulting noise module.
   * 
   * @throws BuilderException
   *           if the test fails due to a builder configuration exception.
   */
  public void testBuilderWithValue () throws BuilderException {
    ConstantBuilder builder = new ConstantBuilder ();
    builder.setValue (CONSTANT_VALUE_ONE);
    Constant module = builder.createModule ();
    assertEquals (CONSTANT_VALUE_ONE, module.getValue ());
  }

  /**
   * Point of interest test that verifies that the "value" property is exposed
   * through the JavaBeans API.
   * 
   * @throws IllegalArgumentException
   *           if the value property mutator method does not accept Doubles.
   * @throws IllegalAccessException
   *           if the value property mutator is not accessible.
   * @throws InvocationTargetException
   *           if the value property mutator throws an exception.
   * @throws IntrospectionException
   *           if the JavaBeans introspection API fails to analyze the
   *           ConstantBuilder class.
   * @throws BuilderException
   *           if the builder is not in a valid state to create a Constant
   *           module after having the value property set.
   */
  public void testSetValueByIntrospection () throws IllegalArgumentException,
      IllegalAccessException, InvocationTargetException,
      IntrospectionException, BuilderException {
    BeanInfo info = Introspector.getBeanInfo (ConstantBuilder.class);
    PropertyDescriptor valueProp = null;
    for (PropertyDescriptor property : info.getPropertyDescriptors ())
      if (VALUE_PROP_NAME.equals (property.getName ()))
        valueProp = property;

    assert (valueProp != null);
    Method setValueMethod = valueProp.getWriteMethod ();

    ConstantBuilder builder = new ConstantBuilder ();
    setValueMethod.invoke (builder, CONSTANT_VALUE_ONE);

    Constant module = builder.createModule ();
    assertEquals (CONSTANT_VALUE_ONE, module.getValue ());
  }
}
