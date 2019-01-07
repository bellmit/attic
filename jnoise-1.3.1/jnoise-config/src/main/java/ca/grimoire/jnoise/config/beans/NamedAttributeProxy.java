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

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.grimoire.jnoise.config.conversion.DefaultTypeStrategy;
import ca.grimoire.jnoise.config.conversion.TypeConversionStrategy;

/**
 * A proxy object which exposes a JavaBean's attributes by name.
 */
public final class NamedAttributeProxy {

  private static final TypeConversionStrategy DEFAULT_TYPE_STRATEGY = new DefaultTypeStrategy ();

  private static BeanInfo getBeanInfo (Object bean) throws InvalidBeanException {
    assert (bean != null);

    try {
      return Introspector.getBeanInfo (bean.getClass ());
    } catch (IntrospectionException ie) {
      throw new InvalidBeanException (ie);
    }
  }

  private static PropertyDescriptor getPropertyDescriptor (BeanInfo info,
      String property) throws UnknownPropertyException {
    for (PropertyDescriptor descriptor : info.getPropertyDescriptors ())
      if (property.equals (descriptor.getName ()))
        return descriptor;

    throw new UnknownPropertyException (property);
  }

  private static Class<?> getPropertyType (Method setMethod)
      throws UnknownPropertyException {
    assert (setMethod != null);

    Class<?>[] args = setMethod.getParameterTypes ();
    if (args.length != 1)
      throw new UnknownPropertyException ();

    return args[0];
  }

  /**
   * Creates a new proxy for a given JavaBean. The names of exposed properties
   * are defined by the JavaBeans specification and the public <code>get</code>/<code>is</code>/<code>set</code>
   * methods on the wrapped bean.
   * 
   * @param bean
   *          the bean to proxy for.
   * @throws InvalidBeanException
   *           if <var>bean</var> is not a valid JavaBean.
   */
  public NamedAttributeProxy (Object bean) throws InvalidBeanException {
    this (bean, DEFAULT_TYPE_STRATEGY);
  }

  /**
   * Create a new proxy using a custom type conversion strategy. The
   * {@link DefaultTypeStrategy default type conversion strategy} can convert
   * from Strings to primitives, wrappers, enums, and String-constructable
   * objects.
   * 
   * @param bean
   *          the bean to proxy for.
   * @param converter
   *          the type conversion strategy to use.
   * @throws InvalidBeanException
   *           if <var>bean</var> is not a valid JavaBean.
   */
  public NamedAttributeProxy (Object bean, TypeConversionStrategy converter)
      throws InvalidBeanException {
    assert (bean != null);
    assert (converter != null);

    this.bean = bean;
    this.beanInfo = getBeanInfo (bean);

    this.converter = converter;
  }

  /**
   * Set a named property on the bean. The property names are automatically
   * determined by the underlying bean's methods, as with the JavaBeans API.
   * 
   * @param property
   *          the name of the proeprty to set.
   * @param value
   *          a string representation of the value to set the property to.
   * @throws BeanException
   *           if the property doesn't exist or is unwritable.
   * @throws Exception
   *           if the underlying bean rejects the property change.
   */
  public void setProperty (String property, String value) throws Exception {
    assert (property != null);

    Method setValueMethod = getPropertySetter (property);
    Class<?> propertyType = getPropertyType (setValueMethod);
    Object propValue = coerceValue (value, propertyType);

    try {
      setValueMethod.invoke (bean, propValue);
    } catch (IllegalAccessException iae) {
      throw new PropertyUnwritableException (iae);
    } catch (InvocationTargetException ite) {
      // Yig. The underlying setter threw an exception. What to do, what to
      // do... Unwrap it! No reason our caller should care about
      // introspection/reflection stuff, that's the whole point of this class.
      // Unfortunately this means we need throws Exception to allow wrapped
      // beans to throw domain-specific exceptions with impunity.
      Throwable cause = ite.getCause ();
      if (cause instanceof RuntimeException)
        throw (RuntimeException) cause;
      if (cause instanceof Exception)
        throw (Exception) cause;
      // else, must be an Error.
      assert (cause instanceof Error);
      throw (Error) cause;
    }
  }

  private Object coerceValue (String value, Class<?> propertyType)
      throws UnknownPropertyException {
    try {
      return converter.convert (propertyType, value);
    } catch (IllegalArgumentException e) {
      throw new UnknownPropertyException ("Cannot convert " + value + " to "
          + propertyType, e);
    }
  }

  private Method getPropertySetter (String property)
      throws UnknownPropertyException, PropertyUnwritableException {
    assert (property != null);

    PropertyDescriptor valueProp = getPropertyDescriptor (beanInfo, property);

    Method setter = valueProp.getWriteMethod ();
    if (setter == null)
      throw new PropertyUnwritableException (property);

    return setter;
  }

  private final Object                 bean;
  private final BeanInfo               beanInfo;
  private final TypeConversionStrategy converter;
}
