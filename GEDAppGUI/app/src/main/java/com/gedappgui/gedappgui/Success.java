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
 * Last Edit: 3-20-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
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

import org.w3c.dom.Text;

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

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
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

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.long_success);
        mediaPlayer.start();



        // Get screen dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        ArrayList<Integer> accessories = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);

        // Make dictionary of image ids
        accessoryMap = new HashMap<Integer, Integer>();
        makeDictionary();

        TextView pickText = (TextView) findViewById(R.id.accessory_choice);
        TextView helperText = (TextView) findViewById(R.id.pick_location);
        TextView congrats = (TextView) findViewById(R.id.congratulations);

        // If the next lesson is newly unlocked, the user receives an accessory
        if (!(dbHelper.isLessonAlreadyStarted(lessonID+1))) {
            pickText.setText("Pick an accessory:");
            // Get random accessories user doesn't have from db and put them in ArrayList
            ArrayList<Integer> ids = dbHelper.getRandomAccessories();
            for (int i=0; i<ids.size(); i++) {
                accessoryGiven = ids.get(i);
                accessories.add(ids.get(i)); // accessory id
                accessories.add(accessoryMap.get(ids.get(i))); // accessory image
            }
            helperText.setText("Once you decide what accessory you want, choose where you\'d like to go next!");
            checkAchievements(totalQuestions, totalCorrect);
        }

        Button toLesson = (Button) findViewById(R.id.to_lesson);
        Button toConcepts = (Button) findViewById(R.id.to_concepts);
        Button toSprite = (Button) findViewById(R.id.to_sprite);

        helperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        pickText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        congrats.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/20);
        toLesson.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        toConcepts.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        toSprite.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

        dbHelper.lessonCompleted(lessonID);


        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        gridlayout = (GridLayout) findViewById(R.id.accessory_options);

        // Put things in the grid layout
        setAccessoryInfo(accessories);
        if (dbHelper.isLastLesson(lessonID)) {
            RelativeLayout page = (RelativeLayout) findViewById(R.id.successPage);
            page.removeView(findViewById(R.id.to_lesson));
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

    /**
     * Checks to see if any achievements can be awarded and awards them accordingly
     * @param totalQuestions the number of questions the user answered
     * @param totalCorrect the number of questions the user answered correctly
     */
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

        // Achievement for the first lesson completion
        if(!dbHelper.achievementExists(8)) {
            //gives an achievement if they complete a lesson for the first time
            Intent achievement = new Intent(getApplicationContext(), AchievementPopUp.class);
            achievement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            achievement.putExtra("achievementID", 8);
            startActivity(achievement);
        }

        // Give an achievement if the user completes the first concept
        if(lessonID == 6 && !dbHelper.achievementExists(9)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 9);
            startActivity(achievement);
        }

        // Give an achievement if the user completes the second concept
        if(lessonID == 12 && !dbHelper.achievementExists(10)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 10);
            startActivity(achievement);
        }

        // Give an achievement if the user completes the final concept
        if(lessonID == 24 && !dbHelper.achievementExists(11)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 11);
            startActivity(achievement);
        }

        // Give an achievement if the user completes the lesson after going over the Redo area
        if(redo == 1 && !dbHelper.achievementExists(14)){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 14);
            startActivity(achievement);
        }

        // Check to make sure that a user is going through a new lesson and not a repeated lesson
        if(!(dbHelper.isLessonAlreadyStarted(lessonID+1))) {
            // Gives an achievement if the user earns 3 accessories use >= if on sprite page
            if (dbHelper.countAccessoriesEarned() == 3) {
                Intent achievement = new Intent(this, AchievementPopUp.class);
                achievement.putExtra("achievementID", 15);
                startActivity(achievement);
            }


        // Gives an achievement if the user earns 8 accessories use >= if on sprite page
            if(dbHelper.countAccessoriesEarned() == 8) {
                Intent achievement = new Intent(this, AchievementPopUp.class);
                achievement.putExtra("achievementID", 16);
                startActivity(achievement);
            }


        // Gives an achievement if the user earns all accessories use >= if on sprite page
            if(dbHelper.countAccessoriesEarned() == 24) {
                Intent achievement = new Intent(this, AchievementPopUp.class);
                achievement.putExtra("achievementID", 17);
                startActivity(achievement);
            }
        }

    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
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

    /**
     * Opens concepts page, Called by user pressing to Concept button
     * @param view current view
     */
    public void goToConcepts(View view) {
        giveUserItem();
        Intent intent = new Intent(this, LearnConcepts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Opens next lesson's summary page, Called by user pressing To Next Lesson button
     * @param view current view
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

    /**
     * Opens sprite page, Called by user pressing to Lair button
     * @param view current view
     */
    public void goToSprite(View view) {
        giveUserItem();
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    /**
     * Adds accessories into layout
     * @param accessories the accessory IDs and images
     */
    public void setAccessoryInfo(ArrayList<Integer> accessories) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels/3;
        int length = accessories.size()/2;

        GridLayout.Spec thisRow = GridLayout.spec(0, 1);

        // Place each accessory into the layout
        for (int i = 0; i < length; i++) {
            GridLayout.Spec col = GridLayout.spec(i+1,1);
            GridLayout.LayoutParams gridLayoutParam0 = new GridLayout.LayoutParams(thisRow, col);
            gridLayoutParam0.setGravity(Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL);
            ImageView img = createAccessoryImage(accessories.get(i*2+1), accessories.get(i*2), maxWidth);
            gridlayout.addView(img,gridLayoutParam0);
        }


    }

    /**
     * Creates the accessory image views correctly and makes them clickable
     * @param img the image information
     * @param id the id of the accessory, so it can be given
     * @param maxWidth the maximum width of the image
     * @return the created image view
     */
    ImageView createAccessoryImage(Integer img, int id, int maxWidth) {
        final int finalID = id;
        ImageView imgView = new ImageView(this);
        imgView.setMaxWidth(maxWidth);
        imgView.setAdjustViewBounds(true);
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // Make the accessory able to be selected
        imgView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                accessoryGiven = finalID;
                ImageView img = (ImageView) v;
                GridLayout gLayout = (GridLayout)findViewById(R.id.accessory_options);
                for (int i=0; i<gLayout.getChildCount();i++) {
                    ImageView child = (ImageView) gLayout.getChildAt(i);
                    child.setBackgroundColor(ContextCompat.getColor(Success.this, R.color.transparent));
                }
                img.setBackgroundColor(ContextCompat.getColor(Success.this, R.color.accessoryHighlight));

            }
        });

        Drawable image;
        image = (Drawable) ContextCompat.getDrawable(Success.this, img);
        imgView.setImageDrawable(image);
        return imgView;
    }

    /**
     * Gives user the item they choose from the three accessory options
     */
    void giveUserItem() {
        dbHelper.giveAccessory(accessoryGiven);
    }

    /**
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