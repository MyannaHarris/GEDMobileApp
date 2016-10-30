/*
 * MainActivity.java
 *
 * Home screen activity
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 10-26-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!((MyApplication) this.getApplication()).getLoginStatus()) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);
            String greeting = "Hello " + ((MyApplication) this.getApplication()).getName();
            greeting += "!\nWelcome to the app.";
            greetingText.setText(greeting);
        }
    }

    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /** Called when the user clicks the Sprite */
    public void gotToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Continue Lesson button */
    public void gotToContinueLesson(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Learn button */
    public void gotToLearn(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Play button */
    public void gotToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Achievements button */
    public void gotToAchievements(View view) {
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Tools button */
    public void gotToTools(View view) {
        Intent intent = new Intent(this, Tools.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Settings button */
    public void gotToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
