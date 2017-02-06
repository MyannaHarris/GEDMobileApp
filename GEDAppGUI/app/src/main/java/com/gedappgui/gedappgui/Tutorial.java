/*
        * Tutorial.java
        *
        * Tutorial activity
        *
        * View that will host the Tutorial
        *
        * Worked on by:
        * Myanna Harris
        * Kristina Spring
        * Jasmine Jans
        * Jimmy Sherman
        *
        * Last Edit: 2-1-17
        *
        */
package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends AppCompatActivity {

    private String[] prompts = {
            "Welcome to the tutorial! Use the forward and back buttons to navigate the tutorial. Hit exit to leave at any time.",
            "The Home Screen is the first thing you will see when opening the app. You can use this screen to access all other features",
            "By clicking on the dragon, you can go directly to the dragon accessory screen",
            "Here you can choose accessories to put on the sprite by clicking on them. You can also remove items by clicking on the left image",
            "Click on the tools button on the home screen to go to the tools section",
            "Here you can access math tools that will help you with memorization of math rules and solving GED questions",
            "Click on the play button to go to the games screen",
            "Here you can play the games that you have unlocked by completing lessons",
            "Click on the achievements button to see what achievements you have earned",
            "You can click the achievements to see the description of the achievements you have earned",
            "Click on learn to go to the concepts screen",
            "On this screen you can select from 4 concepts each holding 6 lessons",
            "After clicking on a concept, you can select the most recent lesson you have unlocked or any past lesson",
            "The first screen in a lesson is a summary that will give you an overview of the lesson",
            "The next screen has a video, picture, and a short tip on the material in the lesson, we suggest you go through all 3 of these parts before proceeding",
            "This screen holds two example problems that are similar to what will be asked later in the lesson",
            "The next few screens will have instructions on how to play a short game and the actual game itself",
            "After the game, you will be asked some questions on the material similar to questions asked on the GED",
            "After you answer enough questions correctly, you will be able to select an accessory for your sprite and move on",
            "If you want to go to a previous lesson, you can select where you would like to start in that lesson",
            "Clicking continue lesson on the home screen will take you to where you left off on the current lesson",
            "Clicking on settings will bring you to the settings screen",
            "Here you can set a notification, change your name, or mute sounds",
            "Thanks for using our app! You can access this tutorial at anytime in Tools! Have fun and good luck!"
    };

    private int[] tutorial_pics = {
            R.drawable.sprite_dragon,
            R.drawable.home_screen,
            R.drawable.home_screen_sprite,
            R.drawable.closet,
            R.drawable.home_screen_tools,
            R.drawable.tools,
            R.drawable.home_screen_play,
            R.drawable.play,
            R.drawable.home_screen_achievements,
            R.drawable.achievements_tutorial,
            R.drawable.home_screen_learn,
            R.drawable.tutorial_concepts,
            R.drawable.tutorial_lessons,
            R.drawable.tutorial_summary,
            R.drawable.tutorial_steps,
            R.drawable.tutorial_example,
            R.drawable.tutorial_game,
            R.drawable.tutorial_questions,
            R.drawable.tutorial_success,
            R.drawable.revisit,
            R.drawable.home_screen_continue,
            R.drawable.settings,
            R.drawable.settings_screen,
            R.drawable.sprite_dragon
    };

    //index for tutorial points
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        //disables back button at beginning
        checkButtons();

    }

    /*
     * Exits the tutorial
     */
    public void exitTutorial(View view){
        finish();
    }

    /*
     * Goes back one image and text
     */
    public void goBack(View view){
        TextView prompt = (TextView)findViewById(R.id.prompt);
        ImageView promptImg = (ImageView) findViewById(R.id.prompt_pic);
        current--;
        checkButtons();
        prompt.setText(prompts[current]);
        promptImg.setImageResource(tutorial_pics[current]);
    }

    /*
     * Goes forward one image and text
     */
    public void goForward(View view){
        TextView prompt = (TextView)findViewById(R.id.prompt);
        ImageView promptImg = (ImageView) findViewById(R.id.prompt_pic);
        current++;
        checkButtons();
        prompt.setText(prompts[current]);
        promptImg.setImageResource(tutorial_pics[current]);
    }

    /*
     * Disables back button if at beginning, disables forward button if at end
     */
    public void checkButtons(){
        Button backbtn = (Button)findViewById(R.id.tutorial_back);
        Button forwardbtn = (Button)findViewById(R.id.tutorial_forward);

        if (current < 1)
            backbtn.setEnabled(false);
        else
            backbtn.setEnabled(true);
        if (current > 22)
            forwardbtn.setEnabled(false);
        else
            forwardbtn.setEnabled(true);
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
}
