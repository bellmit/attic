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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates beans based on a name. Bean classes may be registered to a name and
 * later used to create new module builders under that name.
 * 
 * @param <T>
 *          the base class or interface for beans in this registry.
 */
public final class BeanRegistry<T> {
  private static <T> T createBuilder (Constructor<? extends T> constructor)
      throws InvalidBeanException {
    assert (constructor != null);

    try {
      return constructor.newInstance ();
    } catch (InstantiationException e) {
      throw new InvalidBeanException (e);
    } catch (IllegalAccessException e) {
      throw new InvalidBeanException (e);
    } catch (InvocationTargetException e) {
      // "Caused" by the original exception, not the reflective coating.
      throw new InvalidBeanException (e.getCause ());
    }
  }

  private static <T> Constructor<T> findDefaultConstructor (
      Class<T> builderClass) throws InvalidBeanException {
    assert (builderClass != null);

    try {
      Constructor<T> constructor = builderClass.getConstructor ();
      return constructor;
    } catch (SecurityException e) {
      throw new InvalidBeanException (e);
    } catch (NoSuchMethodException e) {
      throw new InvalidBeanException (e);
    }
  }

  /**
   * Creates a new bean based on the passed name. This returns a new instance of
   * the bean class registered for <var>name</var>, if one exists.
   * 
   * @param name
   *          the name of the bean calss to create.
   * @return the created bean builder.
   * @throws UnknownBeanException
   *           if no bean class was registered for <var>name</var>.
   * @throws InvalidBeanException
   *           if a bean class exists for the registered name but does not
   *           conform to the contract for bean classes.
   */
  public T create (String name) throws UnknownBeanException,
      InvalidBeanException {
    assert (name != null);

    Class<? extends T> builderClass = registry.get (name);
    if (builderClass == null)
      throw new UnknownBeanException (name);

    Constructor<? extends T> constructor = findDefaultConstructor (builderClass);
    return createBuilder (constructor);
  }

  /**
   * Registers a class as a module builder. The class must provide a no-argument
   * constructor which throws no exceptions and is visible to the registry,
   * which will be used to create new builder instances.
   * 
   * @param name
   *          the name to register the builder under.
   * @param builderClass
   *          the builder class to register.
   */
  public void registerBuilder (String name, Class<? extends T> builderClass) {
    assert (name != null);
    assert (builderClass != null);

    // TODO something civilised with already-registered builders.
    registry.put (name, builderClass);
  }

  private final Map<String, Class<? extends T>> registry = new HashMap<String, Class<? extends T>> ();
}
