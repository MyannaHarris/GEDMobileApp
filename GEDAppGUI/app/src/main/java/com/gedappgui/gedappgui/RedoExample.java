/*
 * RedoExample.java
 *
 * Redo Example page activity
 *
 * Takes the student through another example
 * before playing the game and trying the questions again
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-6-16
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

import java.util.ArrayList;

public class RedoExample extends AppCompatActivity {

    //current lesson number
    private int lessonID;
    //current conceot number
    private int conceptID;
    //total number of times the user has attempted the question section
    private int totalRetries;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_redo_example);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        totalRetries = mIntent.getIntExtra("totalRetries", 0);
        DatabaseHelper db = new DatabaseHelper(this);
        setRedos(db, lessonID);
    }

    /**
     * Sets the redo examples using the database
     * @param db the database helper object
     * @param lesson_id the current lesson number
     */
    public void setRedos(DatabaseHelper db, int lesson_id){
        //get screen dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        ArrayList<String> redos = new ArrayList<>();
        redos = db.selectRedos(lesson_id);
        TextView redo_1 = (TextView) findViewById(R.id.redo1_text);
        TextView redo_2 = (TextView) findViewById(R.id.redo2_text);
        TextView redo_3 = (TextView) findViewById(R.id.redo3_text);
        TextView redoHead1 = (TextView)findViewById(R.id.redo1);
        TextView redoHead2 = (TextView)findViewById(R.id.redo2);
        TextView redoHead3 = (TextView)findViewById(R.id.redo3);
        Button toGame = (Button)findViewById(R.id.redoLessonGame);
        Button skipGame = (Button)findViewById(R.id.skipLessonGame);

        redo_1.setText(toHTML(redos.get(0)));
        redo_2.setText(toHTML(redos.get(1)));
        redo_3.setText(toHTML(redos.get(2)));

        //dynamic text size
        redo_1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        redo_2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        redo_3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        redoHead1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        redoHead2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        redoHead3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        toGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        skipGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
    }

    /**
     * hides bottom navigation bar
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

    /*
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /**
     * Sets what menu will be in the action bar
     * homeonlymenu has the settings button and the home button
     * @param menu the menu that is clicked
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
     * Called by the move on to game button being clicked
     * Opens the lesson game
     * intent.putExtra("next_activity", 0);
     *   = sends 0 to tell game to go to question activity next
     * @param view the button that calls this function
     */
    public void gotToLessonGame(View view) {
        Intent intent = new Intent(this, GameIntro.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("lessonID", lessonID);
        intent.putExtra("conceptID", conceptID);
        intent.putExtra("next_activity", 0);
        intent.putExtra("gameName", "");
        intent.putExtra("redoComplete", 1);
        intent.putExtra("totalRetries",totalRetries);
        startActivity(intent);
    }

    /**
     * Called by the skip game being clicked
     * Skips to the questions
     * @param view the button that calls this function
     */
    public void skipTheLessonGame(View view) {
        Intent intent = new Intent(this, Question.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.putExtra("redoComplete", 1);
        intent.putExtra("totalRetries",totalRetries);
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
