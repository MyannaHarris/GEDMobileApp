/*
 * GeoAssist.java
 *
 * Geometry tool activity
 *
 * Shows the menu for selecting shapes and displays info based on the shape selected
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
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GeoAssist extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //creates the global array of summaries describing select geometric shapes
    private String[] summaries = {
            "\n\nA circle is a closed set of points that are the same distance from a certain point. " +
                    "The area is the space inside of a circle. The diameter is a line segment with endpoints" +
                    " on the edges of the circle where the line passes through the center. The radius is a line segment" +
                    " that connects the center of the circle to any point on the circle. The constant value pi" +
                    " is often used to perform calculations with circles. In the test you will use the value 3.14.",
            "\n\nA triangle is a closed three-sided figure. A triangle can be classified depending on its " +
                    "sides and angles: \n\nEquilateral Triangle - All sides are equal in length and all angles " +
                    "are 60 degrees \n\nIsoceles Triangle - Two sides are equal in length causing the angles opposite " +
                    "these sides to be equal \n\nScalene Triangle - No sides are equal and no angles are equal \n\n" +
                    "Right Triangle - One angle measures 90 degrees \n\nAcute Triangle - All angles measure less than 90 degrees" +
                    "\n\nObtuse Triangle - One angle is greater than 90 degrees",
            "\n\nA rectangle is four-sided figure with four right angles. The opposite sides are equal in length",
            "\n\nA square is a special type of rectangle with four equal sides and four angles at 90 degrees.",
            "\n\nA parallelogram is a four-sided figure whose sides are parallel and the same length. Its opposite " +
                    "angles are equal.",
            "\n\nA trapezoid is a four-sided figure with one pair of parallel sides",
            "\n\nA cone is a 3D figure with a circular base and a pointed top",
            "\n\nA pyramid is a 3D figure with a Square base and a pointed top",
            "\n\nA cylinder is a 3D figure with a circular base and a circular top"

    };

    //creates the global array of example describing how to calculate certain measurements of
    // select geometric shapes
    private String[] examples ={
            "\nCircumference = pi * diameter \n\nArea = pi * radius^2 \n\ninterior angle sum = 360 degrees\n\n",
            "\nPerimeter = side1 + side2 +side3 \n\nArea = 1/2 * base * height \n\nPythagorean Relationship - " +
                    "\na^2 + b^2 = c^2; a and b are both legs and c is the hypotenuse of a right triangle \n\n" +
                    "interior angle sum = 360 degrees\n\n",
            "\nPerimeter = 2 * length + 2 * width \n\nArea = length * width\n\ninterior angle sum = 360 degrees\n\n",
            "\nPerimeter = side * 4 \n\nArea = side * side\n\ninterior angle sum = 360 degrees\n\n",
            "\nPerimeter = 2 * length + 2 * width \n\nArea = base * height \n\ninterior angle sum = 360 degrees\n\n",
            "\nPerimeter = side1 + side2 + side3 + side4\n\nArea = 1/2 * height(base1 + base2)\n" +
                    "\n" +
                    "interior angle sum = 360 degrees\n" +
                    "\n",
            "\nVolume = 1/3 * pi * radius^2 * Height\n\nSurface Area = pi * r * Slant Height + pi * radius^2",
            "\nVolume = 1/3 * (base side)^2 * Height\n\nSurface Area = 2 * base * side + base^2",
            "\nVolume = pi * r^2 * Height\n\nSurface Area = 2 * pi * r^2 + height * (2 * pi * radius)"
    };

    //creates the global array of picture of select geometric shapes
    private int[] pics = {
            R.drawable.radius,
            R.drawable.triangletypes,
            R.drawable.rectangle,
            R.drawable.square,
            R.drawable.parallel,
            R.drawable.trapezoid,
            R.drawable.cone,
            R.drawable.pyramid,
            R.drawable.cylinder
    };

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_assist);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Stop spinner from bringing top bar down
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //creates a spinner for selection of geometric shapes
        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        //creates the list of possible geometric shapes available in the spinner
        List<String> categories = new ArrayList<String>();
        categories.add("Circle");
        categories.add("Triangle");
        categories.add("Rectangle");
        categories.add("Square");
        categories.add("Parallelogram");
        categories.add("Trapezoid");
        categories.add("Cone");
        categories.add("Pyramid");
        categories.add("Cylinder");

        // Get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_larger_text, categories){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView)
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v instanceof TextView)
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
                return v;
            }
        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        //gives an achievement if the user uses a tool for the first time
        Intent achievement = new Intent(this, AchievementPopUp.class);
        achievement.putExtra("achievementID", 7);
        startActivity(achievement);
    }

    /**
     * sets the image, example and summary to the texts that correlate
     * with the shape at the given position
     * @param parent parents adapterView
     * @param view current view
     * @param position position of item
     * @param id id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //sets up the texts views and image view
        TextView summary = (TextView)findViewById(R.id.summary);
        TextView example = (TextView)findViewById(R.id.shape_examples);
        ImageView img = (ImageView) findViewById(R.id.shapepicture);

        //sets the image, example and summary to the texts that correlate with the shape at the
        // given position
        summary.setText(summaries[position]);
        example.setText(examples[position]);
        img.setImageResource(pics[position]);

    }

    /**
     * When nothing is selected, do nothing
     * @param parent the parent adapter view
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
