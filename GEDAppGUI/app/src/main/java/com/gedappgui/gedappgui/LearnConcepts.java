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
 * Last Edit: 11-14-16
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

import java.io.File;
import java.util.ArrayList;


public class LearnConcepts extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    GridLayout gridlayout;
    /*
     * Starts the activity and shows corresponding view on screen
     * TO COME: Gets concept information from the database
     *   then passes information to setGridInfo, a function to dynamically add to
     *   the GridLayout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_concepts);
        dbHelper = new DatabaseHelper(this);

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridlayout = (GridLayout) findViewById(R.id.concepts_gridView);
        //gridlayout.setLayoutParams(WRAP_CONTENT);
        ArrayList<String> concepts = dbHelper.selectUnlockedConcepts();

        //put things in the gridlayout
        setGridInfo(concepts);

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
     * Dynamically adds views to the GridLayout - one row per concept
     * Each concept has an ImageView, holding the image to create the "path"
     *   and a TextView, holding the concept name; the order of the views is
     *   decided by whether the row is even or not
     * Calls createConceptName and createConceptImg to actually make the views
     */
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
            int viewGravity = Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL;
            gridLayoutParam0.setGravity(viewGravity);
            gridLayoutParam1.setGravity(viewGravity);

            TextView conceptName = createConceptName(row, titles.get(row).toString(), maxWidth, (row%2));
            ImageView conceptImg = createConceptImg(row, (totalConcepts-1), (row%2), maxWidth);

            if (row % 2 == 0) {
                gridlayout.addView(conceptName,gridLayoutParam1);
                gridlayout.addView(conceptImg,gridLayoutParam0);
            }
            else {
                gridlayout.addView(conceptName,gridLayoutParam0);
                gridlayout.addView(conceptImg,gridLayoutParam1);
            }
        }
    }

    /*
     * Creates a TextView for the concept name that links to the lessons page
     * title is the text that is put into the view,
     * maxWidth makes sure the text stays on its half of the screen
     * odd determines if the text is aligned to the left or to the right
     */
    public TextView createConceptName(int index, String title, int maxWidth, int odd) {
        final int conceptID = index+1;
        TextView conceptName = new TextView(this);
        if (odd == 1) {
            conceptName.setGravity(Gravity.RIGHT);
        }
        else {
            conceptName.setGravity(Gravity.LEFT);
        }
        conceptName.setWidth(maxWidth);
        conceptName.setTextSize(26);
        conceptName.setText(title);
        conceptName.setHorizontallyScrolling(false);
        conceptName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(LearnConcepts.this, LearnLessons.class);
                activityChangeIntent.putExtra("conceptID",conceptID);

                // currentContext.startActivity(activityChangeIntent);

                LearnConcepts.this.startActivity(activityChangeIntent);
            }
        });
        return conceptName;
    }

    /*
     * Creates an ImageView for the image that links to the lessons page
     * What image is used is determined by what row the image is being added to:
     *     there is an image for the first row
     *     there is an image for odd middle rows
     *     there is an image for even middle rows
     *     there are two images for the last row, depending on whether it is even or odd
     * maxWidth is used to make sure the image does not exceed more than half of the screen
     */
    public ImageView createConceptImg(int index, int max, int odd, int maxWidth) {
        final int conceptID = index+1;
        ImageView conceptImg = new ImageView(this);
        conceptImg.setMaxWidth(maxWidth);
        conceptImg.setAdjustViewBounds(true);
        conceptImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        conceptImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(LearnConcepts.this, LearnLessons.class);
                activityChangeIntent.putExtra("conceptID",conceptID);

                // currentContext.startActivity(activityChangeIntent);

                LearnConcepts.this.startActivity(activityChangeIntent);
            }
        });
        if (odd == 0) {
            if (index == 0) {
                conceptImg.setImageResource(R.drawable.goldchest_start);
            }
            else if (index == max) {
                conceptImg.setImageResource(R.drawable.goldchest_end_odd);
            }
            else {
                conceptImg.setImageResource(R.drawable.goldchest_mid_odd);
            }
        }
        else {
            if (index == max) {
                conceptImg.setImageResource(R.drawable.goldchest_end_even);
            }
            else {
                conceptImg.setImageResource(R.drawable.goldchest_mid_even);
            }
        }
        return conceptImg;
    }

}
