package com.gedappgui.gedappgui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Context mMockContext;

    @Mock
    Activity mMockActivity;

    @Mock
    MainActivity mainActivity;

    @Mock
    Sprite sprite;

    @Mock
    LearnConcepts learnConcepts;

    @Mock
    Play play;

    @Mock
    Achievements achievements;

    @Mock
    Tools tools;

    @Mock
    LessonSummary lessonSummary;

    @Mock
    LessonSteps lessonSteps;

    @Mock
    View view;

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

    /*@Test
    public void testHandleIntent_HomeToSprite_works() throws InterruptedException {
        try {
            mainActivity.gotToSprite(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }

    @Test
    public void testHandleIntent_HomeToLearnConcepts_works() throws InterruptedException {
        try {
            mainActivity.gotToLearn(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }

    @Test
    public void testHandleIntent_HomeToPlay_works() throws InterruptedException {
        try {
            mainActivity.gotToPlay(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }

    @Test
    public void testHandleIntent_HomeToAchievements_works() throws InterruptedException {
        try {
            mainActivity.gotToAchievements(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }

    @Test
    public void testHandleIntent_HomeToTools_works() throws InterruptedException {
        try {
            mainActivity.gotToTools(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }

    @Test
    public void testHandleIntent_HomeToContinueLesson_works() throws InterruptedException {
        try {
            mainActivity.gotToContinueLesson(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }*/

    @Test
    public void testDatabaseExists() throws InterruptedException {
        DatabaseHelper db = new DatabaseHelper(mMockActivity);

        assertTrue(db.checkDatabase());
    }
}