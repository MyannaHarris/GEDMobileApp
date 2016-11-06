/*
 * MainActivity.java
 *
 * Home screen activity
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-5-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
     * Starts the first activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //String username = sharedPref.getString("username", "default value");

        if (!((MyApplication) this.getApplication()).getLoginStatus()) {
            // Show login first time the app is opened
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            // Show home screen whenever app is opened after that
            setContentView(R.layout.activity_main);
            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);
            String greeting = "Hello " + ((MyApplication) this.getApplication()).getName();
            greeting += "!\nWelcome to the app.";
            greetingText.setText(greeting);
        }

    }

    /* 
     * Shows and hides the bottom navigation bar when user flings on screen 
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
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
     * Sets what menu will be in the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_continueLesson:
                Intent intentContinue = new Intent(this, LessonSummary.class);
                startActivity(intentContinue);
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

    /*
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /*
     * Opens Sprite view when button is clicked
     */
    /** Called when the user clicks the Sprite */
    public void gotToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /*
     * Opens Last lesson worked on (Lesson Summary) view when button is clicked
     */
    /** Called when the user clicks the Continue Lesson button */
    public void gotToContinueLesson(View view) {
        Intent intent = new Intent(this, LessonSummary.class);
        startActivity(intent);
    }

    /*
     * Opens Learn (Concepts) view when button is clicked
     */
    /** Called when the user clicks the Learn button */
    public void gotToLearn(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /*
     * Opens Play (Games) view when button is clicked
     */
    /** Called when the user clicks the Play button */
    public void gotToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    /*
     * Opens Achievements view when button is clicked
     */
    /** Called when the user clicks the Achievements button */
    public void gotToAchievements(View view) {
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
    }

    /*
     * Opens Tools view when button is clicked
     */
    /** Called when the user clicks the Tools button */
    public void gotToTools(View view) {
        Intent intent = new Intent(this, Tools.class);
        startActivity(intent);
    }

    /*
     * Opens Settings view when button is clicked
     */
    /** Called when the user clicks the Settings button */
    public void gotToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
