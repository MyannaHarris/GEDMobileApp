package com.gedappgui.gedappgui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!login)
            setContentView(R.layout.login);
        else
            setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Login button */
    public void setLogin(View view) {
        login = true;
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Home button */
    public void goHome(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Continue Lesson button */
    public void gotToContinueLesson(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Learn button */
    public void gotToLearn(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Play button */
    public void gotToPlay(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Achievements button */
    public void gotToAchievements(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Tools button */
    public void gotToTools(View view) {
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Settings button */
    public void gotToSettings(View view) {
        setContentView(R.layout.activity_main);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }*/
}
