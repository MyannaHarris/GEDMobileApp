package com.gedappgui.gedappgui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by jasminejans on 10/29/16.
 */

public class Success extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
    }

    public void goToConcepts(View view) {
        Intent intent = new Intent(this, LearnConcepts.class);
        startActivity(intent);
    }

    public void goToNextLesson(View view) {
        Intent intent = new Intent(this, LearnLessons.class);
        startActivity(intent);
    }

    public void goToSprite(View view) {
        Intent intent = new Intent(this, Sprite.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}