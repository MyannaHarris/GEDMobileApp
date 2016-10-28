package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LessonSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_summary);
    }
    public void gotToLessonSteps(View view) {
        Intent intent = new Intent(this, LessonSteps.class);
        startActivity(intent);
    }
}
