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
 * Last Edit: 11-6-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    //private File file;

    // Gridview
    //GridView gridview;

    /*
     * Starts the first activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!((MyApplication) this.getApplication()).getLoginStatus()) {
            // Show login first time the app is opened
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {

            // Show home screen whenever app is opened after that
            setContentView(R.layout.activity_main);

            // Allow user to control audio with volume buttons on phone
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            /*gridview = (GridView) findViewById(R.id.home_gridView);
            String[] buttonNames = {
                    ""
            };
            gridview.setAdapter(new TextViewAdapter(this, buttonNames));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // Preform a function based on the position
                    switch (position) {
                        case 0:
                            Intent intentContinue = new Intent(MainActivity.this, LessonSummary.class);
                            startActivity(intentContinue);
                            break;
                        case 1:
                            Intent intentAchievements = new Intent(MainActivity.this, Achievements.class);
                            startActivity(intentAchievements);
                            break;
                        case 2:
                            Intent intentLearn = new Intent(MainActivity.this, LearnConcepts.class);
                            startActivity(intentLearn);
                            break;
                        case 3:
                            Intent intentTools = new Intent(MainActivity.this, Tools.class);
                            startActivity(intentTools);
                            break;
                        case 4:
                            Intent intentPlay = new Intent(MainActivity.this, Play.class);
                            startActivity(intentPlay);
                            break;
                        case 5:
                            Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intentSettings);
                            break;
                        default:
                            break;
                    }
                }
            });*/
        }

        /*try {
            file = new File(this.getApplication().getFilesDir(), "copy.db");
            copy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = openOrCreateDatabase(file.getPath(), MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM test", null);
        c.moveToFirst();
        //c.moveToNext();
        System.out.println(c.getString(0));
        c.close();
        db.close();*/
    }

    /*public void copy() throws IOException {

        InputStream in = getApplicationContext().getAssets().open("test.db");
        OutputStream out = new FileOutputStream(file);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }*/


    /*
     * Re-checks the username that the app needs to print when homescreen is opened
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (((MyApplication) this.getApplication()).getLoginStatus()) {

            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);
            String greeting = "Hello " + ((MyApplication) this.getApplication()).getName();
            greeting += "!\nWelcome to the app.";
            greetingText.setText(greeting);
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
                Intent intentContinue = new Intent(this, LessonSummary.class);
                startActivity(intentContinue);
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
     */
    /** Called when the user clicks the Sprite */
    public void gotToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /*
     * Opens Learn (Concepts) view when button is clicked
     */
    /** Called when the user clicks the Learn button */
    public void gotToLearn(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /*
     * Opens Play (Games) view when button is clicked
     */
    /** Called when the user clicks the Play button */
    public void gotToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    /*
     * Opens Achievements view when button is clicked
     */
    /** Called when the user clicks the Achievements button */
    public void gotToAchievements(View view) {
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
    }

    /*
     * Opens Tools view when button is clicked
     */
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

    /** Called when the user clicks the Continue Lesson button */
    public void gotToContinueLesson(View view) {
        Intent intent = new Intent(this, LessonSummary.class);
        startActivity(intent);
    }
}
