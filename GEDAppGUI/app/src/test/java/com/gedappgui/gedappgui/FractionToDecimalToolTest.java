/*
 * FractionToDecimalToolTest.java
 *
 * FractionToDecimal ToolTests
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring
 *
 * This file is part of DragonAcademy.
 *
 * DragonAcademy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License. All redistributions
 * of the app or modifications of the app are to remain free in accordance
 * with the GNU General Public License.
 *
 * DragonAcademy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DragonAcademy.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.gedappgui.gedappgui;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by James on 11/29/2016.
 */
public class FractionToDecimalToolTest {
    @Test
    public void Decimaltofraction2088() throws Exception {
        assertTrue((FractionToDecimalTool.evaluateDecimal("2.088").equals("261/125")));
    }

    @Test
    public void gcd3228() throws Exception {
        assertTrue((FractionToDecimalTool.gcd_convert(32,28) == 4));
    }

    @Test
    public void divideby0output() throws Exception {
        assertTrue(FractionToDecimalTool.evaluateFraction("11/0").equals("Invalid input, cannot divide by zero"));
    }
    @Test
    public void fractiontodecimal12598() throws Exception {
        assertTrue(FractionToDecimalTool.evaluateFraction("125/98").equals("1.2755102040816326"));
    }
    @Test
    public void Badoubleconversion() throws Exception {
        assertTrue(FractionToDecimalTool.evaluateFraction("123/a4112").equals("Invalid input, Example inputs: 1/2, 13/7"));
    }

}