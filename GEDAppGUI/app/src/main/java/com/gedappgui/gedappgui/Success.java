/*
 * Success.java
 *
 * Success page activity
 *
 * Tells the user that they successfully completed the lesson
 * Allows them to choose a sprite reward
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by jasminejans on 10/29/16.
 *
 * Last Edit: 11-6-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Success extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int lessonID;
    private int conceptID;
    private int redo;
    private GridLayout gridlayout;
    private int accessoryGiven = 0;

    private Map<Integer, Integer> accessoryMap;

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        redo = mIntent.getIntExtra("redoComplete", 0);
        int totalCorrect = mIntent.getIntExtra("totalCorrect", 0);
        int totalQuestions = mIntent.getIntExtra("totalQuestions", 0);

        ArrayList<Integer> accessories = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);

        // make dictionary of image ids
        accessoryMap = new HashMap<Integer, Integer>();
        makeDictionary();

        if (!(dbHelper.isLessonAlreadyStarted(lessonID+1))) {
            TextView pickText = (TextView) findViewById(R.id.accessory_choice);
            pickText.setText("Pick your sprite accessory:");
            // get random accessories user doesn't have from db and put them in ArrayList
            ArrayList<Integer> ids = dbHelper.getRandomAccessories();
            for (int i=0; i<ids.size(); i++) {
                accessoryGiven = ids.get(i);
                accessories.add(ids.get(i)); // accessory id
                accessories.add(accessoryMap.get(ids.get(i))); // accessory image
            }
        }

        dbHelper.lessonCompleted(lessonID);


        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridlayout = (GridLayout) findViewById(R.id.accessory_options);

        //put things in the gridlayout
        setAccessoryInfo(accessories);

        checkAchievements(totalQuestions, totalCorrect);
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

    //checks to see if any achievements can be awarded
    private void checkAchievements(int totalQuestions, int totalCorrect){
        if(totalQuestions!=0){
            if(totalCorrect%totalQuestions == 0){
                //gives an achievement if the user opens the achievements in for the first time
                Intent achievement = new Intent(this, AchievementPopUp.class);
                achievement.putExtra("achievementID", 12);
                startActivity(achievement);
            }
            else if((float)totalCorrect/(float)totalQuestions >= .75) {
                //gives an achievement if the user opens the achievements in for the first time
                Intent achievement = new Intent(this, AchievementPopUp.class);
                achievement.putExtra("achievementID", 13);
                startActivity(achievement);
            }
        }

        //Achievement for the first lesson completion
        if(!dbHelper.achievementExists(8)) {
            //gives an achievement if they complete a lesson for the first time
            Intent achievement = new Intent(getApplicationContext(), AchievementPopUp.class);
            achievement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            achievement.putExtra("achievementID", 8);
            startActivity(achievement);
        }

        //gives an achievement if the user completes the first concept
        if(lessonID == 6 && !dbHelper.achievementExists(9)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 9);
            startActivity(achievement);
        }

        //gives an achievement if the user completes the second concept
        if(lessonID == 12 && !dbHelper.achievementExists(10)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 10);
            startActivity(achievement);
        }

        //gives an achievement if the user completes the final concept
        if(lessonID == 24 && !dbHelper.achievementExists(11)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 11);
            startActivity(achievement);
        }

        //gives an achievement if the user completes the lesson after going over the redos
        if(redo == 1 && !dbHelper.achievementExists(14)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 14);
            startActivity(achievement);
        }

        //gives an achievement if the user earns 3 accessories use >= if on sprite page
        if(dbHelper.countAccessoriesEarned() == 3){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 15);
            startActivity(achievement);
        }

        //gives an achievement if the user earns 8 accessories use >= if on sprite page
        if(dbHelper.countAccessoriesEarned() == 8){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 16);
            startActivity(achievement);
        }

        //gives an achievement if the user earns all accessories use >= if on sprite page
        if(dbHelper.countAccessoriesEarned() == 24){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 17);
            startActivity(achievement);
        }
    }

    /*
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /*
     * Sets what menu will be in the action bar
     * homeonlymenu has the settings button and the home button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
     * settings = goes to settings page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_home:
                giveUserItem();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                giveUserItem();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    /*
     * Called by concept button
     * Opens concepts page
     */
    public void goToConcepts(View view) {
        giveUserItem();
        Intent intent = new Intent(this, LearnConcepts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * Called by next lesson button
     * Opens next lessons summary page
     */
    public void goToNextLesson(View view) {
        final String lessonTitle = dbHelper.selectLessonTitle(lessonID+1);
        giveUserItem();
        Intent intent = new Intent(this, LessonSummary.class);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID+1);
        intent.putExtra("lessonTitle", lessonTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * Called by sprite button
     * Opens sprite page
     */
    public void goToSprite(View view) {
        giveUserItem();
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }
    public void setAccessoryInfo(ArrayList<Integer> accessories) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels/3;
        int length = accessories.size()/2;

        GridLayout.Spec thisRow = GridLayout.spec(0, 1);

        for (int i = 0; i < length; i++) {
            GridLayout.Spec col = GridLayout.spec(i,1);
            GridLayout.LayoutParams gridLayoutParam0 = new GridLayout.LayoutParams(thisRow, col);
            gridLayoutParam0.setGravity(Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL);
            ImageView img = createAccessoryImage(accessories.get(i*2+1), accessories.get(i*2), maxWidth);
            gridlayout.addView(img,gridLayoutParam0);
        }


    }

    ImageView createAccessoryImage(Integer img, int id, int maxWidth) {
        final int finalID = id;
        ImageView imgView = new ImageView(this);
        imgView.setMaxWidth(maxWidth);
        imgView.setAdjustViewBounds(true);
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                accessoryGiven = finalID;
            }
        });

        Drawable image;
        image = (Drawable) ContextCompat.getDrawable(Success.this, img);
        imgView.setImageDrawable(image);
        return imgView;
    }

    void giveUserItem() {
        dbHelper.giveAccessory(accessoryGiven);
    }

    /*
     * Make dictionary for accessories
     */
    public void makeDictionary() {
        accessoryMap.put(11, R.drawable.sprite_glasses_icon);
        accessoryMap.put(15, R.drawable.sprite_monocle_icon);
        accessoryMap.put(16, R.drawable.sprite_nerdglasses_icon);
        accessoryMap.put(20, R.drawable.sprite_roundglasses_icon);
        accessoryMap.put(26, R.drawable.sprite_fancyglasses_icon);
        accessoryMap.put(27, R.drawable.sprite_grannyglasses_icon);

        accessoryMap.put(1, R.drawable.sprite_brownhat_icon);
        accessoryMap.put(12, R.drawable.sprite_hat_baseball_icon);
        accessoryMap.put(13, R.drawable.sprite_hat_baseball_camo_icon);
        accessoryMap.put(14, R.drawable.sprite_hat_baseball_red_icon);
        accessoryMap.put(25, R.drawable.sprite_tophat_icon);
        accessoryMap.put(19, R.drawable.sprite_ribbonhat_icon);

        accessoryMap.put(21, R.drawable.sprite_shirt_long_icon);
        accessoryMap.put(22, R.drawable.sprite_shirt_long_green_icon);
        accessoryMap.put(23, R.drawable.sprite_shirt_short_icon);
        accessoryMap.put(24, R.drawable.sprite_shirt_short_red_icon);
        accessoryMap.put(28, R.drawable.sprite_fancyshirt_icon);
        accessoryMap.put(29, R.drawable.sprite_tropicalshirt_icon);

        accessoryMap.put(2, R.drawable.sprite_cane_icon);
        accessoryMap.put(17, R.drawable.sprite_partyhat_icon);
        accessoryMap.put(18, R.drawable.sprite_redribbonhat_icon);
        accessoryMap.put(30, R.drawable.sprite_armor_icon);
        accessoryMap.put(32, R.drawable.sprite_sword_icon);
        accessoryMap.put(31, R.drawable.sprite_treasure_icon);
    }
}