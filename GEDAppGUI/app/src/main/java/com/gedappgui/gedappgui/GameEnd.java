package com.gedappgui.gedappgui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by jasminejans on 10/29/16.
 */

public class GameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
    }

    public void goToQuestions(View view) {
        Intent intent = new Intent(this, Question.class);
        startActivity(intent);
    }

    public void goToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }
}