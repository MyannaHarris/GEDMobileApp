/*
 * LearnConcepts.java
 *
 * Learn Concepts page activity
 *
 * Gives a "trail" of math concepts that a student can choose to start
 * Each concept has multiple lessons
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-13-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class LearnConcepts extends AppCompatActivity {

    GridLayout gridlayout;
    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_concepts);

        // Allow homaAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridlayout = (GridLayout) findViewById(R.id.concepts_gridView);
        //gridlayout.setLayoutParams(WRAP_CONTENT);

        //this won't be necessary once it's hooked up to the db
        ArrayList conceptNames = new ArrayList();
        conceptNames.add("Algebra Basics");
        conceptNames.add("Intermediate Algebra I");
        conceptNames.add("Intermediate Algebra II");
        conceptNames.add("Advanced Algebra");

        //put things in the gridlayout
        setGridInfo(conceptNames);

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
                Intent intentHomeConcept = new Intent(this, MainActivity.class);
                intentHomeConcept.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeConcept);
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

    /*
     * Called whena concept button is clicked
     * Opens the lesson "trail" page
     */
    public void gotToLesson(View view) {
        Intent intent = new Intent(this, LearnLessons.class);
        startActivity(intent);
    }

    public void setGridInfo(ArrayList titles) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels/2;

        int totalConcepts = titles.size();
        for (int row = 0; row < totalConcepts; row++) {

            GridLayout.Spec thisRow = GridLayout.spec(row, 1);
            GridLayout.Spec col0 = GridLayout.spec(0, 1);
            GridLayout.Spec col1 = GridLayout.spec(1, 1);
            GridLayout.LayoutParams gridLayoutParam0 = new GridLayout.LayoutParams(thisRow, col0);
            GridLayout.LayoutParams gridLayoutParam1 = new GridLayout.LayoutParams(thisRow, col1);
            TextView conceptName = createConceptName(titles.get(row).toString(), maxWidth, (row%2));
            ImageView conceptImg = createConceptImg(row, (totalConcepts-1), (row%2), maxWidth);

            if (row % 2 == 0) {
                gridLayoutParam0.setGravity(Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL|Gravity.CLIP_VERTICAL);
                gridLayoutParam1.setGravity(Gravity.START|Gravity.CENTER_VERTICAL|Gravity.CLIP_VERTICAL);
                gridlayout.addView(conceptName,gridLayoutParam1);
                gridlayout.addView(conceptImg,gridLayoutParam0);
            }
            else {
                gridLayoutParam0.setGravity(Gravity.END|Gravity.CENTER_VERTICAL|Gravity.CLIP_VERTICAL);
                gridLayoutParam1.setGravity(Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL|Gravity.CLIP_VERTICAL);
                gridlayout.addView(conceptName,gridLayoutParam0);
                gridlayout.addView(conceptImg,gridLayoutParam1);
            }
        }
    }

    public TextView createConceptName(String title, int maxWidth, int odd) {
        TextView conceptName = new TextView(this);
        if (odd == 1) {
            conceptName.setGravity(Gravity.RIGHT);
        }
        conceptName.setWidth(maxWidth);
        conceptName.setTextSize(26);
        conceptName.setText(title);
        conceptName.setHorizontallyScrolling(false);
        conceptName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(LearnConcepts.this, LearnLessons.class);

                // currentContext.startActivity(activityChangeIntent);

                LearnConcepts.this.startActivity(activityChangeIntent);
            }
        });
        return conceptName;
    }

    public ImageView createConceptImg(int index, int max, int odd, int maxWidth) {
        ImageView conceptImg = new ImageView(this);
        conceptImg.setMaxWidth(maxWidth);
        conceptImg.setAdjustViewBounds(true);
        conceptImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        conceptImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(LearnConcepts.this, LearnLessons.class);

                // currentContext.startActivity(activityChangeIntent);

                LearnConcepts.this.startActivity(activityChangeIntent);
            }
        });
        if (odd == 0) {
            if (index == 0) {
                conceptImg.setImageResource(R.drawable.star_start);
            }
            else if (index == max) {
                conceptImg.setImageResource(R.drawable.star_end_left);
            }
            else {
                conceptImg.setImageResource(R.drawable.star_mid_left);
            }
        }
        else {
            if (index == max) {
                conceptImg.setImageResource(R.drawable.star_end_right);
            }
            else {
                conceptImg.setImageResource(R.drawable.star_mid_right);
            }
        }
        return conceptImg;
    }

}
