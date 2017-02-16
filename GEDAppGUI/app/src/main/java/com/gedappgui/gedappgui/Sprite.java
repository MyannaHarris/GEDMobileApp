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
 * Last Edit: 11-17-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private LayerDrawable spriteDrawable;
    private ImageView spriteImage;
    private DatabaseHelper dbHelper;
    private ArrayList<String> glasses;
    private ArrayList<String> shirts;
    private ArrayList<String> hats;
    private ArrayList<String> specials;
    private ArrayList<String> allAccessories;
    private ArrayList<String> dragons;
    private int currDragon = 0;

    private LinearLayout layout;
    private Map<String, ArrayList<Integer>> accessoryMap;

    // Save x values for swiping on dragon
    private int x1 = 0;
    private int x2 = 0;
    private boolean inDragon = false;

    // Screen size
    int width;
    int height;

    // Drag image
    private ImageView currAccessory;
    private RelativeLayout spriteLayout;
    private boolean dragBool = false;
    private String dragIcon;

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
        spriteImage = (ImageView) findViewById(R.id.sprite_spriteScreen);

        // instantiate lists
        allAccessories = new ArrayList<String>();
        specials = new ArrayList<String>();
        glasses = new ArrayList<String>();
        shirts = new ArrayList<String>();
        hats = new ArrayList<String>();
        dragons = new ArrayList<String>();
        accessoryMap = new HashMap<String, ArrayList<Integer>>();

        // make dictionary of image ids
        makeDictionary();

        // Read in accessory data
        // 0 - accessory_img = name
        // 1 - layer_id
        // 2 - currently_wearing
        dbHelper = new DatabaseHelper(this);
        final ArrayList<ArrayList<String>> accessories = dbHelper.selectAccessories();

        // Save what accessories should be displayed
        if (accessories != null && accessories.size() > 0) {
            for (int i = 0; i < accessories.size(); i++) {

                // Add accessory to all list
                if (!accessories.get(i).get(1).equals("5")) {
                    allAccessories.add(accessories.get(i).get(0));
                }

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
                } else if (accessories.get(i).get(1).equals("2")) {
                    hats.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } else if (accessories.get(i).get(1).equals("3")) {
                    shirts.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } else if (accessories.get(i).get(1).equals("4")) {
                    specials.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                } else if (accessories.get(i).get(1).equals("5")) {
                    dragons.add(accessories.get(i).get(0));

                    // Set dragon
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                        currDragon = dragons.size() - 1;
                    }
                }
            }
        }

        // Set variables for dragging image
        spriteLayout = (RelativeLayout)findViewById(R.id.sprite_relativeLayout);
        currAccessory = new ImageView(Sprite.this);

        // Get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        // First display all accessories
        layout = (LinearLayout) findViewById(R.id.linear_sprite);
        for (int i = 0; i < allAccessories.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(allAccessories.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(allAccessories.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();

        ImageView dragonImageView = (ImageView) findViewById(R.id.sprite_spriteScreen);
        dragonImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        inDragon = true;
                        x1 = (int)event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = (int)event.getRawX();
                        if(inDragon && x1 > x2) {
                            if (currDragon == dragons.size() - 1) {
                                currDragon = 0;
                                addAccessory(dragons.get(currDragon));
                            } else {
                                currDragon += 1;
                                addAccessory(dragons.get(currDragon));
                            }
                        } else if (inDragon && x2 > x1){
                            if (currDragon == 0) {
                                currDragon = dragons.size() - 1;
                                addAccessory(dragons.get(currDragon));
                            } else {
                                currDragon -= 1;
                                addAccessory(dragons.get(currDragon));
                            }
                        }
                        inDragon = false;
                        break;
                }
                checkAchievements();
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if(dragBool) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (currAccessory != null) {
                        currAccessory.setVisibility(View.GONE);
                        spriteLayout.removeView(currAccessory);
                    }

                    ArrayList<Integer> info = accessoryMap.get(dragIcon);
                    int icon = info.get(1);

                    currAccessory = new ImageView(Sprite.this);
                    currAccessory.setPadding(8, 8, 8, 8);
                    currAccessory.setLayoutParams(new RelativeLayout.LayoutParams(255, 255));
                    currAccessory.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), icon));

                    spriteLayout.addView(currAccessory);

                    currAccessory.setX(event.getRawX() - 190);
                    currAccessory.setY(event.getRawY() - 300);
                    break;
                case MotionEvent.ACTION_UP:
                    currAccessory.setVisibility(View.GONE);
                    spriteLayout.removeView(currAccessory);

                    ImageView dragonImageView = (ImageView)findViewById(R.id.sprite_spriteScreen);
                    Rect editTextRect = new Rect();
                    dragonImageView.getHitRect(editTextRect);

                    if (editTextRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        addAccessory(dragIcon);
                    }
                    dragBool = false;
                    break;
            }
        }

        return super.dispatchTouchEvent(event);
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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

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

        checkAchievements();
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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
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
                Intent intentHomeSprite = new Intent(this, MainActivity.class);
                intentHomeSprite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHomeSprite);
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

    private void checkAchievements() {
        System.out.println(dbHelper.countAccessoriesEarned());

        //gives an achievement if the user make their sprite wear 5 accessories
        if (dbHelper.countAccessoriesWorn() == 6) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 18);
            startActivity(achievement);
        }

        //gives an achievement if the user dresses their sprite with a monocle, top hat and cane
        if (dbHelper.isFancy()) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 19);
            startActivity(achievement);
        }

        //gives an achievement if the user dresses their sprite with a party hat
        if (dbHelper.isWearingPartyHat()) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 20);
            startActivity(achievement);
        }

        /*
        //gives an achievement if the user earns 3 accessories
        if(dbHelper.countAccessoriesEarned() >= 3){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 15);
            startActivity(achievement);
        }

        //gives an achievement if the user earns 8 accessories
        if(dbHelper.countAccessoriesEarned() >= 8){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 16);
            startActivity(achievement);
        }

        //gives an achievement if the user earns all accessories
        if(dbHelper.countAccessoriesEarned() >= 24){
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 17);
            startActivity(achievement);
        }
        */
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

        int img = 0;
        int icon = 0;
        int layer = 0;

        if (info.size() > 2) {
            img = info.get(0);
            icon = info.get(1);
            layer = info.get(2);
        } else {
            img = info.get(0);
            layer = info.get(1);
        }

        // Draw accessory on dragon
        Drawable newItem;
        newItem = (Drawable) ContextCompat.getDrawable(Sprite.this, img);
        spriteDrawable.setDrawableByLayerId(layer, newItem);
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();

        checkAchievements();
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

        int img = 0;
        int icon = 0;
        int layer = 0;

        if (info.size() > 2) {
            img = info.get(0);
            icon = info.get(1);
            layer = info.get(2);
        } else {
            img = info.get(0);
            layer = info.get(1);
        }

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
            } else if (layer == R.id.accessory_dragon) {
                dbHelper.updateCurrentlyWearing(name, 6);
            }
        }
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();

        checkAchievements();
    }

    /*
     * Called from glasses button
     * Shows glasses accessories
     */
    public void showGlasses(View view) {
        layout.removeAllViews();

        for (int i = 0; i < glasses.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(glasses.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(glasses.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();
    }

    /*
     * Called from shirt button
     * Shows shirt accessories
     */
    public void showShirts(View view) {
        layout.removeAllViews();

        for (int i = 0; i < shirts.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(shirts.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(shirts.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();
    }

    /*
     * Called from bling button
     * Shows bling accessories
     */
    public void showSpecial(View view) {
        layout.removeAllViews();

        for (int i = 0; i < specials.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(specials.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(specials.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();
    }

    /*
     * Called from hat button
     * Shows hat accessories
     */
    public void showHats(View view) {
        layout.removeAllViews();

        for (int i = 0; i < hats.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(hats.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(hats.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();
    }

    /*
     * Called from bling button
     * Shows bling accessories
     */
    public void showAll(View view) {
        layout.removeAllViews();

        for (int i = 0; i < allAccessories.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(allAccessories.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);

            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), icon));
            imageView.setTag(allAccessories.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dragIcon = (String) v.getTag();

                            dragBool = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            ImageView imageView = (ImageView) v;
                            addAccessory((String) imageView.getTag());

                            dragBool = false;
                            break;
                    }
                    return true;
                }
            });
        }

        checkAchievements();
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

        MediaStore.Images.Media.insertImage(getContentResolver(), b, "Dragon", "Dragon");

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogAppearance);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("The picture was saved!")
                .setTitle("Success!");

        builder.setIcon(R.drawable.sprite_dragon);

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
     * Called from dragon image
     * Loops through the different dragon colorings
     */
    public void changeDragon(View view) {
        /*if (currDragon == dragons.size() - 1) {
            currDragon = 0;
            addAccessory(dragons.get(currDragon));
        } else {
            currDragon += 1;
            addAccessory(dragons.get(currDragon));
        }

        checkAchievements();*/
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

        checkAchievements();
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

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon2);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon2", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon3);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon3", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon4);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon4", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon5);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon5", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon6);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon6", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon7);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon7", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_dragon8);
        ids.add(R.id.accessory_dragon);
        accessoryMap.put("sprite_dragon8", ids);
    }
}
