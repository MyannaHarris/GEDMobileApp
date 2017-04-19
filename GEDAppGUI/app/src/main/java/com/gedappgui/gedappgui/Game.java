/*
 * Game.java
 *
 * Game page activity
 *
 * View that hosts the game that was chosen or that corresponds with the current lesson
 * Helps students solidify their knowledge of the lesson
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by jasminejans on 10/29/16.
 *
 * Last Edit: 4-10-17
 *
 */

package com.gedappgui.gedappgui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import java.util.ArrayList;

public class Game extends AppCompatActivity {
    // Next intent information
    private int conceptID;
    private int lessonID;
    private int redo;
    private int totalRetries;
    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Game objects to load different games
    private BucketGameView bucketGameView;
    private MatchGameView matchGameView;
    private PictureGameView pictureGameView;
    private ChemistryGameView chemistryGameView;
    private OrderingGameView orderingGameView;
    private MadlibGameView madlibGameView;

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

        // Set default content for no game present
        setContentView(R.layout.activity_game);

        // Get information from intent
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        redo = mIntent.getIntExtra("redoComplete", 0);
        totalRetries = mIntent.getIntExtra("totalRetries", 0);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get next_activity value from intent to decide next activity after game
        nextActivity = mIntent.getIntExtra("next_activity", 1);

        // Database to get what game, lists of numbers needed and problems
        dbHelper = new DatabaseHelper(this);

        // Get dimensions of screen to make text size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //checks for which template to use, aka which kind of game
        if (dbHelper.selectGameTemplate(lessonID).equals("bucket_game")) {

            ArrayList<ArrayList<String>> gameQuestions;

            if (nextActivity == 1) {
                // Get content for bucket game from database
                gameQuestions = dbHelper.selectInfiniteBucketGameInput(lessonID);
            } else {
                // Get content for bucket game from database
                gameQuestions = dbHelper.selectBucketGameInput(lessonID);
            }

            // Create game object
            bucketGameView = new BucketGameView(this, width, height, gameQuestions,
                    conceptID, lessonID, nextActivity);

            // Set the game as the view
            setContentView(bucketGameView);

        } else if (dbHelper.selectGameTemplate(lessonID).equals("match_game")) {

            ArrayList<ArrayList<String>> texts;

            if (nextActivity == 1) {
                // Get content for bucket game from database
                texts = dbHelper.selectInfiniteMatchGameInput(lessonID);
            } else {
                // Get content for bucket game from database
                texts = dbHelper.selectMatchGameInput(lessonID);
            }

            // Get reference to current activity
            Activity activity = (Activity)this;

            // Create game object
            matchGameView = new MatchGameView(this, activity, texts, conceptID,
                    lessonID, nextActivity, width, height);

            // Set the game as the view
            setContentView(matchGameView);
        } else if (dbHelper.selectGameTemplate(lessonID).equals("chemistry_game")) {

            ArrayList<ArrayList<String>> texts;

            if (nextActivity == 1) {
                // Get content for bucket game from database
                texts = dbHelper.selectInfiniteChemistryGameInput(lessonID);
            } else {
                // Get content for bucket game from database
                texts = dbHelper.selectChemistryGameInput(lessonID);
            }

            // Create game object
            chemistryGameView = new ChemistryGameView(this, texts, conceptID,
                    lessonID, nextActivity, width, height);

            // Set the game as the view
            setContentView(chemistryGameView);
        }else if (dbHelper.selectGameTemplate(lessonID).equals("madlib_game")){

            // Get reference to current activity
            Activity activity = (Activity)this;
            ArrayList<ArrayList<ArrayList<String>>> input = new ArrayList<>();

            if(nextActivity == 1){
                input = dbHelper.selectInfiniteMadlibInput(lessonID);
            }
            else{
                input = dbHelper.selectMadlibInput(lessonID);
            }

            ScrollView scroll = new ScrollView(this);
            scroll.setFocusableInTouchMode(true);
            scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

            madlibGameView = new MadlibGameView(this, activity, (input.get(0)),  input.get(2),
                    input.get(3),  input.get(1), input.get(4), conceptID, lessonID,
                    nextActivity, width, height, scroll);

            scroll.addView(madlibGameView);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            // Set the game as the view
            setContentView(scroll);
        } else if (dbHelper.selectGameTemplate(lessonID).equals("picture_game")){

            // Get content for picture game from database
            String pass_string = dbHelper.selectPicGameInput(lessonID);

            // Create game object
            pictureGameView = new PictureGameView(this,conceptID,lessonID,nextActivity,pass_string);

            // Set the game as the view
            setContentView(pictureGameView);
        } else if (dbHelper.selectGameTemplate(lessonID).equals("order_game")) {

            ArrayList<ArrayList<String>> texts;

            if (nextActivity == 1) {
                // Get content for bucket game from database
                texts = dbHelper.selectInfiniteOrderGameInput(lessonID);
            } else {
                // Get content for bucket game from database
                texts = dbHelper.selectOrderGameInput(lessonID);
            }

            // Create game object
            orderingGameView = new OrderingGameView(this, texts, conceptID,
                    lessonID, nextActivity, width, height);

            // Set the game as the view
            setContentView(orderingGameView);
        } else{
            // Made up bucket game content to run when no game is specified

            ArrayList<ArrayList<String>> gameQuestions = new ArrayList<ArrayList<String>>();

            ArrayList<String> texts = new ArrayList<String>();
            texts.add("1");
            texts.add("2");
            texts.add("3");
            texts.add("4");
            texts.add("5");
            texts.add("7");
            ArrayList<String> answers = new ArrayList<String>();
            String question = "4 * (x-1) = 24";
            answers.add(question);
            answers.add("7");

            gameQuestions.add(texts);
            gameQuestions.add(answers);

            texts = new ArrayList<String>();
            texts.add("11");
            texts.add("7");
            texts.add("8");
            texts.add("9");
            texts.add("10");
            texts.add("5");
            answers = new ArrayList<String>();
            question = "1 + 7x = 36";
            answers.add(question);
            answers.add("5");

            gameQuestions.add(texts);
            gameQuestions.add(answers);

            // Create game object
            bucketGameView = new BucketGameView(this, width, height, gameQuestions,
                    conceptID, lessonID, nextActivity);

            // Set the game as the view
            setContentView(bucketGameView);
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
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        // Resumes bucket game since it has a game loop
        if (bucketGameView != null)
            bucketGameView.resume();
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
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    /**
     * Pauses this activity when it is left by a new activity being put on top of it in the stack
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Pauses bucket game since it uses a game loop
        if (bucketGameView != null)
            bucketGameView.pause();
    }

    /**
     * Used when the page does not have a game to load
     *
     * Goes to the GameEnd activity
     * Called when the game is over
     * intent.putExtra("next_activity", nextActivity);
     *   = sends nextActivity to tell game whether to go to question or play next
     * @param view The current view calling the method
     */
    public void goToGameEnd(View view) {
        Intent intent = new Intent(this, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.putExtra("redoComplete", redo);
        intent.putExtra("totalRetries",totalRetries);
        startActivity(intent);
    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}
