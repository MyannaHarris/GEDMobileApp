/*
 * Achievements.java
 *
 * Achievements page activity
 *
 * View that displays all the student's earned achievements and lets them
 * see descriptions of achievements (how they earned them)
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 3-6-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Achievements extends AppCompatActivity {

    DatabaseHelper db;

    LinearLayout layout;

    GridView gridview;

    // List of bitmaps used so we can clean this up later
    private Bitmap[] buttonPictures;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_achievements);

        db = new DatabaseHelper(this);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Create gridview of achievements
        gridview = (GridView) findViewById(R.id.achievements_gridView);

        //get a list of all achievement images
        ArrayList<String> images = db.selectAchievementsImgs();

        //adds the keeping score achievement if it hasn't been earned yet
        if(!db.achievementExists(6)) {
            buttonPictures = new Bitmap[images.size()+1];
            buttonPictures[images.size()] = getFromAssets("keepingscore.png");
        }
        //if keeping score achievement is added, just create array
        else{
            buttonPictures = new Bitmap[images.size()];
        }

        //fill buttonPictures array with bitmaps of all earned achievements
        for (int i = 0; i < images.size(); i++) {
            buttonPictures[i] = getFromAssets(images.get(i));
        }

        //get size of the screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //use the bitmap button adapter to create buttons of the gridview images
        gridview.setAdapter(new BitmapButtonAdapter(this, buttonPictures, width, height));

        //gives an achievement (adds to the database) if the user opens the achievements
        // screen for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 6);
        startActivity(achievement);

        //create buttons of each achievement image
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                ArrayList<String> desc = db.selectAchievementsDesc();
                ArrayList<String> name = db.selectAchievementsNames();
                ArrayList<String> imgs = db.selectAchievementsImgs();

                String achievementDesc = "";
                String achievementName = "";
                String achievementImg = "";

                //get the desc, name and image that corresponds to the achievements position
                //in the gridview
                if(position >= 0 && position < desc.size()){
                    achievementDesc = desc.get(position);
                    achievementName = name.get(position);
                    achievementImg = imgs.get(position);
                }

                //opens the AchievementInfo activity and send it all necessary information
                Intent achievement = new Intent(v.getContext(), AchievementInfo.class);
                achievement.putExtra("achievementName", achievementName);
                achievement.putExtra("achievementDesc", achievementDesc);
                achievement.putExtra("achievementImg", achievementImg);
                v.getContext().startActivity(achievement);
            }
        });
    }

    /**
     * Returns a bitmap of the image with the given name found in the assets folder
     * @param imgName the name of the image you want
     * @return the bitmap from assets
     *
     * modified Myanna's code from LessonSteps
     */
    private Bitmap getFromAssets(String imgName)
    {
        AssetManager assetManager = getAssets();
        InputStream in = null;

        //tries to find the given filename in assets
        try {
            in = assetManager.open("achievement_pics/" + imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //creates a bitmap from the file got from assets
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Go to Home Screen
        Intent intentHomePlay = new Intent(this, MainActivity.class);
        intentHomePlay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHomePlay);
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
     * When the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (buttonPictures != null) {
            for (int i = 0; i < buttonPictures.length; i++) {
                if (buttonPictures[i] != null) {
                    buttonPictures[i].recycle();
                    buttonPictures[i] = null;
                }
            }
        }
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
                Intent intentHomeAchievement = new Intent(this, MainActivity.class);
                intentHomeAchievement.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeAchievement);
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
