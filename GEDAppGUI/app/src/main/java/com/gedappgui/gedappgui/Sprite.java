package com.gedappgui.gedappgui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Sprite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprite);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* 
     * Shows and hides the bottom navigation bar when user flings on screen 
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            // action with ID action_refresh was selected
            case R.id.action_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void showGlasses(View view) {

    }

    public void showShirts(View view) {

    }

    public void showBling(View view) {

    }

    public void showHats(View view) {

    }

    public void addAccessory(View view) {

        /*ImageView spriteImage = (ImageView)findViewById(R.id.sprite_homeScreen);

        switch(view.getId()) {
            case R.id.accessory_1:
                achievementDesc = "Achievement 1";
                break;
            case R.id.accessory_2:
                achievementDesc = "Achievement 2";
                break;
            case R.id.accessory_3:
                achievementDesc = "Achievement 3";
                break;
            case R.id.accessory_4:
                achievementDesc = "Achievement 4";
                break;
            case R.id.accessory_5:
                achievementDesc = "Achievement 5";
                break;
            case R.id.accessory_6:
                achievementDesc = "Achievement 6";
                break;
            default:
                break;
        }*/
    }
}
