package com.gedappgui.gedappgui;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        if(!db.achievementExists(achievementID)) {
            setContentView(R.layout.activity_pop_up);

            String desc = db.selectAchievementDesc(achievementID);
            String img = db.selectAchievementImg(achievementID);
            String name = db.selectAchievementName(achievementID);

            db.insertAchievement(achievementID);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = dm.heightPixels;

            getWindow().setLayout((int) (width * .5), (int) (height * .5));

            setUpPopUp(desc, img, name);
        }
        else {
            finish();
        }
    }

    private void setUpPopUp(String desc, String img, String name){

        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);

        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);

        // get correct image
        //Bitmap achievementImg = getFromAssets(img);
        Bitmap achievementImg = getFromAssets("achievement_badge.png");

        ImageView lesson_imageView = (ImageView) findViewById(R.id.achievement_badge);
        lesson_imageView.setImageBitmap(achievementImg);

    }

    /**
     * Returns a bitmap of the image with the given name found in the assets folder
     * @param imgName the name of the image you want
     * @return the
     *
     * used from Myannas code in LessonSteps
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

    public void exitPopUp(View view) {
        finish();
    }
}
