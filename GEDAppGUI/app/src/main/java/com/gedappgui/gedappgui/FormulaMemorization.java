/*
 * FormulaMemorization.java
 *
 * Formula Memorization tool activity
 *
 * Holds all GED Math formulas and has buttons to hid parts of a formula to allow for memorization
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 1-22-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FormulaMemorization extends AppCompatActivity {

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula_memorization);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initializeformulas();

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);
    }

    /**
     * Initializes the formula strings
     */
    protected void initializeformulas(){
        TextView areaformulas = (TextView)findViewById(R.id.areaformulas);
        TextView perimeterformulas = (TextView)findViewById(R.id.perimeterformulas);
        TextView volumeformulas = (TextView)findViewById(R.id.volumeformulas);
        TextView wpformulas = (TextView)findViewById(R.id.wpformulas);

        String areas = "Square:\nArea = side * side \n \nRectangle:\nArea = length * width \n \n" +
                "Parallelogram:\nArea = base * height \n \nTriangle:\nArea = 1/2 * base * height \n \n" +
                "Trapezoid:\nArea = 1/2 * (base1 + base2) * height \n \nCircle:\nArea = pi * radius<sup><small>2</small></sup>";

        areaformulas.setText(toHTML(areas));

        String perimeters = "Square:\nPerimeter = 4 * side \n \nRectangle:\nPerimeter = 2 * length + 2 * width" +
                "\n \nTriangle:\nPerimeter = side1 + side2 + side3 \n \nCircle:\nCircumference = pi * diameter";

        perimeterformulas.setText(toHTML(perimeters));

        String volumes = "Cube:\nVolume = edge<sup><small>3</small></sup> \n \nRectangular Solid:\nVolume = length * width * height" +
                "\n \nSquare Pyramid:\nVolume = 1/3 * (base edge)<sup><small>2</small></sup> * height \n \nCylinder:\nVolume = pi * radius<sup><small>2</small></sup> * height" +
                "\n \nCone:\nVolume = 1/3 * pi * radius<sup><small>2</small></sup> * height";

        volumeformulas.setText(toHTML(volumes));

        String wordprobformulas = "Pthagorean:\nleg1<sup><small>2</small></sup> + leg2<sup><small>2</small></sup> = hypotenuse<sup><small>2</small></sup>\n \nSimple Interest" +
                ":\ninterest = principal * rate * time \n \nDistance:\ndistance = rate * time \n \n" +
                "Total Cost:\ntotal cost = (number of units) * (price per unit)";

        wpformulas.setText(toHTML(wordprobformulas));

    }

    /**
     * Hides one part of formulas from view
     * @param view the button that was pressed
     */
    public void hideonepart(View view){
        TextView areaformulas = (TextView)findViewById(R.id.areaformulas);
        TextView perimeterformulas = (TextView)findViewById(R.id.perimeterformulas);
        TextView volumeformulas = (TextView)findViewById(R.id.volumeformulas);
        TextView wpformulas = (TextView)findViewById(R.id.wpformulas);

        String areas = "Square:\nArea = ? * side \n \nRectangle:\nArea = ? * width \n \n" +
                "Parallelogram:\nArea = ? * height \n \nTriangle:\nArea = 1/2 * base * ? \n \n" +
                "Trapezoid:\nArea = 1/2 * (base1 + ?) * height \n \nCircle:\nArea = pi * ?<sup><small>2</small></sup>";

        areaformulas.setText(toHTML(areas));

        String perimeters = "Square:\nPerimeter = ? * side \n \nRectangle:\nPerimeter = 2 * ? + 2 * width" +
                "\n \nTriangle:\nPerimeter = ? + side2 + side3 \n \nCircle:\nCircumference = pi * ?";

        perimeterformulas.setText(toHTML(perimeters));

        String volumes = "Cube:\nVolume = ?<sup><small>3</small></sup> \n \nRectangular Solid:\nVolume = length * ? * height" +
                "\n \nSquare Pyramid:\nVolume = 1/3 * (?)<sup><small>2</small></sup> * height \n \nCylinder:\nVolume = pi * radius<sup><small>2</small></sup> * ?" +
                "\n \nCone:\nVolume = 1/3 * pi * radius<sup><small>2</small></sup> * ?";

        volumeformulas.setText(toHTML(volumes));

        String wordprobformulas = "Pthagorean:\nleg1<sup><small>2</small></sup> + ?<sup><small>2</small></sup> = hypotenuse<sup><small>2</small></sup>\n \nSimple Interest" +
                ":\ninterest = principal * ? * time \n \nDistance:\ndistance = rate × ? \n \n" +
                "Total Cost:\ntotal cost = (number of units) * (?)";

        wpformulas.setText(toHTML(wordprobformulas));
    }

    /**
     * Hides two parts of formulas from view
     * @param view the button that was pressed
     */
    public void hidetwoparts(View view){
        TextView areaformulas = (TextView)findViewById(R.id.areaformulas);
        TextView perimeterformulas = (TextView)findViewById(R.id.perimeterformulas);
        TextView volumeformulas = (TextView)findViewById(R.id.volumeformulas);
        TextView wpformulas = (TextView)findViewById(R.id.wpformulas);

        String areas = "Square:\nArea = ? * ? \n \nRectangle:\nArea = ? * ? \n \n" +
                "Parallelogram:\nArea = ? * ? \n \nTriangle:\nArea = ? * base * ? \n \n" +
                "Trapezoid:\nArea = 1/2 * (base1 + ?) * ? \n \nCircle:\nArea = ? * ?<sup><small>2</small></sup>";

        areaformulas.setText(toHTML(areas));

        String perimeters = "Square:\nPerimeter = ? * ? \n \nRectangle:\nPerimeter = 2 * ? + 2 * ?" +
                "\n \nTriangle:\nPerimeter = ? + side2 + ? \n \nCircle:\nCircumference = ? * ?";

        perimeterformulas.setText(toHTML(perimeters));

        String volumes = "Cube:\nVolume = ?<sup><small>?</small></sup> \n \nRectangular Solid:\nVolume = ? * ? * height" +
                "\n \nSquare Pyramid:\nVolume = ? * (?)<sup><small>2</small></sup> * height \n \nCylinder:\nVolume = pi * ?<sup><small>2</small></sup> * ?" +
                "\n \nCone:\nVolume = 1/3 * ? * radius^2 * ?";

        volumeformulas.setText(toHTML(volumes));

        String wordprobformulas = "Pthagorean:\n?<sup><small>2</small></sup> + ?<sup><small>2</small></sup> = hypotenuse<sup><small>2</small></sup>\n \nSimple Interest" +
                ":\ninterest = ? * ? * time \n \nDistance:\ndistance = ? * ? \n \n" +
                "Total Cost:\ntotal cost = (?) * (?)";

        wpformulas.setText(toHTML(wordprobformulas));
    }

    /**
     * Return formulas to original state
     * @param view the button that was pressed
     */
    public void hidenone(View view){
        initializeformulas();
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
