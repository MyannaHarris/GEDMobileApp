package com.gedappgui.gedappgui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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

    @Mock
    Application testApp;

    @Mock
    SQLiteDatabase database;

    File file;

    @Before
    public void init() {
        mainActivity = org.mockito.Mockito.mock(MainActivity.class);
        String path = "/data/data/com.gedappgui.gedappgui/files";
        testApp = new Application();

        when(mainActivity.getApplication()).thenReturn(testApp);
        //when(testApp.getFilesDir()).thenReturn(new File(path));

        file = new File(((Activity) this.mainActivity).getApplication().getFilesDir(),
                "GEDPrep.db");
    }

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
    public void testHandleIntent_HomeToContinueLesson_works() throws InterruptedException {
        try {
            mainActivity.gotToContinueLesson(view);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            fail("Waiting for the worker thread took too long.");
        }
    }*/

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
    public void testDatabaseDoesNotExist() {
        DatabaseHelper db = new DatabaseHelper(database, file);

        assertFalse(db.checkDatabase());
    }

    @Test
    public void testDatabaseExists() {
        database = SQLiteDatabase.openDatabase(
                file.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);

        DatabaseHelper db = new DatabaseHelper(database, file);

        assertTrue(db.checkDatabase());
    }

    @Test
    public void testDatabaseHasAchievementWithID_1() {
        database = SQLiteDatabase.openDatabase(
                file.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);

        DatabaseHelper db = new DatabaseHelper(database, file);

        assertTrue(db.achievementExists(1));
    }
}