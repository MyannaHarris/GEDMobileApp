package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LessonSteps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_steps);
    }
    public void gotToLessonExample(View view) {
        Intent intent = new Intent(this, LessonExample.class);
        startActivity(intent);
    }
}
