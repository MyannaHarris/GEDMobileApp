/*
 * ExampleUnitTest.java
 *
 * Example UnitTests
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