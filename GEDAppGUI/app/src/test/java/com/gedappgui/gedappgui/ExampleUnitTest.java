package com.gedappgui.gedappgui;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void username_isCorrect() throws Exception {
        MyApplication myApp = new MyApplication();
        myApp.setName("TestName");
        String test = myApp.getName();
        assertEquals(test, "TestName");
    }

    @Test
    public void username_isCorrect2() throws Exception {
        MyApplication myApp = new MyApplication();
        myApp.setName("testIncorrect");
        String test = myApp.getName();
        assertEquals(test, "testIncorrect");
    }
}