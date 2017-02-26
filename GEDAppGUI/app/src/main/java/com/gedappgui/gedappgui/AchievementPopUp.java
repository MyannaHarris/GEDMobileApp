/*
 * AchievementPopUp.java
 *
 * The activity to display a popup for when achievements are earned in the app
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by jjans on 11/25/16.
 *
 * Last Edit: 11-29-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.MotionEvent;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;

public class AchievementPopUp extends AppCompatActivity {
    DatabaseHelper db;
    int achievementID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        Intent mIntent = getIntent();
        achievementID = mIntent.getIntExtra("achievementID", 0);

        //only executes the pop up if the achievement hasn't been earned yet
        if(!db.achievementExists(achievementID)) {
            setContentView(R.layout.activity_pop_up);

            String desc = db.selectAchievementDesc(achievementID);
            String img = db.selectAchievementImg(achievementID);
            String name = db.selectAchievementName(achievementID);

            //adds the achievement to the user achievements table
            db.insertAchievement(achievementID);

            //makes the activity smaller and appear on top of the previous activity
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            int popup_height = (int) (height/5);
            int popup_width = (int) (width);

            getWindow().setLayout(popup_width, popup_height);

            //adds the correct text data to the UI
            setUpPopUp(desc, img, name, popup_height, popup_width);
        }
        //the achievement has already been earned
        else {
            finish();
        }
    }

    /**
     * Adds the correct text from the database to the popup user interface
     * @param desc the description string of the achievement
     * @param img the image name string of the achievement
     * @param name the name string of the achievement
     */
    private void setUpPopUp(String desc, String img, String name, int height, int width){

        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/7);

        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);
        a_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/6));


        // get correct image from database
        ImageView lesson_imageView = (ImageView) findViewById(R.id.achievement_badge);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width/4, height);
        lesson_imageView.setLayoutParams(layoutParams);
        Bitmap achievementImg = getFromAssets(img);
        lesson_imageView.setImageBitmap(achievementImg);

    }

    /**
     * Returns a bitmap of the image with the given name found in the assets folder
     * @param imgName the name of the image you want
     * @return the
     *
     * modified Myanna's code from LessonSteps
     */
    private Bitmap getFromAssets(String imgName)
    {
        AssetManager assetManager = getAssets();
        InputStream in = null;

        try {
            in = assetManager.open("achievement_pics/" + imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
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

    /**
     * exits the activity and returns to the previous where this activity was called
     * when the user clicks anywhere
     * @param event the event the user creates
     */
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        this.finish();
        return false;
    }
}
