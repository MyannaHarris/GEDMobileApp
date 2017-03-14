/*
 * Tools.java
 *
 * Tools page activity
 *
 * View from which students can select a study tool to look at
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 10-26-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Tools extends AppCompatActivity {

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Button fracbtn = (Button) findViewById(R.id.fracdectool);
        Button slopebtn = (Button) findViewById(R.id.slopeCalculator);
        Button membtn = (Button) findViewById(R.id.FormMem);
        Button geobtn = (Button) findViewById(R.id.geoassist);
        Button placebtn = (Button) findViewById(R.id.placevalues);
        Button linksbtn = (Button) findViewById(R.id.links);
        Button tutbtn = (Button) findViewById(R.id.tutorial_button);



        ViewGroup.LayoutParams params = fracbtn.getLayoutParams();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        params.height = (height/8);

        fracbtn.setLayoutParams(params);
        fracbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        slopebtn.setLayoutParams(params);
        slopebtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        membtn.setLayoutParams(params);
        membtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        geobtn.setLayoutParams(params);
        geobtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        placebtn.setLayoutParams(params);
        placebtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        linksbtn.setLayoutParams(params);
        linksbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        tutbtn.setLayoutParams(params);
        tutbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));

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
     * Called by a tool being selected
     * Opens fraction to decimal tool
     */
    public void goTofracdectool(View view) {
        Intent intent = new Intent(this, FractionToDecimalTool.class);
        startActivity(intent);
    }

    /*
     * Called by a tool being selected
     * Opens fraction to decimal tool
     */
    public void goToFormulas(View view) {
        Intent intent = new Intent(this, FormulaMemorization.class);
        startActivity(intent);
    }

    /*
     * Called by a tool being selected
     * Opens fraction to decimal tool
     */
    public void goToSlopeCalculator(View view) {
        Intent intent = new Intent(this, SlopeCalculator.class);
        startActivity(intent);
    }

    public void goToGeoAssist (View view){
        Intent intent = new Intent(this, GeoAssist.class);
        startActivity(intent);
    }

    public void goToPlaceValues (View view){
        Intent intent = new Intent(this, PlaceValues.class);
        startActivity(intent);
    }

    /*
     * Opens Tutorial view when button is clicked
     * Called when the user clicks the Settings button
     */
    public void goToTutorial(View view){
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    public void goToLinks(View view){
        Intent intent = new Intent(this, NGZLinks.class);
        startActivity(intent);
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
                Intent intentHomeTools = new Intent(this, MainActivity.class);
                intentHomeTools.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeTools);
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
