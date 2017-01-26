package com.gedappgui.gedappgui;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jasminejans on 11/17/16.
 */
public class MyApplicationTest {
    @Test
    public void getName() throws Exception {
        MyApplication app = new MyApplication();
        assertTrue(app.getName().equals("Jasmine"));
    }

    @Test
    public void getLoginStatus() throws Exception {
        assertFalse(MyApplication.getLoginStatus());
    }

}