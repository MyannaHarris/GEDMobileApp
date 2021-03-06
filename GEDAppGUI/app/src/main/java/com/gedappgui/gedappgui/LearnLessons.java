/*
 * LearnLessons.java
 *
 * Learn Lessons page activity
 *
 * Gives a "trail" of math lessons for the chosen concept that a student can choose to start
 * Each lesson has multiple parts and questions that the student must pass
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

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LearnLessons extends AppCompatActivity {

    // Gridlayout to put items in
    private GridLayout gridlayout;
    // Database helper
    private DatabaseHelper dbHelper;
    // Intent info
    private int conceptID;
    private int lessonID;

    // Completed lesson dialog
    private AlertDialog.Builder lessonDialog;
    // Adapter to fill dialog
    private AlertTextViewAdapter optionAdapter;

    // Intent to move on to next page
    private Intent activityChangeIntent;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_learn_lessons);
        dbHelper = new DatabaseHelper(this);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridlayout = (GridLayout) findViewById(R.id.lessons_gridView);

        ArrayList<String> lessonNames = dbHelper.selectLessons(conceptID);

        // Instantiate an AlertDialog.Builder with its constructor
        lessonDialog = new AlertDialog.Builder(this, R.style.AlertDialogAppearance);

        // Get dimensions of screen to make text size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        // Fill option adapter
        optionAdapter = new AlertTextViewAdapter(LearnLessons.this,
                new String[]
                        {"Summary - A brief intro",
                                "Tips - Further explanation",
                                "Examples - Solved problems",
                                "Game - Practice game",
                                "Questions - Practice problems"}, height);

        // Put things in the gridlayout
        setGridInfo(lessonNames);
    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Goes to concepts
     */
    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent(this, LearnConcepts.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentBack);
    }

    /**
     * Hides bottom navigation bar
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        ArrayList<String> lessonNames = dbHelper.selectLessons(conceptID);
        // Put things in the gridlayout
        setGridInfo(lessonNames);
    }

    /**
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
     * @param hasFocus true or false based on if the focus of the window changes to this activity
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    /**
     * Sets what menu will be in the action bar
     * @param menu The options menu in which we place the items.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /**
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
     * settings = goes to settings page
     * android.R.id.home = go to the activity that called the current activity
     * @param item that is selected from the menu in the action bar
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intentBack = new Intent(this, LearnConcepts.class);
                intentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentBack);
                return true;
            // action with ID action_refresh was selected
            case R.id.action_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Dynamically adds views to the GridLayout - one row per lesson
     * Each lesson has an ImageView, holding the image to create the "path"
     *   and a TextView, holding the lesson name; the order of the views is
     *   decided by whether the row is even or not
     * Calls createLessonName and createLessonImg to actually make the views
     * @param titles each lesson that should be on the page
     */
    public void setGridInfo(ArrayList titles) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels/2;

        // Add each lesson in the list
        int totalConcepts = titles.size();
        for (int row = 0; row < totalConcepts; row++) {

            // Set up layout parameters for items to be inserted into the Grid Layout
            GridLayout.Spec thisRow = GridLayout.spec(row, 1);
            GridLayout.Spec col0 = GridLayout.spec(0, 1);
            GridLayout.Spec col1 = GridLayout.spec(1, 1);
            GridLayout.LayoutParams gridLayoutParam0 = new GridLayout.LayoutParams(thisRow, col0);
            GridLayout.LayoutParams gridLayoutParam1 = new GridLayout.LayoutParams(thisRow, col1);
            int viewGravity = Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL;
            gridLayoutParam0.setGravity(viewGravity);
            gridLayoutParam1.setGravity(viewGravity);
            String title = titles.get(row).toString();
            TextView conceptName = createLessonName(row, title, maxWidth, (row%2));
            ImageView conceptImg = createLessonImg(row, title, (totalConcepts-1), (row%2), maxWidth);

            // If the row is even put the text second else put text first
            if (row % 2 == 0) {
                gridlayout.addView(conceptName,gridLayoutParam1);
                gridlayout.addView(conceptImg,gridLayoutParam0);
            }
            else {
                gridlayout.addView(conceptName,gridLayoutParam0);
                gridlayout.addView(conceptImg,gridLayoutParam1);
            }
        }
    }

    /**
     * Creates a TextView for the lesson name that links to the lesson summary page
     * @param index Makes the Text View clickable to the correct lesson summary
     * @param title the text put into the TextView
     * @param maxWidth Ensures the text stays on its half of the screen
     * @param odd Determines whether the text is left or right aligned
     * @return the finished TextView, ready to be put into the layout
     */
    public TextView createLessonName(int index, String title, int maxWidth, int odd) {
        TextView lessonName = new TextView(this);
        final int offset = index;
        final String lessonTitle = title;
        if (odd == 1) {
            lessonName.setGravity(Gravity.RIGHT);
        }
        else {
            lessonName.setGravity(Gravity.LEFT);
        }
        lessonName.setWidth(maxWidth);

        //dynamic size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        lessonName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        lessonName.setText(title);
        lessonName.setHorizontallyScrolling(false);
        lessonName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                lessonID = dbHelper.selectLessonID(conceptID,offset);

                // If lesson was previously completed, the user can choose which part of the lesson
                //     to jump to
                if (dbHelper.isLessonAlreadyDone(lessonID)) {
                    lessonDialog.setTitle("Pick a lesson section:");

                    lessonDialog.setIcon(R.drawable.appicon);

                    lessonDialog.setAdapter(optionAdapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            activityChangeIntent = new Intent(LearnLessons.this,
                                                    LessonSummary.class);
                                            activityChangeIntent.putExtra("conceptID",conceptID);
                                            activityChangeIntent.putExtra("lessonID",lessonID);
                                            activityChangeIntent.putExtra("lessonTitle",lessonTitle);

                                            LearnLessons.this.startActivity(activityChangeIntent);
                                            break;
                                        case 1:
                                            activityChangeIntent = new Intent(LearnLessons.this,
                                                    LessonSteps.class);
                                            activityChangeIntent.putExtra("conceptID",conceptID);
                                            activityChangeIntent.putExtra("lessonID",lessonID);

                                            LearnLessons.this.startActivity(activityChangeIntent);
                                            break;
                                        case 2:
                                            activityChangeIntent = new Intent(LearnLessons.this,
                                                    LessonExample.class);
                                            activityChangeIntent.putExtra("conceptID",conceptID);
                                            activityChangeIntent.putExtra("lessonID",lessonID);

                                            LearnLessons.this.startActivity(activityChangeIntent);
                                            break;
                                        case 3:
                                            activityChangeIntent = new Intent(LearnLessons.this,
                                                    GameIntro.class);
                                            activityChangeIntent.putExtra("next_activity", 0);
                                            activityChangeIntent.putExtra("conceptID",conceptID);
                                            activityChangeIntent.putExtra("lessonID",lessonID);
                                            activityChangeIntent.putExtra("gameName", "");

                                            LearnLessons.this.startActivity(activityChangeIntent);
                                            break;
                                        case 4:
                                            activityChangeIntent = new Intent(LearnLessons.this,
                                                    Question.class);
                                            activityChangeIntent.putExtra("conceptID",conceptID);
                                            activityChangeIntent.putExtra("lessonID",lessonID);
                                            activityChangeIntent.putExtra("redoComplete", 0);

                                            LearnLessons.this.startActivity(activityChangeIntent);
                                            break;
                                    }
                                }
                            });

                    // Add the buttons
                    lessonDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });

                    // Get the AlertDialog from create()
                    AlertDialog dialog = lessonDialog.create();
                    dialog.show();
                }
                // Otherwise, start the user out at the beginning of the lesson
                else {
                    Intent activityChangeIntent = new Intent(LearnLessons.this, LessonSummary.class);
                    activityChangeIntent.putExtra("conceptID",conceptID);
                    activityChangeIntent.putExtra("offset",offset);
                    activityChangeIntent.putExtra("lessonTitle",lessonTitle);

                    LearnLessons.this.startActivity(activityChangeIntent);
                }
            }
        });
        return lessonName;
    }

    /**
     * Creates an ImageView for the image that links to the Lesson Summary
     * The image used is determined by what row the image is being added to:
     *     there is an image for the first row
     *     there is an image for odd middle rows
     *     there is an image for even middle rows
     *     there are two images for the last row, depending on whether it is even or odd
     * @param index Makes the Text View clickable to the correct lesson summary
     * @param title the title of the lesson
     * @param max the index of the last item
     * @param odd Determines whether the image is left or right aligned
     * @param maxWidth used to make sure the image does not exceed more than half of the screen
     * @return an Image View set up with correct parameters to be put onto the page
     */
    public ImageView createLessonImg(int index, String title, int max, int odd, int maxWidth) {
        final int offset = index;
        final String lessonTitle = title;
        ImageView lessonImg = new ImageView(this);
        lessonImg.setMaxWidth(maxWidth);
        lessonImg.setAdjustViewBounds(true);
        lessonImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        lessonImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                lessonID = dbHelper.selectLessonID(conceptID,offset);

                // If lesson was previously completed, the user can choose which part of the lesson
                //     to jump to
                if (dbHelper.isLessonAlreadyDone(lessonID)) {
                    lessonDialog.setTitle("Pick a lesson section:");

                    lessonDialog.setIcon(R.drawable.appicon);

                    lessonDialog.setAdapter(optionAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    activityChangeIntent = new Intent(LearnLessons.this,
                                            LessonSummary.class);
                                    activityChangeIntent.putExtra("conceptID",conceptID);
                                    activityChangeIntent.putExtra("lessonID",lessonID);
                                    activityChangeIntent.putExtra("lessonTitle",lessonTitle);

                                    LearnLessons.this.startActivity(activityChangeIntent);
                                    break;
                                case 1:
                                    activityChangeIntent = new Intent(LearnLessons.this,
                                            LessonSteps.class);
                                    activityChangeIntent.putExtra("conceptID",conceptID);
                                    activityChangeIntent.putExtra("lessonID",lessonID);

                                    LearnLessons.this.startActivity(activityChangeIntent);
                                    break;
                                case 2:
                                    activityChangeIntent = new Intent(LearnLessons.this,
                                            LessonExample.class);
                                    activityChangeIntent.putExtra("conceptID",conceptID);
                                    activityChangeIntent.putExtra("lessonID",lessonID);

                                    LearnLessons.this.startActivity(activityChangeIntent);
                                    break;
                                case 3:
                                    activityChangeIntent = new Intent(LearnLessons.this,
                                            GameIntro.class);
                                    activityChangeIntent.putExtra("next_activity", 0);
                                    activityChangeIntent.putExtra("conceptID",conceptID);
                                    activityChangeIntent.putExtra("lessonID",lessonID);
                                    activityChangeIntent.putExtra("gameName", "");

                                    LearnLessons.this.startActivity(activityChangeIntent);
                                    break;
                                case 4:
                                    activityChangeIntent = new Intent(LearnLessons.this,
                                            Question.class);
                                    activityChangeIntent.putExtra("conceptID",conceptID);
                                    activityChangeIntent.putExtra("lessonID",lessonID);
                                    activityChangeIntent.putExtra("redoComplete", 0);

                                    LearnLessons.this.startActivity(activityChangeIntent);
                                    break;
                            }
                        }
                    });

                    // Add the buttons
                    lessonDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });

                    // Get the AlertDialog from create()
                    AlertDialog dialog = lessonDialog.create();
                    dialog.show();
                }
                // Otherwise, start the user out at the beginning of the lesson
                else {
                    Intent activityChangeIntent = new Intent(LearnLessons.this, LessonSummary.class);
                    activityChangeIntent.putExtra("conceptID",conceptID);
                    activityChangeIntent.putExtra("offset",offset);
                    activityChangeIntent.putExtra("lessonTitle",lessonTitle);

                    LearnLessons.this.startActivity(activityChangeIntent);
                }
            }
        });
        // Set the correct image depending on the place of the lesson in the list
        if (odd == 0) {
            if (index == 0) {
                lessonImg.setImageResource(R.drawable.goldbag_start);
            }
            else if (index == max) {
                lessonImg.setImageResource(R.drawable.goldbag_end_odd);
            }
            else {
                lessonImg.setImageResource(R.drawable.goldbag_mid_odd);
            }
        }
        else {
            if (index == max) {
                lessonImg.setImageResource(R.drawable.goldbag_end_even);
            }
            else {
                lessonImg.setImageResource(R.drawable.goldbag_mid_even);
            }
        }
        return lessonImg;
    }
}
