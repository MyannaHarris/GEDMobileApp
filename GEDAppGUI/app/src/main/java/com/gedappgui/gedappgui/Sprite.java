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
 * Last Edit: 11-27-16
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
import android.widget.ImageView;

public class Sprite extends AppCompatActivity {

    // Accessories gridview
    GridView gridview;
    LayerDrawable spriteDrawable;
    ImageView spriteImage;

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sprite);

        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        spriteDrawable = ((MyApplication) this.getApplication()).getSpriteDrawable();
        spriteImage = (ImageView)findViewById(R.id.sprite_spriteScreen);

        // Fill gridview with sprite accessories
        // First show glasses
        gridview = (GridView) this.findViewById(R.id.sprite_gridView);
        Integer[] buttonPictures = {
                R.drawable.sprite_glasses,
                R.drawable.sprite_monocle,
                R.drawable.sprite_nerdglasses,
                R.drawable.sprite_roundglasses,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Drawable newItem;
                Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                        R.drawable.sprite_blank);
                Drawable oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_glasses);

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_glasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 1:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_monocle);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 2:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_nerdglasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 3:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_roundglasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 4:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 5:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*
     * sets the sprite drawable
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        spriteImage.setImageDrawable(spriteDrawable);
    }

    /*
     * sets the sprite drawable global variable
     * Called when activity loses focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        ((MyApplication)
                Sprite.this.getApplication()).setSpriteDrawable(spriteDrawable);
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
                R.drawable.sprite_glasses,
                R.drawable.sprite_monocle,
                R.drawable.sprite_nerdglasses,
                R.drawable.sprite_roundglasses,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Drawable newItem;
                Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                        R.drawable.sprite_blank);
                Drawable oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_glasses);

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_glasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 1:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_monocle);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 2:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_nerdglasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 3:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_roundglasses);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 4:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 5:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_glasses, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
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
                R.drawable.sprite_shirt_long,
                R.drawable.sprite_shirt_long_green,
                R.drawable.sprite_shirt_short,
                R.drawable.sprite_shirt_short_red,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Drawable newItem;
                Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                        R.drawable.sprite_blank);
                Drawable oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_shirt);

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_shirt_long);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 1:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_shirt_long_green);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 2:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_shirt_short);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 3:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_shirt_short_red);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 4:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 5:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
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
    public void showSpecial(View view) {
        Integer[] buttonPictures = {
                R.drawable.sprite_cane,
                R.drawable.sprite_partyhat,
                R.drawable.sprite_redribbonhat,
                R.drawable.example_picture,
                R.drawable.example_picture,
                R.drawable.example_picture
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Drawable newItem;
                Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                        R.drawable.sprite_blank);
                Drawable oldItem;

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_handItem);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_cane);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_handItem, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_handItem, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 1:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_hat);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_partyhat);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 2:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_hat);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_redribbonhat);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 3:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_shirt);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_shirt, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 4:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_handItem);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_handItem, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_handItem, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 5:
                        oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_wingItem);
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_blank);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_wingItem, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_wingItem, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
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
                R.drawable.sprite_brownhat,
                R.drawable.sprite_hat_baseball,
                R.drawable.sprite_hat_baseball_camo,
                R.drawable.sprite_hat_baseball_red,
                R.drawable.sprite_tophat,
                R.drawable.sprite_ribbonhat
        };
        gridview.setAdapter(new ButtonAdapter(this, buttonPictures));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Drawable newItem;
                Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                        R.drawable.sprite_blank);
                Drawable oldItem = spriteDrawable.findDrawableByLayerId(R.id.accessory_hat);

                // Preform a function based on the position
                switch (position) {
                    case 0:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_brownhat);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 1:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_hat_baseball);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 2:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_hat_baseball_camo);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 3:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_hat_baseball_red);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 4:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_tophat);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
                        break;
                    case 5:
                        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                                R.drawable.sprite_ribbonhat);
                        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, blankItem);
                        } else {
                            spriteDrawable.setDrawableByLayerId(R.id.accessory_hat, newItem);
                        }
                        spriteImage.setImageDrawable(spriteDrawable);
                        spriteImage.invalidate();
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
