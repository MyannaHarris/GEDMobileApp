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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sprite extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Accessories gridview
    GridView gridview;
    LayerDrawable spriteDrawable;
    ImageView spriteImage;
    DatabaseHelper dbHelper;
    ArrayList<Integer> glasses;
    ArrayList<Integer> shirts;
    ArrayList<Integer> hats;
    ArrayList<Integer> specials;
    LinearLayout layout;
    Map<String, ArrayList<Integer>> accessoryMap;

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

        makeDictionary();

        glasses = new ArrayList<Integer>();
        glasses.add(R.drawable.sprite_glasses);
        glasses.add(R.drawable.sprite_monocle);
        glasses.add(R.drawable.sprite_nerdglasses);
        glasses.add(R.drawable.sprite_roundglasses);
        glasses.add(R.drawable.sprite_fancyglasses);
        glasses.add(R.drawable.sprite_grannyglasses);

        layout = (LinearLayout) findViewById(R.id.linear_sprite);
        for (int i = 0; i < glasses.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), glasses.get(i)));
            imageView.setTag(glasses.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_glasses);
                }
            });
        }

        // Fill gridview with sprite accessories
        // First show glasses
        /*gridview = (GridView) this.findViewById(R.id.sprite_gridView);
        Integer[] buttonPictures = {
                R.drawable.sprite_glasses,
                R.drawable.sprite_monocle,
                R.drawable.sprite_nerdglasses,
                R.drawable.sprite_roundglasses,
                R.drawable.sprite_fancyglasses,
                R.drawable.sprite_grannyglasses
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
                                R.drawable.sprite_fancyglasses);
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
                                R.drawable.sprite_grannyglasses);
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
        });*/
    }

    /*
     * sets the sprite drawable
     * hides bottom navigation bar
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        spriteImage.setImageDrawable(spriteDrawable);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        // Stop spinner from bringing top bar down
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Spinner spinner = (Spinner) findViewById(R.id.accessories_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accessories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
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
     * Add accessory to sprite
     */
    public void addAccessory(int accessory, int layer) {
        Drawable newItem;
        Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                R.drawable.sprite_blank);
        Drawable oldItem = spriteDrawable.findDrawableByLayerId(layer);

        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this, accessory);
        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
            spriteDrawable.setDrawableByLayerId(layer, blankItem);
        } else {
            spriteDrawable.setDrawableByLayerId(layer, newItem);
        }
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();
    }

    /*
     * Called from glasses button
     * Shows glasses accessories
     */
    public void showGlasses(View view) {
        layout.removeAllViews();

        for (int i = 0; i < glasses.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), glasses.get(i)));
            imageView.setTag(glasses.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_glasses);
                }
            });
        }

        /*Integer[] buttonPictures = {
                R.drawable.sprite_glasses,
                R.drawable.sprite_monocle,
                R.drawable.sprite_nerdglasses,
                R.drawable.sprite_roundglasses,
                R.drawable.sprite_fancyglasses,
                R.drawable.sprite_grannyglasses
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
                                R.drawable.sprite_fancyglasses);
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
                                R.drawable.sprite_grannyglasses);
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
        });*/
    }

    /*
     * Called from shirt button
     * Shows shirt accessories
     */
    public void showShirts(View view) {
        layout.removeAllViews();

        shirts = new ArrayList<Integer>();
        shirts.add(R.drawable.sprite_shirt_long);
        shirts.add(R.drawable.sprite_shirt_long_green);
        shirts.add(R.drawable.sprite_shirt_short);
        shirts.add(R.drawable.sprite_shirt_short_red);
        shirts.add(R.drawable.sprite_fancyshirt);
        shirts.add(R.drawable.sprite_tropicalshirt);

        for (int i = 0; i < shirts.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), shirts.get(i)));
            imageView.setTag(shirts.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_shirt);
                }
            });
        }

        /*Integer[] buttonPictures = {
                R.drawable.sprite_shirt_long,
                R.drawable.sprite_shirt_long_green,
                R.drawable.sprite_shirt_short,
                R.drawable.sprite_shirt_short_red,
                R.drawable.sprite_fancyshirt,
                R.drawable.sprite_tropicalshirt
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
                                R.drawable.sprite_fancyshirt);
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
                                R.drawable.sprite_tropicalshirt);
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
        });*/
    }

    /*
     * Called from bling button
     * Shows bling accessories
     */
    public void showSpecial(View view) {
        layout.removeAllViews();

        ArrayList<Integer> handItems = new ArrayList<Integer>();
        handItems.add(R.drawable.sprite_cane);
        handItems.add(R.drawable.sprite_sword);

        for (int i = 0; i < handItems.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), handItems.get(i)));
            imageView.setTag(handItems.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_handItem);
                }
            });
        }

        ArrayList<Integer> hatItems = new ArrayList<Integer>();
        hatItems.add(R.drawable.sprite_partyhat);
        hatItems.add(R.drawable.sprite_redribbonhat);

        for (int i = 0; i < hatItems.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), hatItems.get(i)));
            imageView.setTag(hatItems.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_hat);
                }
            });
        }

        ArrayList<Integer> shirtItems = new ArrayList<Integer>();
        shirtItems.add(R.drawable.sprite_armor);

        ImageView imageViewShirt = new ImageView(this);
        imageViewShirt.setId(0);
        imageViewShirt.setPadding(8, 8, 8, 8);
        imageViewShirt.setLayoutParams(
                new GridView.LayoutParams(255, 255));
        imageViewShirt.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), shirtItems.get(0)));
        imageViewShirt.setTag(shirtItems.get(0));
        imageViewShirt.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(imageViewShirt);
        imageViewShirt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                addAccessory((int)imageView.getTag(), R.id.accessory_shirt);
            }
        });

        ArrayList<Integer> wingItems = new ArrayList<Integer>();
        wingItems.add(R.drawable.sprite_treasure);

        ImageView imageViewWing = new ImageView(this);
        imageViewWing.setId(0);
        imageViewWing.setPadding(8, 8, 8, 8);
        imageViewWing.setLayoutParams(
                new GridView.LayoutParams(255, 255));
        imageViewWing.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), wingItems.get(0)));
        imageViewWing.setTag(wingItems.get(0));
        imageViewWing.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(imageViewWing);
        imageViewWing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                addAccessory((int)imageView.getTag(), R.id.accessory_wingItem);
            }
        });

        /*Integer[] buttonPictures = {
                R.drawable.sprite_cane,
                R.drawable.sprite_partyhat,
                R.drawable.sprite_redribbonhat,
                R.drawable.sprite_armor,
                R.drawable.sprite_sword,
                R.drawable.sprite_treasure
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
                                R.drawable.sprite_armor);
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
                                R.drawable.sprite_sword);
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
                                R.drawable.sprite_treasure);
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
        });*/
    }

    /*
     * Called from hat button
     * Shows hat accessories
     */
    public void showHats(View view) {
        layout.removeAllViews();

        hats = new ArrayList<Integer>();
        hats.add(R.drawable.sprite_brownhat);
        hats.add(R.drawable.sprite_hat_baseball);
        hats.add(R.drawable.sprite_hat_baseball_camo);
        hats.add(R.drawable.sprite_hat_baseball_red);
        hats.add(R.drawable.sprite_tophat);
        hats.add(R.drawable.sprite_ribbonhat);

        for (int i = 0; i < hats.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), hats.get(i)));
            imageView.setTag(hats.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((int)imageView.getTag(), R.id.accessory_hat);
                }
            });
        }

        /*Integer[] buttonPictures = {
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
        });*/
    }

    /*
     * Called from save photo button
     * Saves copy of dragon and accessories to camera roll
     */
    public void savePhoto(View view) {

        spriteImage.buildDrawingCache();
        Bitmap bm = spriteImage.getDrawingCache();

        int width = spriteDrawable.getIntrinsicWidth();
        int height = spriteDrawable.getIntrinsicHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        spriteDrawable.draw(new Canvas(b));

        //values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis()/1000);

        MediaStore.Images.Media.insertImage(getContentResolver(), b, "Dragon" , "Dragon");

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("The picture was saved!")
                .setTitle("Success!");

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
     * Called from done button
     * Saves changes to dragon
     */
    public void done(View view) {

        Intent intentHome = new Intent(this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showGlasses(view);
                break;
            case 1:
                showShirts(view);
                break;
            case 2:
                showHats(view);
                break;
            case 3:
                showSpecial(view);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    /*
     * Make dictionary for accessories
     */
    public void makeDictionary() {

        accessoryMap = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_glasses);
        ids.add(R.drawable.sprite_glasses);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_glasses", ids);
        ids.clear();
        ids.add(R.drawable.sprite_monocle);
        ids.add(R.drawable.sprite_monocle);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_monocle", ids);
        ids.clear();
        ids.add(R.drawable.sprite_nerdglasses);
        ids.add(R.drawable.sprite_nerdglasses);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_nerdglasses", ids);
        ids.clear();
        ids.add(R.drawable.sprite_roundglasses);
        ids.add(R.drawable.sprite_roundglasses);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_roundglasses", ids);
        ids.clear();
        ids.add(R.drawable.sprite_fancyglasses);
        ids.add(R.drawable.sprite_fancyglasses);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_fancyglasses", ids);
        ids.clear();
        ids.add(R.drawable.sprite_grannyglasses);
        ids.add(R.drawable.sprite_grannyglasses);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_grannyglasses", ids);
        ids.clear();
        ids.add(R.drawable.sprite_brownhat);
        ids.add(R.drawable.sprite_brownhat);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_brownhat", ids);
        ids.clear();
        ids.add(R.drawable.sprite_hat_baseball);
        ids.add(R.drawable.sprite_hat_baseball);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball", ids);
        ids.clear();
        ids.add(R.drawable.sprite_hat_baseball_camo);
        ids.add(R.drawable.sprite_hat_baseball_camo);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_camo", ids);
        ids.clear();
        ids.add(R.drawable.sprite_hat_baseball_red);
        ids.add(R.drawable.sprite_hat_baseball_red);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_red", ids);
        ids.clear();
        ids.add(R.drawable.sprite_tophat);
        ids.add(R.drawable.sprite_tophat);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_tophat", ids);
        ids.clear();
        ids.add(R.drawable.sprite_ribbonhat);
        ids.add(R.drawable.sprite_ribbonhat);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_ribbonhat", ids);
        ids.clear();
        ids.add(R.drawable.sprite_shirt_long);
        ids.add(R.drawable.sprite_shirt_long);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long", ids);
        ids.clear();
        ids.add(R.drawable.sprite_shirt_long_green);
        ids.add(R.drawable.sprite_shirt_long_green);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long_green", ids);
        ids.clear();
        ids.add(R.drawable.sprite_shirt_short);
        ids.add(R.drawable.sprite_shirt_short);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short", ids);
        ids.clear();
        ids.add(R.drawable.sprite_shirt_short_red);
        ids.add(R.drawable.sprite_shirt_short_red);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short_red", ids);
        ids.clear();
        ids.add(R.drawable.sprite_fancyshirt);
        ids.add(R.drawable.sprite_fancyshirt);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_fancyshirt", ids);
        ids.clear();
        ids.add(R.drawable.sprite_tropicalshirt);
        ids.add(R.drawable.sprite_tropicalshirt);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_tropicalshirt", ids);
        ids.clear();
        ids.add(R.drawable.sprite_cane);
        ids.add(R.drawable.sprite_cane);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_cane", ids);
        ids.clear();
        ids.add(R.drawable.sprite_partyhat);
        ids.add(R.drawable.sprite_partyhat);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_partyhat", ids);
        ids.clear();
        ids.add(R.drawable.sprite_redribbonhat);
        ids.add(R.drawable.sprite_redribbonhat);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_redribbonhat", ids);
        ids.clear();
        ids.add(R.drawable.sprite_armor);
        ids.add(R.drawable.sprite_armor);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_armor", ids);
        ids.clear();
        ids.add(R.drawable.sprite_sword);
        ids.add(R.drawable.sprite_sword);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_sword", ids);
        ids.clear();
        ids.add(R.drawable.sprite_treasure);
        ids.add(R.drawable.sprite_treasure);
        ids.add(R.id.accessory_wingItem);
        accessoryMap.put("sprite_treasure", ids);
        ids.clear();
        /*ids.add(R.drawable.sprite_blank);
        ids.add(R.drawable.sprite_blank);
        accessoryMap.put("sprite_blank", ids);
        ids.clear();*/
    }
}
