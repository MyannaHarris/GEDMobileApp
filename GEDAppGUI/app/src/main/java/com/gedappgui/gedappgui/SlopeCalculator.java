/*
 * SlopeCalculator.java
 *
 * Slope Calculator tool activity
 *
 * View that will host the Slope Calculator tool
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring
 *
 * This file is part of DragonAcademy.
 *
 * DragonAcademy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License. All redistributions
 * of the app or modifications of the app are to remain free in accordance
 * with the GNU General Public License.
 *
 * DragonAcademy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DragonAcademy.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SlopeCalculator extends AppCompatActivity {

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_calculator);

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Allows for keyboard resizing on the inputs
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);

        //listener for action Done on the keyboard, evaluates the slope inputs
        EditText y2input = (EditText)findViewById(R.id.y2_input);
        y2input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view =  findViewById(R.id.FindSlope);
                    MakeString(view);
                }
                return false;
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        String intro = "The slope formula can be written as: <br /> <br /> m = (y<sub><small>2</small></sub> " +
                "- y<sub><small>1</small></sub>) / (x<sub><small>2</small></sub> - x<sub><small>1</small></sub>) " +
                "<br /> <br />For any ordered pair (x<sub><small>1</small></sub>,y<sub><small>1</small></sub>) and " +
                "(x<sub><small>2</small></sub>,y<sub><small>2</small></sub>). Input values for the ordered pairs " +
                "below to see the slope and the steps that were taken to find the slope.";

        EditText y1input = (EditText)findViewById(R.id.y1_input);
        EditText x1input = (EditText)findViewById(R.id.x1_input);
        EditText x2input = (EditText)findViewById(R.id.x2_input);

        ViewGroup.LayoutParams paramsy2 = y2input.getLayoutParams();
        ViewGroup.LayoutParams paramsy1 = y1input.getLayoutParams();
        ViewGroup.LayoutParams paramsx2 = x2input.getLayoutParams();
        ViewGroup.LayoutParams paramsx1 = x1input.getLayoutParams();

        paramsy2.width = (int) (width/2.3);
        paramsy1.width = (int) (width/2.3);
        paramsx1.width = (int) (width/2.3);
        paramsx2.width = (int) (width/2.3);

        x1input.setLayoutParams(paramsx1);
        x2input.setLayoutParams(paramsx2);
        y1input.setLayoutParams(paramsy1);
        y2input.setLayoutParams(paramsy2);

        TextView instr = (TextView) findViewById(R.id.instructions);
        instr.setText(toHTML(intro));
        instr.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        x1input.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        x1input.setHint(toHTML("Input x<sub><small>1</small></sub> value"));
        x2input.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        x2input.setHint(toHTML("Input x<sub><small>2</small></sub> value"));
        y1input.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        y1input.setHint(toHTML("Input y<sub><small>1</small></sub> value"));
        y2input.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        y2input.setHint(toHTML("Input y<sub><small>2</small></sub> value"));

        Button rbtn = (Button) findViewById(R.id.resetbutton);
        Button sbtn = (Button) findViewById(R.id.FindSlope);

        // Set button height
        sbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        rbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        TextView stepbystep = (TextView)findViewById(R.id.stepbystep);
        stepbystep.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

    }

    /**
     * Listener for the Find slope button
     * Makes a string that shows the steps to find the slope if valid inputs are made
     * If not, throws an error string and exits.
     * @param view the slope button that is pressed
     */
    public void MakeString (View view){
        String steps;
        TextView stepbystep = (TextView)findViewById(R.id.stepbystep);

        EditText x1_text = (EditText)findViewById(R.id.x1_input);
        EditText x2_text = (EditText)findViewById(R.id.x2_input);
        EditText y1_text = (EditText)findViewById(R.id.y1_input);
        EditText y2_text = (EditText)findViewById(R.id.y2_input);

        //Check to see if the hint is still in the input
        if (TextUtils.isEmpty(x1_text.getText()) || TextUtils.isEmpty(x2_text.getText())
                || TextUtils.isEmpty(y1_text.getText()) || TextUtils.isEmpty(y2_text.getText())){
            stepbystep.setText("Invalid input, all inputs must have a number in them");
            return;
        }

        //convert text values to float values to be calculated
        float x1 = Float.parseFloat(x1_text.getText().toString());
        float x2 = Float.parseFloat(x2_text.getText().toString());
        float y1 = Float.parseFloat(y1_text.getText().toString());
        float y2 = Float.parseFloat(y2_text.getText().toString());
        if (x1 == x2){
            steps = "Slope is undefined";
            stepbystep.setText(steps);
            return;
        }
        float slope = ((y2 - y1) / (x2 - x1));
        if (y1 == y2){
            slope = (float)0.0;
        }
        String ordered1 = "(" + x1 + "," + y1 + ")";
        String ordered2 = "(" + x2 + "," + y2 + ")";
        steps = "The ordered pairs you put in are: " + ordered1 + " and " + ordered2 +
                "<br /> <br />The slope of the line is: <br /> <br />m = " + slope + " <br /> <br />The first step is to " +
                "remember our slope formula as: <br /> <br />m = (y<sub><small>2</small></sub> - y<sub><small>1</small></sub>) / " +
                "(x<sub><small>2</small></sub> - x<sub><small>1</small></sub>). <br /> <br />The next step is " +
                "to substitute each variable with our ordered pairs. Lets do this one at a time: " +
                "<br /> <br /> y<sub><small>2</small></sub> = " + y2 + " which goes into our formula as the y<sub><small>2</small></sub> variable: <br /> <br />" +
                "m = (" + y2 + " - y<sub><small>1</small></sub>) / (x<sub><small>2</small></sub> - x<sub><small>1</small></sub>). <br /> <br />Now we plug " +
                "in the value for y<sub><small>1</small></sub> which is " +
                y1 + ": <br /> <br />m = (" + y2 + " - "+ y1 + ") / (x<sub><small>2</small></sub> - x<sub><small>1</small></sub>). <br /> <br />Now we can put the remaining " +
                "two variables in, x<sub><small>1</small></sub> and x<sub><small>2</small></sub>, which are: " + x1 + " and " + x2 + ": <br /> <br />" +
                "m = (" + y2 + " - "+ y1 + ") / (" + x2 + " - " + x1 + "). <br /><br />Now we simplify, " +
                "subtracting above and below the divide sign: " + (y2 - y1) + " / " + (x2 - x1) + "<br /> <br />" +
                "And now we divide to give us our answer: <br /> <br />m = " + slope + "<br />";
        stepbystep.setText(toHTML(steps));
        onResume();
    }

    /**
     * Listener for the Reset button
     * Sets the current stepbystep text to null
     * @param view the reset button that was pressed
     */
    public void ResetText(View view){
        TextView stepbystep = (TextView)findViewById(R.id.stepbystep);
        stepbystep.setText(null);
        onResume();
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

    /**
     * Event handler for closing keyboard when users touches outside an edittext
     * @param event the touch event sent by the device
     * @return true if outside the edittext, false otherwise
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
}
