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

import ca.grimoire.jnoise.config.BuilderException;
import ca.grimoire.jnoise.config.ChildlessElement;
import ca.grimoire.jnoise.config.modules.ModuleBuilder;
import ca.grimoire.jnoise.modules.basic.Constant;

/**
 * A builder capable of creating Constant noise modules. Constant noise modules
 * require a single value (the noise value), which can be configured through
 * setValue.
 * 
 * @see Constant
 */
public final class ConstantBuilder extends ChildlessElement implements
    ModuleBuilder {

  /**
   * Creates a new ConstantBuilder with no value set. The value for the created
   * modules must be passed to {@link #setValue(double)} before calling
   * {@link #createModule()}.
   */
  public ConstantBuilder () {

  }

  /**
   * Creates a new ConstantBuilder with a value set.
   * 
   * @param value
   *          the initial value for the module.
   */
  public ConstantBuilder (double value) {
    setValue (value);
  }

  /**
   * Generates a Constant noise module using the configured noise value. If no
   * value has been configured yet this throws a BuilderException instead.
   * 
   * @return a Constant noise module.
   * @throws BuilderException
   *           if no noise value has been configured.
   * @see #setValue(double)
   * @see ModuleBuilder#createModule()
   */
  public Constant createModule () throws BuilderException {
    if (!valueSet)
      throw new BuilderException ("No noise value for constant module.");

    return new Constant (value);
  }

  /**
   * Changes the noise value used to configure subsequently-created modules.
   * 
   * @param value
   *          the noise value to use in subsequent modules.
   */
  public void setValue (double value) {
    this.value = value;
    this.valueSet = true;
  }

  private double  value;
  private boolean valueSet = false;
}
