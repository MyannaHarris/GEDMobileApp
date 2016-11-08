/*
 * SettingsActivity.java
 *
 * Settings page
 *
 * Activity that holds the settings fragment
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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets defaults for the setting preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Could help with changing text size
        /*String currTheme = ((MyApplication) this.getApplication()).getCurrTheme();
        switch(currTheme) {
            case "Medium":
                setTheme(R.style.AppTheme);
                break;
            case "Small":
                setTheme(R.style.AppThemeSmall);
                break;
            case "Large":
                setTheme(R.style.AppThemeLarge);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }*/

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        // Allow homaAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /*
     * Sets the sharedpreference listener so the settings changes will be propagated
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) this.getApplication()).setSharedPreferences(
                PreferenceManager.getDefaultSharedPreferences(this));
        ((MyApplication) this.getApplication()).getSharedPreferences().
                registerOnSharedPreferenceChangeListener(
                        ((MyApplication) this.getApplication()).getSharedPreferenceListener());
    }

    /*
     * Unsets the sharedpreference listener so it is cleaned up
     * Called every time this activity loses the focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        ((MyApplication) this.getApplication()).getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(
                ((MyApplication) this.getApplication()).getSharedPreferenceListener());
    }

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
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
     * nosettingsmenu has the home button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nosettingsmenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
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
            default:
                break;
        }

        return true;
    }
}
