package com.gedappgui.gedappgui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.DisplayMetrics;
import android.content.Intent;
import android.widget.TextView;

public class AchievementPopUp extends AppCompatActivity {
    DatabaseHelper db;
    int achievementID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        db = new DatabaseHelper(this);

        Intent mIntent = getIntent();
        achievementID = mIntent.getIntExtra("achievementID", 0);

        String desc = db.selectAchievementDesc(achievementID);
        String img = db.selectAchievementImg(achievementID);
        String name = db.selectAchievementName(achievementID);

        //db.insertAchievement(achievementID);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        TextView description = (TextView) findViewById(R.id.achievement_desc);
        description.setText(desc);

        TextView a_name = (TextView) findViewById(R.id.achievement_name);
        a_name.setText(name);
    }

}
