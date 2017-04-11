/*
 * LessonSummary.java
 *
 * Lesson Summary page activity
 *
 * Gives a summary of the math lesson content
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 3-20-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LessonSummary extends AppCompatActivity {
    // Globals for database access, concept ID, lesson ID, and lesson offset
    private DatabaseHelper dbHelper;
    private int conceptID;
    private int lessonID;
    // this is for navigation from the lessons page, which doesn't know the lesson ids
    private int lessonOffset;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get lesson information from database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_summary);
        dbHelper = new DatabaseHelper(this);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        String lessonTitle = mIntent.getStringExtra("lessonTitle");
        // If the lesson ID is passed, get that
        if (mIntent.getExtras().containsKey("lessonID")) {
            lessonID = mIntent.getIntExtra("lessonID", 0);
        }
        // Otherwise, get the lesson ID using the offset value
        else {
            lessonOffset = mIntent.getIntExtra("offset", 0);
            lessonID = dbHelper.selectLessonID(conceptID,lessonOffset);
        }

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Set the title on the page
        TextView title = (TextView) findViewById(R.id.lessonSummary_title);
        title.setText(lessonTitle);

        // Set the summary text on the page
        String lessonSummary = dbHelper.selectLessonSummary(lessonID);
        TextView summary = (TextView) findViewById(R.id.lessonSummary_text);
        summary.setText(toHTML(lessonSummary));

        //  Make current lesson this one...because we're on it now
        if (dbHelper.selectCurrentLessonID() != lessonID) {
            dbHelper.updateCurrentLessonID(lessonID);
        }

        // Create button to go to next step, the Lesson Steps page
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Button nextbtn = (Button) findViewById(R.id.lessonSteps);

        nextbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        summary.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Goes to lessons
     */
    @Override
    public void onBackPressed() {
        Intent intentLesson = new Intent(this, LearnLessons.class);
        intentLesson.putExtra("conceptID",conceptID);
        startActivity(intentLesson);
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
                Intent intentLesson = new Intent(this, LearnLessons.class);
                intentLesson.putExtra("conceptID",conceptID);
                startActivity(intentLesson);
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
     * Opens the lesson steps page, passing concept ID and lesson ID information to that class
     * Called when the Next button is pressed
     * @param view current view
     */
    public void gotToLessonSteps(View view) {
        Intent intent = new Intent(this, LessonSteps.class);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        startActivity(intent);
    }

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }

    }
}
