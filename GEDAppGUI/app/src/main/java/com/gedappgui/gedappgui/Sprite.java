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
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class Sprite extends AppCompatActivity {

    // Accessories gridview
    GridView gridview;

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

        gridview = (GridView) this.findViewById(R.id.sprite_gridView);
        Integer[] buttonPictures = {
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
    }

    /*
     * Registers onItemClickListener
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();

        // *************************************************************************************
        // ****** Could create problems when all the different glasses, hats, etc work *********
        // *************************************************************************************

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String test = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        test = "test 1";
                        break;
                    case 1:
                        test = "test 2";
                        break;
                    case 2:
                        test = "test 3";
                        break;
                    case 3:
                        test = "test 4";
                        break;
                    case 4:
                        test = "test 4";
                        break;
                    case 5:
                        test = "test 4";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
     * UnRegisters onItemClickListener
     * Called every time this activity loses the focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        gridview.setOnItemClickListener(null);
    }

    /*
     * UnRegisters onItemClickListener
     * Called every time this activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gridview.setOnItemClickListener(null);
    }

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
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
                /*Intent intentHomeSprite = new Intent(this, MainActivity.class);
                intentHomeSprite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeSprite);*/
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
        Integer[] buttonPictures = {
                R.drawable.home_button,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String test = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        test = "test 1";
                        break;
                    case 1:
                        test = "test 2";
                        break;
                    case 2:
                        test = "test 3";
                        break;
                    case 3:
                        test = "test 4";
                        break;
                    case 4:
                        test = "test 4";
                        break;
                    case 5:
                        test = "test 4";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
     * Called from shirt button
     * Shows shirt accessories
     */
    public void showShirts(View view) {
        Integer[] buttonPictures = {
                R.drawable.example_picture,
                R.drawable.home_button,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String test = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        test = "test 1";
                        break;
                    case 1:
                        test = "test 2";
                        break;
                    case 2:
                        test = "test 3";
                        break;
                    case 3:
                        test = "test 4";
                        break;
                    case 4:
                        test = "test 4";
                        break;
                    case 5:
                        test = "test 4";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
     * Called from bling button
     * Shows bling accessories
     */
    public void showBling(View view) {
        Integer[] buttonPictures = {
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.home_button,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String test = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        test = "test 1";
                        break;
                    case 1:
                        test = "test 2";
                        break;
                    case 2:
                        test = "test 3";
                        break;
                    case 3:
                        test = "test 4";
                        break;
                    case 4:
                        test = "test 4";
                        break;
                    case 5:
                        test = "test 4";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
     * Called from hat button
     * Shows hat accessories
     */
    public void showHats(View view) {
        Integer[] buttonPictures = {
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.home_button,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String test = "";

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        test = "test 1";
                        break;
                    case 1:
                        test = "test 2";
                        break;
                    case 2:
                        test = "test 3";
                        break;
                    case 3:
                        test = "test 4";
                        break;
                    case 4:
                        test = "test 4";
                        break;
                    case 5:
                        test = "test 4";
                        break;
                    default:
                        break;
                }
            }
        });
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
