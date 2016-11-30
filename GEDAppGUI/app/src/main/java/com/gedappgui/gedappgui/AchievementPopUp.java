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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.DisplayMetrics;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

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
            getWindow().setLayout((int) (width * .5), (int) (height * .5));

            //adds the correct text data to the UI
            setUpPopUp(desc, img, name);
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
    private void setUpPopUp(String desc, String img, String name){

        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);

        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);

        // get correct image from database
        //Bitmap achievementImg = getFromAssets(img);
        //placeholder image for now
        Bitmap achievementImg = getFromAssets("achievement_badge.png");

        ImageView lesson_imageView = (ImageView) findViewById(R.id.achievement_badge);
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

    /**
     * exits the activity and returns to the previous where this activity was called
     * @param view the view of the activity
     */
    public void exitPopUp(View view) {
        finish();
    }
}
