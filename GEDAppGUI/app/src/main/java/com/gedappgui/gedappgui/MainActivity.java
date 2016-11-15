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
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private File file;

    /*
     * Starts the first activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!((MyApplication) this.getApplication()).getLoginStatus()) {
            //tries to load database on first login
            try {
                //check for availability of the external storage
                //keep in mind external storage is public
                /*if(isExternalStorageReadable() && isExternalStorageWritable()) {
                    //copies the database file in assets to a new file in external storage
                    file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(), "GEDPrep.db");
                    System.out.println("external");
                    copy();
                }
                else{*/
                //if external is not available we must copy to local app storage that is at risk of being cleared
                file = new File(this.getApplication().getFilesDir(), "GEDPrep.db");
                copy();
                System.out.println("local");

                //send a popup here maybe that notifies the user of the risks?
                //System.out.println("fail");
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }

            SQLiteDatabase db = openOrCreateDatabase(file.getPath(), MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM test", null);

            //gets all the values saved from the query in the cursor
            while (c.moveToNext()) {
                System.out.println(c.getString(0));
            }

            c.close();
            db.close();

            // Show login first time the app is opened
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {

            // Show home screen whenever app is opened after that
            setContentView(R.layout.activity_main);

            // Allow user to control audio with volume buttons on phone
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    /* Checks if external storage is available write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    Checks if external storage is available to read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    } */



    /* copies the database in the assets folder into the new db in either
       external storage or local app storage */
    public void copy() throws IOException {

        //sets the existing DB int the assets folder to be copied as the input
        InputStream in = getApplicationContext().getAssets().open("test.db");
        OutputStream out = new FileOutputStream(file);

        // Transfers all bytes from in to out
        byte[] buffer = new byte[1024];
        int len;


        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }


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
        Intent intent = new Intent(this, LessonSummary.class);
        startActivity(intent);
    }
}
