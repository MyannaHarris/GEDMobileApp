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