package com.gedappgui.gedappgui;

import android.support.v7.app.AppCompatActivity;

/*
 * AchievementInfo.java
 *
 * The activity to display a popup for when achievements are clicked in the achievemnets page
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by jjans on 1/29/17.
 *
 * Last Edit: 1-29-17
 *
 */


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
import android.widget.Button;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;

public class AchievementInfo extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);

        setContentView(R.layout.activity_pop_up_info);

        Intent mIntent = getIntent();
        String achievementName = mIntent.getStringExtra("achievementName");
        String achievementDesc = mIntent.getStringExtra("achievementDesc");
        String achievementImg = mIntent.getStringExtra("achievementImg");


        //makes the activity smaller and appear on top of the previous activity
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        int popup_height = (int)(height/5);
        int popup_width = (int)(width);

        getWindow().setLayout(popup_width, popup_height);

        //adds the correct text data to the UI
        setUpPopUp(achievementDesc, achievementName, achievementImg, popup_height, popup_width);
    }

    /**
     * Adds the correct text from the database to the popup user interface
     * @param desc the description string of the achievement
     * @param name the name string of the achievement
     */
    private void setUpPopUp(String desc, String name, String img, int height, int width){

        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/7);

        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);
        a_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/6));

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

    /**
     * exits the activity and returns to the previous where this activity was called
     * @param view the view of the activity
     */
    public void exitPopUp(View view) {
        finish();
    }

}
