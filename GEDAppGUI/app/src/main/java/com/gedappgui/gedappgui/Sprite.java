/*
 * Sprite.java
 *
 * Sprite page activity
 *
 * Shows sprite and unlocked accessories
 * User can select accessories to place on sprite
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-6-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Sprite extends AppCompatActivity {

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprite);

        // Allow homaAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
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

    /*
     * Sets what menu will be in the action bar
     * homeonlymenu has the settings button and the home button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /*
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
     * settings = goes to settings page
     * android.R.id.home = go to the activity that called the current activity
     */
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
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    /*
     * Called from glasses button
     * Shows glasses accessories
     */
    public void showGlasses(View view) {

    }

    /*
     * Called from shirt button
     * Shows shirt accessories
     */
    public void showShirts(View view) {

    }

    /*
     * Called from bling button
     * Shows bling accessories
     */
    public void showBling(View view) {

    }

    /*
     * Called from hat button
     * Shows hat accessories
     */
    public void showHats(View view) {

    }

    /*
     * called when an accessory is selected
     * Puts accessory on sprite
     */
    public void addAccessory(View view) {
        /*LayerDrawable layerList =
                (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.layers);
        Drawable layer;

        switch(view.getId()) {
            case R.id.accessory_1:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_1);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            case R.id.accessory_2:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_2);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            case R.id.accessory_3:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_3);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            case R.id.accessory_4:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_1);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            case R.id.accessory_5:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_2);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            case R.id.accessory_6:
                layer =  layerList.findDrawableByLayerId(R.id.accessory_picture_3);
                if (layer.isVisible()==true)
                {
                    layer.setAlpha(0);
                }
                else
                {
                    layer.setAlpha(255);
                }
                break;
            default:
                break;
        }*/
    }
}
