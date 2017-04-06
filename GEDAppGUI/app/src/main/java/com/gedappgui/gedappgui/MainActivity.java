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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Database
    private DatabaseHelper db;

    // Sprite image
    private LayerDrawable spriteDrawable;
    private ImageView spriteImage;

    // Sprite Accessory variables
    private Map<String, ArrayList<Integer>> accessoryMap;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        if (db.firstTimeLogin()){
            // Show login first time the app is opened
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            // Show home screen whenever app is opened after that
            setContentView(R.layout.activity_main);

            //set up the global username
            ((MyApplication) this.getApplication()).setName(db.selectUsername());

            // Get dragon name from database
            if(!((MyApplication) this.getApplication()).getDragonName().equals(db.selectDragonName())) {
                ((MyApplication) this.getApplication()).setDragonName(db.selectDragonName());
            }

            // Allow user to control audio with volume buttons on phone
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            //set up the dragon image with all his potential layers
            spriteImage = (ImageView)findViewById(R.id.sprite_homeScreen);
            ((MyApplication) this.getApplication()).setSpriteDrawable(
                    (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.layers)
            );

            // instantiate lists
            accessoryMap = new HashMap<String, ArrayList<Integer>>();

            // make dictionary of image ids
            makeDictionary();

            //get metrics of the screen
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            // Set dynamic size of text for greetings
            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);
            greetingText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/40));

            //sets a dynamic size for the button text on the main page
            Button toolsButton = (Button) findViewById(R.id.tools_button);
            toolsButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
            Button curLessButton = (Button) findViewById(R.id.continue_button);
            curLessButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
            Button achievementButton = (Button) findViewById(R.id.achievements_button);
            achievementButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
            Button settingsButton = (Button) findViewById(R.id.settings_button);
            settingsButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
            Button arcadeButton = (Button) findViewById(R.id.play_button);
            arcadeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
            Button classroomButton = (Button) findViewById(R.id.learn_button);
            classroomButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

            //changes to progress bar
            ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
            TextView seeProgress = (TextView)findViewById(R.id.see_progress);
            progress.setScaleY(4f);
            if(db.lessonCount() == 25){
                progress.setProgress(24);
                seeProgress.setText("Congratulations! You completed all lessons!");
            }
            else{
                progress.setProgress(db.lessonCount()-1);
            }
            seeProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/45));

            // Set database for my application
            ((MyApplication) this.getApplication()).setDBHelper(new DatabaseHelper(this));
        }


        //gives an achievement if the user earns all achievements
        if(db.usedAllFeatures()){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 5);
            startActivity(achievement);
        }
    }

    /**
     * Re-checks the username that the app needs to print when homescreen is opened
     * Set sprite image
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

        if(!db.firstTimeLogin()){
            TextView greetingText = (TextView)findViewById(R.id.sprite_speechBubble);

            //create list of greetings
            String name = db.selectUsername();
            String[] greetings = {
                    "There's Math to do \n" + name + "!",
                    "Hello \n" + name + "!\nWelcome to the app!",
                    "Have you checked my closet?",
                    "Have you played any games recently?",
                    "Did you know you can change my color? Go to my lair and tap on me!",
                    "I love Math! Don't you,\n" + name + "?",
                    "Check out the tools for some cool references!",
                    "Did you know you can change my name? Go to settings and click 'Change Dragon Name'!"
            };

            //sets greeting to a random greeting
            Random rand = new Random();
            greetingText.setText(greetings[rand.nextInt(7)]);

            // Sprite image
            spriteDrawable = ((MyApplication) this.getApplication()).getSpriteDrawable();
            spriteImage.setImageDrawable(spriteDrawable);

            //get metrics of the screen
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            //changes to progress bar
            ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar);
            TextView seeProgress = (TextView)findViewById(R.id.see_progress);
            seeProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/45));
            progress.setScaleY(4f);
            if(db.lessonCount() >= 25){
                progress.setProgress(24);
                seeProgress.setText("Congratulations! You completed all lessons!");
            }
            else{
                progress.setProgress(db.lessonCount()-1);
            }

            // Read in accessory data
            // accessory_img, layer_id, currently_wearing
            ArrayList<ArrayList<String>> accessories = db.selectAccessories();

            // Save what accessories should be displayed
            if(accessories != null && accessories.size() > 0) {
                for (int i = 0; i < accessories.size(); i++) {
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                        System.out.println(accessories.get(i).get(0));
                    }
                }
            }
        }
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
        inflater.inflate(R.menu.mainmenu, menu);
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

    /**
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

    /**
     * Add accessory to sprite
     * Map from name:
     * 0 - actual image id
     * 1 - icon image id
     * 2 - ImageView layer id
     * @param name the name of the saved accessory
     */
    public void addSavedAccessory(String name) {
        // Get info from map
        ArrayList<Integer> info = accessoryMap.get(name);

        int img = 0;
        int icon = 0;
        int layer = 0;

        if (info.size() > 2) {
            img = info.get(0);
            icon = info.get(1);
            layer = info.get(2);
        } else {
            img = info.get(0);
            layer = info.get(1);
        }

        // Draw accesory on dragon
        Drawable newItem;
        newItem = (Drawable) ContextCompat.getDrawable(MainActivity.this, img);
        spriteDrawable.setDrawableByLayerId(layer, newItem);
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();
    }

    /**
     * Opens Sprite view when button is clicked
     * Called when the user clicks the Sprite
     * @param view current view
     */
    public void goToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /**
     * Opens Learn (Concepts) view when button is clicked
     * Called when the user clicks the Learn button
     * @param view current view
     */
    public void goToLearn(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    /**
     * Opens Play (Games) view when button is clicked
     * Called when the user clicks the Play button
     * @param view current view
     */
    public void goToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }

    /**
     * Opens Achievements view when button is clicked
     * Called when the user clicks the Achievements button
     * @param view current view
     */
    public void goToAchievements(View view) {
        Intent intent = new Intent(this, Achievements.class);
        startActivity(intent);
    }

    /**
     * Opens Tools view when button is clicked
     * Called when the user clicks the Tools button
     * @param view current view
     */
    public void goToTools(View view) {
        Intent intent = new Intent(this, Tools.class);
        startActivity(intent);
    }

    /**
     * Opens Settings view when button is clicked
     * Called when the user clicks the Settings button
     * @param view current view
     */
    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Opens Lesson Summary view when button is clicked
     * Called when the user clicks the Continue Lesson button
     * @param view current view
     */
    public void goToContinueLesson(View view) {
        Intent intentSummary = new Intent(MainActivity.this, LessonSummary.class);

        //gets the current lesson id
        final int lessonID = db.selectCurrentLessonID();

        //for test purposes
        System.out.println(lessonID);

        //check to see if it is a valid lesson
        if (lessonID < 25) {
            final String lessonTitle = db.selectLessonTitle(lessonID);
            System.out.println(lessonTitle);
            final int conceptID = db.selectConceptID(lessonID);
            System.out.println(conceptID);

            intentSummary.putExtra("lessonTitle", lessonTitle);
            intentSummary.putExtra("conceptID", conceptID);
            intentSummary.putExtra("lessonID", lessonID);

            MainActivity.this.startActivity(intentSummary);
        }
    }

    /**
     * Make dictionary for accessories
     */
    public void makeDictionary() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_glasses);
        ids.add(R.drawable.sprite_glasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_glasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_monocle);
        ids.add(R.drawable.sprite_monocle_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_monocle", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_nerdglasses);
        ids.add(R.drawable.sprite_nerdglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_nerdglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_roundglasses);
        ids.add(R.drawable.sprite_roundglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_roundglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyglasses);
        ids.add(R.drawable.sprite_fancyglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_fancyglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_grannyglasses);
        ids.add(R.drawable.sprite_grannyglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_grannyglasses", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_brownhat);
        ids.add(R.drawable.sprite_brownhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_brownhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball);
        ids.add(R.drawable.sprite_hat_baseball_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_camo);
        ids.add(R.drawable.sprite_hat_baseball_camo_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_camo", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_red);
        ids.add(R.drawable.sprite_hat_baseball_red_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tophat);
        ids.add(R.drawable.sprite_tophat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_tophat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_ribbonhat);
        ids.add(R.drawable.sprite_ribbonhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_ribbonhat", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long);
        ids.add(R.drawable.sprite_shirt_long_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long_green);
        ids.add(R.drawable.sprite_shirt_long_green_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long_green", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short);
        ids.add(R.drawable.sprite_shirt_short_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short_red);
        ids.add(R.drawable.sprite_shirt_short_red_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyshirt);
        ids.add(R.drawable.sprite_fancyshirt_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_fancyshirt", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tropicalshirt);
        ids.add(R.drawable.sprite_tropicalshirt_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_tropicalshirt", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_cane);
        ids.add(R.drawable.sprite_cane_icon);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_cane", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_partyhat);
        ids.add(R.drawable.sprite_partyhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_partyhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_redribbonhat);
        ids.add(R.drawable.sprite_redribbonhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_redribbonhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_armor);
        ids.add(R.drawable.sprite_armor_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_armor", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_sword);
        ids.add(R.drawable.sprite_sword_icon);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_sword", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_treasure);
        ids.add(R.drawable.sprite_treasure_icon);
        ids.add(R.id.accessory_wingItem);
        accessoryMap.put("sprite_treasure", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon2);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon2", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon3);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon3", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon4);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon4", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon5);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon5", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon6);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon6", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon7);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon7", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon8);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon8", ids);
    }
}
