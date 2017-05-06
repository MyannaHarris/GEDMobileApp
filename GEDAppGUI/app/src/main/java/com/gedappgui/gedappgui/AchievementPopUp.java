/*
 * AchievementPopUp.java
 *
 * The activity to display a popup for when an achievement is earned in the app
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring
 *
 * This file is part of DragonAcademy.
 *
 * DragonAcademy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License. All redistributions
 * of the app or modifications of the app are to remain free in accordance
 * with the GNU General Public License.
 *
 * DragonAcademy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DragonAcademy.  If not, see <http://www.gnu.org/licenses/>.
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

    // The bitmap
    private Bitmap achievementImg;

    // ImageView for achievement
    private ImageView lesson_imageView;

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

        Intent mIntent = getIntent();
        achievementID = mIntent.getIntExtra("achievementID", 0);

        //only executes the pop up (activity) if the achievement hasn't been earned yet
        if(!db.achievementExists(achievementID)) {
            setContentView(R.layout.activity_pop_up);

            //gets the corresponding achievement information from the db
            String desc = db.selectAchievementDesc(achievementID);
            String imgStr = db.selectAchievementImg(achievementID);
            String name = db.selectAchievementName(achievementID);

            //adds the achievement to the user achievements table
            db.insertAchievement(achievementID);

            //makes the activity smaller and appear on top of the previous activity
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            int popup_height = height/5;
            getWindow().setLayout(width, popup_height);

            //sets up the pop up with the correct image, name and description
            setUpPopUp(desc, name, imgStr, popup_height, width);
        }
        //if the achievement has already been earned, finish the activity
        else {
            finish();
        }
    }

    /**
     * Adds the correct name, description and image of the achievement
     * from the database to the popup activity
     * @param desc the description string of the achievement
     * @param name the name string of the achievement
     * @param imgStr the name of the img of the achievement
     * @param height the height of the popup
     * @param width the width of the popup
     */
    private void setUpPopUp(String desc, String name, String imgStr, int height, int width){
        //sets the description texts
        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/7);

        //sets the name texts
        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);
        a_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/6));


        // get correct image from database and adjusts its size
        lesson_imageView = (ImageView) findViewById(R.id.achievement_badge);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width/4, height);
        lesson_imageView.setLayoutParams(layoutParams);

        //gets the image from assets
        achievementImg = getFromAssets(imgStr);
        lesson_imageView.setImageBitmap(achievementImg);

    }

    /**
     * Returns a bitmap of the image with the given name found in the assets folder
     * @param imgName the name of the image you want
     * @return the bitmap from assets
     *
     * modified Myanna's code from LessonSteps
     */
    private Bitmap getFromAssets(String imgName)
    {
        AssetManager assetManager = getAssets();
        InputStream in = null;

        //tries to find the given filename in assets
        try {
            in = assetManager.open("achievement_pics/" + imgName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //creates a bitmap from the file got from assets
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
    }

    /**
     * Gets access to the assets folder
     * @return Access to the assets
     */
    @Override
    public AssetManager getAssets()
    {
        return getResources().getAssets();
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
     * When the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (lesson_imageView != null) {
            lesson_imageView.setImageBitmap(null);
        }

        if(achievementImg!=null)
        {
            achievementImg.recycle();
            achievementImg=null;
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
     * exits the activity and returns to the previous activity where this popup was called
     * when the user clicks anywhere on the screen
     * @param event the event the user creates
     */
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        this.finish();
        return false;
    }
}
