/*
        * SlopeCalculator.java
        *
        * Slope Calculator tool activity
        *
        * View that will host the Slope Calculator tool
        *
        * Worked on by:
        * Myanna Harris
        * Kristina Spring
        * Jasmine Jans
        * Jimmy Sherman
        *
        * Last Edit: 1-12-17
        *
        */
package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class SlopeCalculator extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_calculator);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Allows for keyboard resizing on the inputs
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);
    }

    /*
     * Listener for the Find slope button
     * Makes a string that shows the steps to find the slope if valid inputs are made
     * If not, throws an error string and exits.
     */
    public void MakeString (View view){
        String steps = new String();
        TextView stepbystep = (TextView)findViewById(R.id.stepbystep);

        EditText x1_text = (EditText)findViewById(R.id.x1_input);
        EditText x2_text = (EditText)findViewById(R.id.x2_input);
        EditText y1_text = (EditText)findViewById(R.id.y1_input);
        EditText y2_text = (EditText)findViewById(R.id.y2_input);

        //Check to see if the hint is still in the input
        if (TextUtils.isEmpty(x1_text.getText()) || TextUtils.isEmpty(x2_text.getText())
                || TextUtils.isEmpty(y1_text.getText()) || TextUtils.isEmpty(y2_text.getText())){
            stepbystep.setText("Invalid input, all inputs must have a number in them");
            return;
        }

        //convert text values to float values to be calculated
        float x1 = Float.parseFloat(x1_text.getText().toString());
        float x2 = Float.parseFloat(x2_text.getText().toString());
        float y1 = Float.parseFloat(y1_text.getText().toString());
        float y2 = Float.parseFloat(y2_text.getText().toString());
        if (y1 - y2 == 0.0){
            steps = "Slope is undefined";
            return;
        }
        float slope = ((y2 - y1) / (x2 - x1));
        String ordered1 = "(" + x1 + "," + y1 + ")";
        String ordered2 = "(" + x2 + "," + y2 + ")";
        steps = "The ordered pairs you put in are: " + ordered1 + " and " + ordered2 +
                "The slope of the line is: \n \nm = " + slope + " \n \nThe first step is to " +
                "remember our slope formula as: \n \nm = (y2 - y1) / (x2 - x1). \n \nThe next step is " +
                "to substitute each variable with our ordered pairs. Lets do this one at a time: " +
                "\n \n y2 = " + y2 + " which goes into our formula as the y2 variable: \n \n" +
                "m = (" + y2 + " - y1) / (x2 - x1). \n \nNow we plug in the value for y1 which is " +
                y1 + ": \n \nm = (" + y2 + " - "+ y1 + ") / (x2 - x1). \n \nNow we can put the remaining " +
                "two variables in, x1 and x2, which are: " + x1 + " and " + x2 + ": \n \n" +
                "m = (" + y2 + " - "+ y1 + ") / (" + x2 + " - " + x1 + "). \n \nNow we simplify, " +
                "subtracting above and below the divide sign: " + (y2 - y1) + " / " + (x2 - x1) + "\n \n" +
                "And now we divide to give us our answer: \n \nm = " + slope + "\n";
        stepbystep.setText(steps);
        onResume();
    }

    /*
     * Listener for the Reset button
     * Sets the current stepbystep text to null
     */
    public void ResetText(View view){
        TextView stepbystep = (TextView)findViewById(R.id.stepbystep);
        stepbystep.setText(null);
        onResume();
    }

    /*
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

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
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
     * Sets what menu will be in the action bar
     * homeonlymenu has the settings button and the home button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
     * settings = goes to settings page
     * android.R.id.home = go to the activity that called the current activity
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
}
