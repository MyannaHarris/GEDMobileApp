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
        * Last Edit: 3-20-17
        *
        */
package com.gedappgui.gedappgui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends AppCompatActivity {

    // Set prompts for each page of tutorial
    private String[] prompts = {
            "Welcome to the tutorial! Use the forward and back buttons to navigate the tutorial. Hit exit to leave at any time.",
            "The Home Screen is the first thing you will see when opening the app. You can use this screen to access all other features.",
            "By clicking on the dragon, you can go directly to the dragon's lair.",
            "Here you can choose accessories to put on the sprite by dragging them onto the dragon. You can also remove accessories by clicking on them again.",
            "Clicking continue lesson on the home screen will take you to where you left off on the current lesson.",
            "Click on the achievements button to see what achievements you have earned. You can view all of the achievements you have earned on this screen.",
            "Click on the tools button on the home screen to go to the tools section. You can access math tools that will help you with memorization of math rules and solving GED questions on this screen.",
            "Click on the classroom button to go to the concepts screen.",
            "On this screen you can select from 4 concepts each holding 6 lessons.",
            "Each lesson will be unlocked as you complete more lessons. Initially, you will only have access to the first lesson",
            "The first screen in a lesson is a summary that will give you an overview of the lesson.",
            "The next screen has a video, picture, and a short tip on the material in the lesson, we suggest you go through all 3 of these parts before proceeding.",
            "You can view two example problems on this screen that are similar to what will be asked later in the lesson.",
            "The next few screens will have instructions on how to play a short game and the actual game itself.",
            "After the game, you will be asked some questions on the material similar to questions asked on the GED.",
            "After you answer enough questions correctly, you will be able to select an accessory for your sprite and move on.",
            "Click on the arcade button to go to the games screen. You can play the games that you have unlocked by completing lessons on this screen.",
            "Thanks for using our app! You can access this tutorial at anytime in Tools! Have fun and good luck!",
            "Thanks for using our app! You can access this tutorial at anytime in Tools! Have fun and good luck!"
    };

    private String[] headings = {
            "Tutorial",
            "Home Screen",
            "Dragon Lair",
            "Dragon Lair",
            "Continuing lessons",
            "Achievements",
            "Tools",
            "Classroom",
            "Concepts Selection",
            "Lesson Selection",
            "Summary",
            "Information",
            "Examples",
            "Instructions",
            "Questions",
            "Success!",
            "Arcade",
            "Have fun!",
            "Have fun!"

    };


    // Set pictures for each page of tutorial
    private int[] tutorial_pics = {
            R.drawable.sprite_dragon,
            R.drawable.home_screen,
            R.drawable.home_screen_sprite,
            R.drawable.closet,
            R.drawable.home_screen_continue,
            R.drawable.home_screen_achievements,

            R.drawable.home_screen_tools,
            R.drawable.home_screen_learn,
            R.drawable.tutorial_concepts,
            R.drawable.tutorial_lessons,
            R.drawable.tutorial_summary,
            R.drawable.tutorial_steps,
            R.drawable.tutorial_example,
            R.drawable.tutorial_game,
            R.drawable.tutorial_questions,
            R.drawable.tutorial_success,
            R.drawable.home_screen_play,
            R.drawable.sprite_dragon,
            R.drawable.sprite_dragon
    };

    //index for tutorial pictures and text
    private int current = 0;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;



        // Set dynamic sizes for the buttons on tutorial
        Button backbtn = (Button)findViewById(R.id.tutorial_back);
        backbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        Button forwardbtn = (Button)findViewById(R.id.tutorial_forward);
        forwardbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        Button exitbtn = (Button)findViewById(R.id.tutorial_exit);
        exitbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        ImageView iview = (ImageView)findViewById(R.id.prompt_pic);


        ViewGroup.LayoutParams paramsexit = exitbtn.getLayoutParams();
        ViewGroup.LayoutParams paramsforward = forwardbtn.getLayoutParams();
        ViewGroup.LayoutParams paramsback = backbtn.getLayoutParams();
        ViewGroup.LayoutParams paramsiview = iview.getLayoutParams();

        paramsiview.height = height / 2;
        paramsiview.width = width / 2;

        paramsexit.height = (height/8);
        paramsexit.width = (width/4);
        paramsforward.height = (height/8);
        paramsforward.width = (width/4);
        paramsback.height = (height/8);
        paramsback.width = (width/4);

        exitbtn.setLayoutParams(paramsexit);
        forwardbtn.setLayoutParams(paramsforward);
        backbtn.setLayoutParams(paramsback);
        iview.setLayoutParams(paramsiview);

        TextView prompt = (TextView)findViewById(R.id.prompt);
        prompt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/40));

        Button backer = (Button)findViewById(R.id.tutorial_back);
        backer.setVisibility(View.GONE);

    }

    /**
     * Exits the tutorial when the user chooses to do so
     * @param view current view
     */
    public void exitTutorial(View view){
        finish();
    }

    /**
     * Goes back one image and text in the tutorial
     * @param view current view
     */
    public void goBack(View view){
        Button backer = (Button)findViewById(R.id.tutorial_back);
        if (current > 0) {
            TextView prompt = (TextView) findViewById(R.id.prompt);
            ImageView promptImg = (ImageView) findViewById(R.id.prompt_pic);
            current--;
            prompt.setText(prompts[current]);
            promptImg.setImageResource(tutorial_pics[current]);
            this.setTitle(headings[current]);
        }
        if (current == 0){
            backer.setVisibility(View.GONE);
        }
        else{
            backer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Goes forward one image and text
     * @param view current view
     */
    public void goForward(View view){
        TextView prompt = (TextView)findViewById(R.id.prompt);
        ImageView promptImg = (ImageView) findViewById(R.id.prompt_pic);
        current++;
        // If it's the end, leave the tutorial
        if (current > 17) {
            finish();
        }
        prompt.setText(prompts[current]);
        promptImg.setImageResource(tutorial_pics[current]);
        this.setTitle(headings[current]);

        Button backer = (Button)findViewById(R.id.tutorial_back);
        if (current == 0){
            backer.setVisibility(View.GONE);
        }
        else{
            backer.setVisibility(View.VISIBLE);
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
}
