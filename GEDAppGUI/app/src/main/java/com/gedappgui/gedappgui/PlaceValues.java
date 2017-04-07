/*
 * PlaceValues.java
 *
 * Place Values activity
 *
 * Shows the menu for selecting shapes and displays info based on the shape selected
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 1-31-17
 *
 */
package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlaceValues extends AppCompatActivity {
    //strings for the seekbar to select points on the number
    private String[] places = {
            "Millions, can be represented in this number as 1 * 1,000,000",
            "Hundred Thousands, can be represented in this number as 2 * 100,000",
            "Ten Thousands, can be represented in this number as 3 * 10,000",
            "Thousands, can be represented in this number as 4 * 1,000",
            "Hundreds, can be represented in this number as 5 * 100",
            "Tens, can be represented in this number as 6 * 10",
            "Ones, can be represented in this number as 7 * 1",
            "The decimal point separates the whole number part from the fractional part",
            "Tenths, can be represented in this number as 1 * .1",
            "Hundredths, can be represented in this number as 2 * .01",
            "Thousandths, can be represented in this number as 3 * .001",
            "Ten Thousandths, can be represented in this number as 4 * .0001",
            "Hundred Thousandths, can be represented in this number as 5 * .00001",
            "Millionths, can be represented in this number as 6 * .000001",
            "Ten Millionths, can be represented in this number as 7 * .0000001",

    };
    private SeekBar seekbar;
    private TextView places_text;
    private TextView places_title;
    //text for the seekbar to select
    private String instruct = "1234567.1234567";

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_values);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        places_text = (TextView)findViewById(R.id.places_text);
        places_title = (TextView)findViewById(R.id.number_change);

        //listener for when the seekbar changes
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                places_text.setText(places[progress]);

                //Used to get the correct position for setting the selected text red

                //Make and set the new String
                String before = instruct.substring(0,progress);
                String red = "<font color='#EE0000'>"+ instruct.charAt(progress) + "</font>";
                String end = instruct.substring(progress + 1);

                //Check for version to use correct HTML method
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
                    places_title.setText(Html.fromHtml(before + red + end,Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    places_title.setText(Html.fromHtml(before + red + end));
                }

            }
            //the next two methods do nothing but must be implemented in a seekbar listener
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        TextView ip = (TextView) findViewById(R.id.instructions_place);
        ip.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        TextView nc = (TextView) findViewById(R.id.number_change);
        nc.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        TextView pt = (TextView) findViewById(R.id.places_text);
        pt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        ViewGroup.LayoutParams paramss = seekbar.getLayoutParams();

        paramss.height = (height/10);

        seekbar.setLayoutParams(paramss);


    }

    /*
     * hides bottom navigation bar
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
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
}
