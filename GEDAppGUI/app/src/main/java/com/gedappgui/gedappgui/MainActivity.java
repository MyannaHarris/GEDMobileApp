/*
 * MainActivity.java
 *
 * Home screen activity
 *
 * Main screen for app
 * Has links to learn, play, achievements, sprite, and tools
 * Has links to settings and continue lesson in action bar
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

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    // Database
    DatabaseHelper db;

    // Sprite image
    LayerDrawable spriteDrawable;
    ImageView spriteImage;

    /*
     * Starts the first activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        //if (!((MyApplication) this.getApplication()).getLoginStatus()) {
        if (db.firstTimeLogin()){
            // Show login first time the app is opened
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            // Show home screen whenever app is opened after that
            setContentView(R.layout.activity_main);

            // Allow user to control audio with volume buttons on phone
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            spriteImage = (ImageView)findViewById(R.id.sprite_homeScreen);

            ((MyApplication) this.getApplication()).setSpriteDrawable(
                    (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.layers)
            );
        }
    }


    /*
     * Re-checks the username that the app needs to print when homescreen is opened
     * Set sprite image
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();

        //if (((MyApplication) this.getApplication()).getLoginStatus()) {
        if(!db.firstTimeLogin()){
            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);

            //without the DB
            //String greeting = "Hello " + ((MyApplication) this.getApplication()).getName();

            //with the DB pulling information
            String greeting = "Hello " + db.selectUsername();

            greeting += "!\nWelcome to the app.";
            greetingText.setText(greeting);

            // Sprite image
            spriteDrawable = ((MyApplication) this.getApplication()).getSpriteDrawable();
            spriteImage.setImageDrawable(spriteDrawable);
        }
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

    /*
     * Sets what menu will be in the action bar
     * mainmenu has the settings button and continue lesson button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * continue lesson = goes to beginning of current lesson
     * settings = goes to settings page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_continueLesson:

                Intent intentContinue = new Intent(MainActivity.this, LessonSummary.class);

                final int lessonID = db.selectCurrentLessonID();
                final String lessonTitle = db.selectLessonTitle(lessonID);
                final int conceptID = db.selectConceptID(lessonID);

                intentContinue.putExtra("conceptID",conceptID);
                intentContinue.putExtra("lessonTitle",lessonTitle);
                intentContinue.putExtra("lessonTitle",lessonID);

                MainActivity.this.startActivity(intentContinue);
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
     * Listens for the back button on the bottom navigation bar
     * leaves app if pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
     * Opens Sprite view when button is clicked
     * Called when the user clicks the Sprite
     */
    public void gotToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /*
     * Opens Learn (Concepts) view when button is clicked
     * Called when the user clicks the Learn button
     */
    public void gotToLearn(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /*
     * Opens Play (Games) view when button is clicked
     * Called when the user clicks the Play button
     */
    public void gotToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    /*
     * Opens Achievements view when button is clicked
     * Called when the user clicks the Achievements button
     */
    public void gotToAchievements(View view) {
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
    }


    /*
     * Opens Tools view when button is clicked
     * Called when the user clicks the Tools button
     */
    public void gotToTools(View view) {
        Intent intent = new Intent(this, Tools.class);
        startActivity(intent);
    }

    /*
     * Opens Settings view when button is clicked
     * Called when the user clicks the Settings button
     */
    public void gotToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /*
     * Opens Lesson Summary view when button is clicked
     * Called when the user clicks the Continue Lesson button
     */
    public void gotToContinueLesson(View view) {
        Intent intentSummary = new Intent(MainActivity.this, LessonSummary.class);

        final int lessonID = db.selectCurrentLessonID();
        System.out.println(lessonID);
        final String lessonTitle = db.selectLessonTitle(lessonID);
        System.out.println(lessonTitle);
        final int conceptID = db.selectConceptID(lessonID);
        System.out.println(conceptID);

        intentSummary.putExtra("lessonTitle", lessonTitle);
        intentSummary.putExtra("conceptID",conceptID);
        intentSummary.putExtra("lessonID",lessonID);

        MainActivity.this.startActivity(intentSummary);
    }
}
