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
 * Last Edit: 1-17-17
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
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sprite extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // NOTE: layer_ids for the database accessories
    // 0 - blank
    // 1 - glasses
    // 2 - hats
    // 3 - shirts
    // 4 - specials
    // 5 - dragons

    // Accessories variables
    LayerDrawable spriteDrawable;
    ImageView spriteImage;
    DatabaseHelper dbHelper;
    ArrayList<String> glasses;
    ArrayList<String> shirts;
    ArrayList<String> hats;
    ArrayList<String> specials;
    ArrayList<String> allAccessories;

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

        // Set up for editing sprite
        spriteDrawable = ((MyApplication) this.getApplication()).getSpriteDrawable();
        spriteImage = (ImageView)findViewById(R.id.sprite_spriteScreen);

        // instantiate lists
        allAccessories = new ArrayList<String>();
        specials = new ArrayList<String>();
        glasses = new ArrayList<String>();
        shirts = new ArrayList<String>();
        hats = new ArrayList<String>();
        accessoryMap = new HashMap<String, ArrayList<Integer>>();

        // make dictionary of image ids
        makeDictionary();

        // Read in accessory data
        // accessory_img, layer_id, currently_wearing
        dbHelper = new DatabaseHelper(this);
        ArrayList<ArrayList<String>> accessories = dbHelper.selectAccessories();

        // Save what accessories should be displayed
        if(accessories != null && accessories.size() > 0) {
            for (int i = 0; i < accessories.size(); i++) {

                // Add accessory to all list
                allAccessories.add(accessories.get(i).get(0));

                // Add accessory to specific list
                // 1 - glasses
                // 2 - hats
                // 3 - shirts
                // 4 - specials
                if (accessories.get(i).get(1).equals("1")) {
                    glasses.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } if (accessories.get(i).get(1).equals("2")) {
                    hats.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } if (accessories.get(i).get(1).equals("3")) {
                    shirts.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } if (accessories.get(i).get(1).equals("4")) {
                    specials.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                }
            }
        }

        // First display all accessories
        layout = (LinearLayout) findViewById(R.id.linear_sprite);
        for (int i = 0; i < allAccessories.size(); i++) {
            ArrayList<Integer> info =  accessoryMap.get(allAccessories.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(allAccessories.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
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
     * Map from name:
     * 0 - actual image id
     * 1 - icon image id
     * 2 - ImageView layer id
     */
    public void addSavedAccessory(String name) {
        // Get info from map
        ArrayList<Integer> info = accessoryMap.get(name);
        int img = info.get(0);
        int icon = info.get(1);
        int layer = info.get(2);

        // Draw accesory on dragon
        Drawable newItem;
        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this, img);
        spriteDrawable.setDrawableByLayerId(layer, newItem);
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();
    }

    /*
     * Add accessory to sprite
     * Map from name:
     * 0 - actual image id
     * 1 - icon image id
     * 2 - ImageView layer id
     */
    public void addAccessory(String name) {
        // Get info from map
        ArrayList<Integer> info = accessoryMap.get(name);
        int img = info.get(0);
        int icon = info.get(1);
        int layer = info.get(2);

        // Draw accesory on dragon
        Drawable newItem;
        Drawable blankItem = (Drawable) ContextCompat.getDrawable(Sprite.this,
                R.drawable.sprite_blank);
        Drawable oldItem = spriteDrawable.findDrawableByLayerId(layer);

        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this, img);
        if (newItem.getConstantState().equals(oldItem.getConstantState())) {
            spriteDrawable.setDrawableByLayerId(layer, blankItem);
            dbHelper.takeOffClothing(name);
        } else {
            spriteDrawable.setDrawableByLayerId(layer, newItem);
            // Set accessory in database
            if (layer == R.id.accessory_glasses) {
                dbHelper.updateCurrentlyWearing(name, 1);
            } else if (layer == R.id.accessory_hat) {
                dbHelper.updateCurrentlyWearing(name, 2);
            } else if (layer == R.id.accessory_shirt) {
                dbHelper.updateCurrentlyWearing(name, 3);
            } else if (layer == R.id.accessory_handItem) {
                dbHelper.updateCurrentlyWearing(name, 4);
            } else if (layer == R.id.accessory_wingItem) {
                dbHelper.updateCurrentlyWearing(name, 5);
            }
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
            ArrayList<Integer> info =  accessoryMap.get(glasses.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(glasses.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
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

        for (int i = 0; i < shirts.size(); i++) {
            ArrayList<Integer> info =  accessoryMap.get(shirts.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(shirts.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
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

        for (int i = 0; i < specials.size(); i++) {
            ArrayList<Integer> info =  accessoryMap.get(specials.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(specials.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
                }
            });
        }

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

        for (int i = 0; i < hats.size(); i++) {
            ArrayList<Integer> info =  accessoryMap.get(hats.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(hats.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
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
     * Called from bling button
     * Shows bling accessories
     */
    public void showAll(View view) {
        layout.removeAllViews();

        for (int i = 0; i < allAccessories.size(); i++) {
            ArrayList<Integer> info =  accessoryMap.get(allAccessories.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams( new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(allAccessories.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    addAccessory((String)imageView.getTag());
                }
            });
        }
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

    /*
     * Called from combo box
     * Changes what accessory list is displayed
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showAll(view);
                break;
            case 1:
                showGlasses(view);
                break;
            case 2:
                showShirts(view);
                break;
            case 3:
                showHats(view);
                break;
            case 4:
                showSpecial(view);
                break;
            default:
                showAll(view);
                break;
        }
    }

    /*
     * Called from combobox
     * Makes the auto-selection to be "All"
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    /*
     * Make dictionary for accessories
     */
    public void makeDictionary() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_glasses);
        ids.add(R.drawable.sprite_glasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_glasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_monocle);
        ids.add(R.drawable.sprite_monocle_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_monocle", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_nerdglasses);
        ids.add(R.drawable.sprite_nerdglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_nerdglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_roundglasses);
        ids.add(R.drawable.sprite_roundglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_roundglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyglasses);
        ids.add(R.drawable.sprite_fancyglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_fancyglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_grannyglasses);
        ids.add(R.drawable.sprite_grannyglasses_icon);
        ids.add(R.id.accessory_glasses);
        accessoryMap.put("sprite_grannyglasses", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_brownhat);
        ids.add(R.drawable.sprite_brownhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_brownhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball);
        ids.add(R.drawable.sprite_hat_baseball_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_camo);
        ids.add(R.drawable.sprite_hat_baseball_camo_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_camo", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_red);
        ids.add(R.drawable.sprite_hat_baseball_red_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_hat_baseball_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tophat);
        ids.add(R.drawable.sprite_tophat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_tophat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_ribbonhat);
        ids.add(R.drawable.sprite_ribbonhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_ribbonhat", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long);
        ids.add(R.drawable.sprite_shirt_long_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long_green);
        ids.add(R.drawable.sprite_shirt_long_green_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_long_green", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short);
        ids.add(R.drawable.sprite_shirt_short_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short_red);
        ids.add(R.drawable.sprite_shirt_short_red_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_shirt_short_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyshirt);
        ids.add(R.drawable.sprite_fancyshirt_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_fancyshirt", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tropicalshirt);
        ids.add(R.drawable.sprite_tropicalshirt_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_tropicalshirt", ids);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_cane);
        ids.add(R.drawable.sprite_cane_icon);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_cane", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_partyhat);
        ids.add(R.drawable.sprite_partyhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_partyhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_redribbonhat);
        ids.add(R.drawable.sprite_redribbonhat_icon);
        ids.add(R.id.accessory_hat);
        accessoryMap.put("sprite_redribbonhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_armor);
        ids.add(R.drawable.sprite_armor_icon);
        ids.add(R.id.accessory_shirt);
        accessoryMap.put("sprite_armor", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_sword);
        ids.add(R.drawable.sprite_sword_icon);
        ids.add(R.id.accessory_handItem);
        accessoryMap.put("sprite_sword", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_treasure);
        ids.add(R.drawable.sprite_treasure_icon);
        ids.add(R.id.accessory_wingItem);
        accessoryMap.put("sprite_treasure", ids);

        /*ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_blank);
        ids.add(R.drawable.sprite_blank);
        accessoryMap.put("sprite_blank", ids);
        ids.clear();*/
    }
}
