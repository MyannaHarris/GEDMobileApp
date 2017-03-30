/*
 * Play.java
 *
 * Play page activity
 *
 * Has links to games that the user has unlocked
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Play extends AppCompatActivity {

    private ArrayList<Integer> lessonIds;
    private ArrayList<String> gameNames;

    // Screen size
    int width;
    int height;

    // Database
    private DatabaseHelper dbHelper;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        // Get current lesson from the database
        dbHelper = new DatabaseHelper(this);
        int currLessonId = dbHelper.lessonCount();

        // Only show information if the user has completed at least one lesson
        // The current lesson id is one higher than the number of lessons the user has completed
        if (currLessonId > 1) {

            lessonIds = new ArrayList<Integer>();
            gameNames = new ArrayList<String>();

            // Fill lessonIds and gameNames with corresponding information
            for (int i = 1; i <= currLessonId - 1; i ++) {
                lessonIds.add(i);
                gameNames.add(dbHelper.selectLessonTitle(i));
            }

            // Get layout to add buttons to and set up layout parameters
            LinearLayout gamesLayout = (LinearLayout) findViewById(R.id.GameLayout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    (height/8));

            // Actually put in buttons
            for (int i = 0; i < lessonIds.size(); i++) {
                Button game = new Button(this);
                game.setText(gameNames.get(i));
                game.setLayoutParams(layoutParams);
                game.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (height / 35));
                game.setTag(lessonIds.get(i));
                gamesLayout.addView(game);

                // Button functionality, a listener for each one
                game.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Button gameButton = (Button) v;
                        Intent intent = new Intent(Play.this, GameIntro.class);
                        intent.putExtra("next_activity", 1);
                        intent.putExtra("lessonID", (Integer) gameButton.getTag());
                        startActivity(intent);
                    }
                });
            }
        }
        // If the user hasn't completed any lessons, add a bit of text into the layout
        else {
            LinearLayout gamesLayout = (LinearLayout) findViewById(R.id.GameLayout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 50, 20, 20);

            TextView noGame = new TextView(this);
            noGame.setGravity(Gravity.CENTER);
            noGame.setText("Please finish lessons to get games for your arcade.");
            noGame.setLayoutParams(layoutParams);
            noGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (height / 35));
            gamesLayout.addView(noGame);
        }
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
                Intent intentHomePlay = new Intent(this, MainActivity.class);
                intentHomePlay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomePlay);
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