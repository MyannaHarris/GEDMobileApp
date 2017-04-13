/*
 * NGZLinks.java
 *
 * Links page activity
 *
 * View from which students can select aa link which will launch in their browser
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 3-4-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NGZLinks extends AppCompatActivity {

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngzlinks);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        TextView expl_link = (TextView) findViewById(R.id.explanation_link);
        expl_link.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        TextView title = (TextView) findViewById(R.id.Links_header);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));

        //set links for clicking
        TextView home = (TextView) findViewById(R.id.home_link);
        TextView info = (TextView) findViewById(R.id.information);
        TextView job = (TextView) findViewById(R.id.Job);
        TextView passport = (TextView) findViewById(R.id.passport);
        TextView who = (TextView) findViewById(R.id.who);
        TextView resources = (TextView) findViewById(R.id.Resources);
        home.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        info.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        job.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        passport.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        who.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        resources.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        home.setMovementMethod(LinkMovementMethod.getInstance());
        info.setMovementMethod(LinkMovementMethod.getInstance());
        job.setMovementMethod(LinkMovementMethod.getInstance());
        passport.setMovementMethod(LinkMovementMethod.getInstance());
        who.setMovementMethod(LinkMovementMethod.getInstance());
        resources.setMovementMethod(LinkMovementMethod.getInstance());
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
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}
