/*
 * Achievements.java
 *
 * Achievements page activity
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

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Achievements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DailyNotification.notify(this, "testing", 333);
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
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
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

    /** Called when the user clicks an achievement */
    public void showAchievementDescription(View view) {

        TextView achievementText = (TextView)findViewById(R.id.achievement_description);
        String achievementDesc = "";

        switch(view.getId()) {
            case R.id.achievement_1:
                achievementDesc = "Achievement 1";
                break;
            case R.id.achievement_2:
                achievementDesc = "Achievement 2";
                break;
            case R.id.achievement_3:
                achievementDesc = "Achievement 3";
                break;
            case R.id.achievement_4:
                achievementDesc = "Achievement 4";
                break;
            case R.id.achievement_5:
                achievementDesc = "Achievement 5";
                break;
            case R.id.achievement_6:
                achievementDesc = "Achievement 6";
                break;
            case R.id.achievement_7:
                achievementDesc = "Achievement 7";
                break;
            case R.id.achievement_8:
                achievementDesc = "Achievement 8";
                break;
            case R.id.achievement_9:
                achievementDesc = "Achievement 9";
                break;
            default:
                break;
        }

        achievementText.setText(achievementDesc);
    }
}
