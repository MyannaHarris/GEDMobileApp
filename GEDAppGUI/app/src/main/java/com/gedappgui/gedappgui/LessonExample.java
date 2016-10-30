package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LessonExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_example);
    }
    public void gotToLessonGame(View view) {
        Intent intent = new Intent(this, GameIntro.class);
        startActivity(intent);
    }
}
