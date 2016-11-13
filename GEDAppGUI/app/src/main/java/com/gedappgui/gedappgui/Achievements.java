/*
 * Achievements.java
 *
 * Achievements page activity
 *
 * View that displays student's achievements and lets them see decriptions of achievements
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class Achievements extends AppCompatActivity {

    // Gridview
    GridView gridview;

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_achievements);

        // Allow homaAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridview = (GridView) findViewById(R.id.achievements_gridView);
        Integer[] buttonPictures = {
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
    }

    /*
     * Registers onItemClickListener
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                TextView achievementText = (TextView)findViewById(R.id.achievement_description);
                String achievementDesc = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        achievementDesc = "Achievement 1";
                        break;
                    case 1:
                        achievementDesc = "Achievement 2";
                        break;
                    case 2:
                        achievementDesc = "Achievement 3";
                        break;
                    case 3:
                        achievementDesc = "Achievement 4";
                        break;
                    case 4:
                        achievementDesc = "Achievement 5";
                        break;
                    case 5:
                        achievementDesc = "Achievement 6";
                        break;
                    case 6:
                        achievementDesc = "Achievement 7";
                        break;
                    case 7:
                        achievementDesc = "Achievement 8";
                        break;
                    case 8:
                        achievementDesc = "Achievement 9";
                        break;
                    default:
                        break;
                }

                achievementText.setText(achievementDesc);
            }
        });
    }

    /*
     * UnRegisters onItemClickListener
     * Called every time this activity loses the focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        gridview.setOnItemClickListener(null);
    }

    /*
     * UnRegisters onItemClickListener
     * Called every time this activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gridview.setOnItemClickListener(null);
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
                gridview.setOnItemClickListener(null);
                Intent intentHomeAchievement = new Intent(this, MainActivity.class);
                intentHomeAchievement.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeAchievement);
                return true;
            // action with ID action_refresh was selected
            case R.id.action_home:
                gridview.setOnItemClickListener(null);
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

    /*
     * Called when the user clicks an achievement
     * Sets text for achievement description
     */
    /*public void showAchievementDescription(View view) {

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
    }*/
}
