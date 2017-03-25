/*
 * FractionToDecimalTool.java
 *
 * FractionToDecimalTool activity
 *
 * View that will host the Fraction to Decimal tool
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-27-16
 *
 */
package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class FractionToDecimalTool extends AppCompatActivity {


    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraction_to_decimal_tool);
        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //listener for Next action on keyboard
        EditText fraction_text = (EditText)findViewById(R.id.FractionInput);
        fraction_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //evaluates answer and closes keyboard
                    View view = findViewById(R.id.submitButton);
                    evaluateText(view);
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        //listener for Done action on keyboard
        EditText decimal_text = (EditText)findViewById(R.id.DecimalInput);
        decimal_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view =  findViewById(R.id.submitButton);
                    evaluateText(view);
                }
                return false;
            }
        });

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);
    }

    /**
     * Button listener for submit,
     * Calls helper functions for evaluating inputs of fraction and decimal
     * and setting the answers to the correct outputs
     * @param view the button that was pressed
     */
    public void evaluateText(View view){
        EditText fraction_text = (EditText)findViewById(R.id.FractionInput);
        String fraction = fraction_text.getText().toString();
        TextView fraction_answer = (TextView)findViewById(R.id.FractionAnswer);
        fraction_answer.setText(evaluateFraction(fraction));

        EditText decimal_text = (EditText)findViewById(R.id.DecimalInput);
        String decimal = decimal_text.getText().toString();
        TextView decimal_answer = (TextView)findViewById(R.id.DecimalAnswer);
        decimal_answer.setText(evaluateDecimal(decimal));

        //hides keyboard after evaluating
        onResume();


    }

    /**
     * Function for converting the decimal input to a fraction.
     * Checks for validity of the input and then calculates the
     * fraction using the gcd of the numerator and denominator.
     * Returns a string object, the answer if valid and an error string if invalid.
     * @param decimal the string representation of the decimal input
     * @return an error string or the reduced fractional form of the decimal as a string
     */
    public static String evaluateDecimal(String decimal) {
        if (decimal.equals("")){
            return "";
        }
        if (decimal.contains(".")){
            String[] top = decimal.split("\\.");
            if (top.length > 2){
                return "Invalid input, Example inputs: 0.33, 1.89";
            }
            //converts right side of decimal to an integer by a power of ten
            int bottom_int = (int) Math.pow((double) 10, (double) (top[1].length()));
            //converts left side of decimal to integer
            int top_int = Integer.parseInt(top[0] + top[1]);
            //finds GCD of top and bottom
            int gcd = gcd_convert(top_int, bottom_int);
            //reduces and returns fraction
            return (Integer.toString(top_int/gcd) + "/" + Integer.toString(bottom_int/gcd));
        }

        return "Invalid input, Example inputs: 0.33, 1.89";
    }

    /**
     * Helper function for finding the greatest common divisor of the numerator and denominator
     * @param top the top portion of the decimal fraction
     * @param bottom the bottom portion of the decimal fraction
     * @return the greatest common divisor of top and bottom
     */
    public static int gcd_convert(int top, int bottom){
        if (bottom == 0){
            return top;
        }
        return gcd_convert(bottom, top % bottom);
    }

    /**
     * Function for converting fraction input to decimal output
     * checks for validity of the fraction before converting into decimal
     * outputs error strings if the string is invalid or the answer as a
     * string object.
     * @param fraction the string representation of the fraction input
     * @return an error message or the decimal form of the fraction as a string
     */
    public static String evaluateFraction(String fraction) {
        double numerator;
        double denominator;
        if (fraction.equals("")){
            return "";
        }
        //checks to see if the input is a valid fraction before evaluating
        if (fraction.contains("/")){
            String[] ratio = fraction.split("/");
            if (ratio.length > 2){
                return "Invalid input, Example inputs: 1/2, 13/7";
            }
            //checks to see if the strings are numeric, returns invalid otherwise
            try {
                numerator = Double.parseDouble(ratio[0]);
                denominator = Double.parseDouble(ratio[1]);
                if (denominator == 0.0){
                    return "Invalid input, cannot divide by zero";
                }
            }
            catch(NumberFormatException f){
                return "Invalid input, Example inputs: 1/2, 13/7";
            }
            double answer = numerator / denominator;
            return Double.toString(answer);
        }
        return "Invalid input, Example inputs: 1/2, 13/7";
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
}
