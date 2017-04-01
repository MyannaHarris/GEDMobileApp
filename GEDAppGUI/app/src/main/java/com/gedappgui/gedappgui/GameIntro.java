/*
 * GameIntro.java
 *
 * Game intro page activity
 *
 * View that hosts the game intro
 * which gives the student instructions on how to play the game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 3-31-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameIntro extends AppCompatActivity {
    //globals for current concept id, lesson id and redo id
    private int conceptID;
    private int lessonID;
    private int redo;

    RelativeLayout layout;
    GridLayout grid;
    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

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

        setContentView(R.layout.activity_game_intro);

        //gets all the current db information (lesson id, concept id, redo)
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        redo = mIntent.getIntExtra("redoComplete", 0);

        // Allow homeAsUpIndicator (back arrow) to display on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get next_activity value from intent to decide next activity after game
        nextActivity = mIntent.getIntExtra("next_activity", 1);

        // Database to get game instructions
        dbHelper = new DatabaseHelper(this);

        layout = (RelativeLayout) findViewById(R.id.gameIntro);
        grid = (GridLayout) findViewById(R.id.gameIntro_gridView);

        //gets the game instructions views
        TextView instructions = (TextView) findViewById(R.id.instructions);
        TextView introduction = (TextView) findViewById(R.id.welcome_message);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcome);
        TextView instructionLabel = (TextView) findViewById(R.id.instruction_label);

        //creates the imageviews for the game instructions
        ImageView instructionImage1 = new ImageView(this);
        ImageView instructionImage2 = new ImageView(this);
        instructionImage1.setId(R.id.instruct1);
        instructionImage2.setId(R.id.instruct2);

        //gets the game instructions strings from the db
        String intro = dbHelper.selectIntroduction(lessonID);
        String instruct = dbHelper.selectInstructions(lessonID);
        String name = dbHelper.selectLessonTitle(lessonID);
        String welcome = "";


        //check for first lesson, special case
        if(lessonID == 1){
            welcome = "Welcome to " + name + " Game!";
        }
        else{
            welcome = "Welcome to the " + name + " Game!";
        }

        //sets the views for the correct instructions
        instructions.setText(instruct);
        welcomeMessage.setText(welcome);
        introduction.setText(intro);

        // Get dimensions of screen to make text size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        // Set dynamic size of text for instructions and button
        instructions.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        welcomeMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));
        introduction.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        instructionLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        //checks to see if there are images for the game instructions
        if(dbHelper.gameIntroHasImages(lessonID)) {
            //if there are instruction images, add the first one into the grid
            ArrayList<String> pics = dbHelper.selectGameIntroPics(lessonID);
            float newWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float)(width/2.25), getResources().getDisplayMetrics());
            float newHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float)(height/2.25), getResources().getDisplayMetrics());
            int viewGravity = Gravity.FILL_HORIZONTAL | Gravity.CENTER_VERTICAL;

            Bitmap image1 = getFromAssets(pics.get(0));
            instructionImage1.setImageBitmap(image1);

            GridLayout.Spec col1 = GridLayout.spec(0, 1);
            GridLayout.LayoutParams gridLayoutParam1 = new GridLayout.LayoutParams(GridLayout.spec(0, 1), col1);
            gridLayoutParam1.setMargins(20, 0, 20, 0);

            gridLayoutParam1.setGravity(viewGravity);
            grid.addView(instructionImage1, gridLayoutParam1);

            //if there are to images, add the second to the grid
            if(pics.size() == 2) {
                Bitmap image2 = getFromAssets(pics.get(1));
                instructionImage2.setImageBitmap(image2);

                GridLayout.Spec col2 = GridLayout.spec(1, 1);
                GridLayout.LayoutParams gridLayoutParam2 = new GridLayout.LayoutParams(GridLayout.spec(0, 1), col2);
                gridLayoutParam2.setMargins(20, 0, 20, 0);

                gridLayoutParam2.setGravity(viewGravity);
                grid.addView(instructionImage2, gridLayoutParam2);

                //set the max width and height for the images
                instructionImage2.setMaxWidth((int)newWidth);
                instructionImage2.setMaxHeight((int)newHeight);
                instructionImage2.setAdjustViewBounds(true);
                instructionImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }

            //set the max width and height for the images
            instructionImage1.setMaxWidth((int)newWidth);
            instructionImage1.setMaxHeight((int)newHeight);
            instructionImage1.setAdjustViewBounds(true);
            instructionImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        Button startButton = (Button) findViewById(R.id.play_button);
        startButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
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
            in = assetManager.open("game_instruction_pics/" + imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //creates a bitmap from the file got from assets
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
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
     * Called when Play button clicked
     * Goes to the game activity
     * intent.putExtra("next_activity", nextActivity);
     *   = sends nextActivity to tell game whether to go to question or play next
     * @param view current view
     */
    public void goToGame(View view) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.putExtra("redoComplete", redo);
        startActivity(intent);
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
        }
        return super.onOptionsItemSelected(item);
    }
}

