package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LearnLessons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_lessons);
    }
    public void gotToLessonSummary(View view) {
        Intent intent = new Intent(this, LessonSummary.class);
        startActivity(intent);
    }
}
