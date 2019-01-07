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
 * Copyright (C) 2006 Owen Jacobson <angrybaldguy@gmail.com>
 */
package ca.grimoire.jnoise.models;

import ca.grimoire.jnoise.modules.Module;
import ca.grimoire.jnoise.modules.basic.Integer;
import junit.framework.TestCase;

/**
 * Test cases for the SphereModel coordinate mapping tool.
 */
public class SphereModelTest extends TestCase {
    private static final double EPSILON = 1e-10;
    private static final int SEED = 523;
    private static final Module MODULE = new Integer( SEED );
    private static final SphereModel MODEL = new SphereModel( MODULE );

    /**
     * Verifies that the model returns the correct noise value at 0 latitude.
     */
    public void testNorthPole() {
        assertEquals( MODULE.getValue( 0, 1, 0 ), MODEL.getValue( 0, 0 ),
            EPSILON );
    }

    /**
     * Verifies that the model returns the correct noise value at pi/2 latitude,
     * 0 longitude.
     */
    public void testXwardFace() {
        assertEquals( MODULE.getValue( 1, 0, 0 ), MODEL.getValue( Math.PI / 2,
            0 ), EPSILON );
    }

    /**
     * Verifies that the model returns the correct noise value at pi/2 latitude,
     * pi/2 longitude.
     */
    public void testZwardFace() {
        assertEquals( MODULE.getValue( 0, 0, 1 ), MODEL.getValue( Math.PI / 2,
            Math.PI / 2 ), EPSILON );
    }

    /**
     * Verifies that the model returns the correct noise value at pi latitude.
     */
    public void testSouthPole() {
        assertEquals( MODULE.getValue( 0, -1, 0 ),
            MODEL.getValue( Math.PI, 0 ), EPSILON );
    }
}
