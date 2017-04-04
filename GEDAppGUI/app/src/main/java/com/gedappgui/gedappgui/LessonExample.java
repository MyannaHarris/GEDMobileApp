/*
 * LessonExample.java
 *
 * Lesson Example page activity
 *
 * Gives an example working out of a math problem from the chosen lesson
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

public class LessonExample extends AppCompatActivity {

    private int conceptID;
    private int lessonID;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get information from intents passed from the previous page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_example);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Set up database helper in order to get example text onto the page
        DatabaseHelper db = new DatabaseHelper(this);
        setExamples(db, lessonID);

        // Create button to go to next step, the Game Intro page
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Button nextbtn = (Button) findViewById(R.id.lessonGame);
        TextView ex_1 = (TextView) findViewById(R.id.example_1);
        TextView ex_2 = (TextView) findViewById(R.id.example_2);
        TextView title1 = (TextView) findViewById(R.id.example1_header);
        TextView title2 = (TextView) findViewById(R.id.example2_header);

        nextbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        ex_1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        ex_2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        title1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        title2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));


    }

    /**
     * Queries the database for two examples and then places them on the screen
     * @param db DatabaseHelper object to use to query the database
     * @param id the lesson ID to get examples for
     */
    private void setExamples(DatabaseHelper db, int id){
        String example_1 = db.selectLessonExample1(id);
        String example_2 = db.selectLessonExample2(id);
        TextView ex_1 = (TextView) findViewById(R.id.example_1);
        TextView ex_2 = (TextView) findViewById(R.id.example_2);
        ex_1.setText(toHTML(example_1));
        ex_2.setText(toHTML(example_2));
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
                finish();
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
     * Opens Game Intro, passing concept ID and lesson ID information to that class, as
     *     well as a 0 for next_activity to tell the game to go to the question activity after
     * Called when the Next button is pressed
     * @param view current view
     */
    public void gotToLessonGame(View view) {
        Intent intent = new Intent(this, GameIntro.class);
        intent.putExtra("next_activity", 0);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.putExtra("gameName", "");
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
