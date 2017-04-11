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
 * Last Edit: 4-8-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

    // Layout for the accessories
    private LinearLayout layout;
    // Map from accessory names to drawable ids
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

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
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

        // Get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        // Dynamically set imageview size
        RelativeLayout.LayoutParams relativeLay = (RelativeLayout.LayoutParams)spriteImage.getLayoutParams();
        relativeLay.height = (int)(width * 4 / 6);
        relativeLay.width = (int)(width * 4 / 6);

        // Change text size to be dynamic
        Button button = (Button) findViewById(R.id.sprite_savePhoto);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        button = (Button) findViewById(R.id.sprite_removeAll);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
        button = (Button) findViewById(R.id.sprite_done);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));

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

        // Ask for dragon name if this is the first time on this page
        if(dbHelper.selectDragonName() == null || dbHelper.selectDragonName().equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogAppearance);

            alert.setTitle("Please set your dragon's name:");

            // Set an EditText view to get user input
            final EditText inputText = new EditText(this);
            inputText.setHint("Dragon name");
            inputText.setHintTextColor(ContextCompat.getColor(this, R.color.colorHint));
            inputText.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(10)
            });

            // Add editview box to alert pop-up
            alert.setView(inputText);

            // Saves dragons name and makes first letter uppercase and closes alert
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Read input
                    String lowerName = inputText.getText().toString();
                    String newName;

                    if (!lowerName.equals("") && lowerName.length() > 0) {
                        // Make first letter uppercase
                        if (lowerName.length() < 2) {
                            newName = lowerName.substring(0, 1).toUpperCase();
                            dbHelper.updateDragonName(newName);
                            ((MyApplication) Sprite.this.getApplication()).setDragonName(newName);
                            setTitle(newName + "'s Lair");
                        } else {
                            newName = lowerName.substring(0, 1).toUpperCase() + lowerName.substring(1);
                            dbHelper.updateDragonName(newName);
                            ((MyApplication) Sprite.this.getApplication()).setDragonName(newName);
                            setTitle(newName + "'s Lair");
                        }
                    } else {
                        // If no name provided, make the name "Unknown"
                        newName = "Unknown";
                        dbHelper.updateDragonName(newName);
                        ((MyApplication) Sprite.this.getApplication()).setDragonName(newName);
                        setTitle(newName + "'s Lair");
                    }
                }
            });

            // Creates and shows alert
            AlertDialog dialog = alert.create();
            dialog.show();
        }

        // Get accessory info from database
        final ArrayList<ArrayList<String>> accessories = dbHelper.selectAccessories();

        // Save what accessories should be displayed
        if (accessories != null && accessories.size() > 0) {
            for (int i = 0; i < accessories.size(); i++) {

                // Add accessory to all list
                if (!accessories.get(i).get(1).equals("5")) {
                    allAccessories.add(accessories.get(i).get(0));

                    // Put accessory on dragon if need be
                    if (accessories.get(i).get(2).equals("1")) {
                        addSavedAccessory(accessories.get(i).get(0));
                    }
                }

                // Add accessory to specific list
                // 1 - glasses
                // 2 - hats
                // 3 - shirts
                // 4 - specials
                if (accessories.get(i).get(1).equals("1")) {
                    glasses.add(accessories.get(i).get(0));

                } else if (accessories.get(i).get(1).equals("2")) {
                    hats.add(accessories.get(i).get(0));

                } else if (accessories.get(i).get(1).equals("3")) {
                    shirts.add(accessories.get(i).get(0));

                } else if (accessories.get(i).get(1).equals("4")) {
                    specials.add(accessories.get(i).get(0));

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

        // First display all accessories
        layout = (LinearLayout) findViewById(R.id.linear_sprite);

        checkAchievements();

        ImageView dragonImageView = (ImageView) findViewById(R.id.sprite_spriteScreen);
        dragonImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Read ouch in dragon to see if a swipe to change the color happens
                        inDragon = true;
                        x1 = (int)event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Read the new x coordinate to see if a swipe left or a swipe right happened
                        x2 = (int)event.getRawX();
                        if(inDragon && x1 > x2) {
                            // Right swipe
                            if (currDragon == dragons.size() - 1) {
                                currDragon = 0;
                                addAccessory(dragons.get(currDragon));
                            } else {
                                currDragon += 1;
                                addAccessory(dragons.get(currDragon));
                            }
                        } else if (inDragon && x2 > x1){
                            // Left swipe
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
                return true;
            }
        });
    }

    /**
     * Listens for the back button on the bottom navigation bar
     * Goes to home page
     */
    @Override
    public void onBackPressed() {
        Intent intentHomeSprite = new Intent(this, MainActivity.class);
        intentHomeSprite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHomeSprite);
    }

    /**
     * Sends event to listener then process touch itself
     * @param event Motion event
     * @return super.dispatchTouchEvent(event) send event to other listeners
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(dragBool) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (currAccessory != null) {
                        // Remove dragging accessory imageView before moving it
                        currAccessory.setVisibility(View.GONE);
                        spriteLayout.removeView(currAccessory);
                    }

                    // Get icon to drag
                    ArrayList<Integer> info = accessoryMap.get(dragIcon);
                    int icon = info.get(1);

                    // Create new imageview to drag
                    currAccessory = new ImageView(Sprite.this);
                    currAccessory.setPadding(8, 8, 8, 8);
                    currAccessory.setLayoutParams(new RelativeLayout.LayoutParams(255, 255));
                    currAccessory.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), icon));

                    // Add imageview to activity layout
                    spriteLayout.addView(currAccessory);

                    // Move accessory imageview
                    currAccessory.setX(event.getRawX() - 190);
                    currAccessory.setY(event.getRawY() - 300);
                    break;
                case MotionEvent.ACTION_UP:
                    // Remove dragging imageview from activity layout when it is dropped
                    currAccessory.setVisibility(View.GONE);
                    spriteLayout.removeView(currAccessory);

                    // Get rectangle around dragon imageview
                    ImageView dragonImageView = (ImageView)findViewById(R.id.sprite_spriteScreen);
                    Rect editTextRect = new Rect();
                    dragonImageView.getHitRect(editTextRect);

                    // If accessory dropped on dragon, add to dragon and database
                    if (editTextRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        addAccessory(dragIcon);
                    }
                    // Stop dragging
                    dragBool = false;
                    break;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * Sets the sprite drawable
     * Hides bottom navigation bar
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

        // Create an ArrayAdapter using the string array and a special spinner layout
        // make text dynamically size
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, R.layout.spinner_larger_text,
                getResources().getTextArray(R.array.accessories_array)) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView)
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v instanceof TextView)
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/35));
                return v;
            }
        };

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        // Get dragon name to update title of the page
        String dragonName = ((MyApplication) this.getApplication()).getDragonName();
        if (dragonName != null && !dragonName.equals("")) {
            setTitle(dragonName + "'s Lair");
        }
    }

    /**
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
     * @param hasFocus true or false based on if the focus of the window changes to this activity
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

    /**
     * Sets the sprite drawable global variable
     * Called when activity loses focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        ((MyApplication)
                Sprite.this.getApplication()).setSpriteDrawable(spriteDrawable);
    }

    /**
     * Sets what menu will be in the action bar
     * homeonlymenu has the settings button and the home button
     * @param menu The options menu in which we place the items.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeonlymenu, menu);
        return true;
    }

    /**
     * Listens for selections from the menu in the action bar
     * Does action corresponding to selected item
     * home = goes to homescreen
     * settings = goes to settings page
     * android.R.id.home = go to the activity that called the current activity
     * @param item that is selected from the menu in the action bar
     * @return true
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
            // Action with ID action_refresh was selected
            case R.id.action_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;
            // Action with ID action_settings was selected
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Check if user has earned an achievement
     */
    private void checkAchievements() {
        // Gives an achievement if the user make their sprite wear 5 accessories
        if (dbHelper.countAccessoriesWorn() == 6) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 18);
            startActivity(achievement);
        }

        // Gives an achievement if the user dresses their sprite with a monocle, top hat and cane
        if (dbHelper.isFancy()) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 19);
            startActivity(achievement);
        }

        // Gives an achievement if the user dresses their sprite with a party hat
        if (dbHelper.isWearingPartyHat()) {
            Intent achievement = new Intent(this, AchievementPopUp.class);
            achievement.putExtra("achievementID", 20);
            startActivity(achievement);
        }
    }

    /**
     * Add accessory that is saved in the database already to sprite
     * Map from name:
     * 0 - actual image id
     * 1 - icon image id
     * 2 - ImageView layer id
     * @param name The string name of the accessory
     */
    public void addSavedAccessory(String name) {
        // Get info from map
        ArrayList<Integer> info = accessoryMap.get(name);

        int img = 0;
        int icon = 0;
        int layer = 0;

        // For when the dragon is changed since it only has the img and layer
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

        // Set new imageview content for dragon
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();

        checkAchievements();
    }

    /**
     * Add or remove accessory and save to database
     * Map from name:
     * 0 - actual image id
     * 1 - icon image id
     * 2 - ImageView layer id
     * 3 - icon image with white background for easier dragging
     * @param name The string name of the accessory
     */
    public void addAccessory(String name) {

        // Get info from map
        ArrayList<Integer> info = accessoryMap.get(name);

        int img = 0;
        int icon = 0;
        int layer = 0;

        // For when the dragon is changed since it only has the img and layer
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
            // Remove accessory if already on dragon
            spriteDrawable.setDrawableByLayerId(layer, blankItem);

            // Set removed in database
            dbHelper.takeOffClothing(name);
        } else {
            // Add accessory if not on dragon
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

        // Set new imageview content for dragon
        spriteImage.setImageDrawable(spriteDrawable);
        spriteImage.invalidate();

        checkAchievements();
    }

    /**
     * Called from drop down
     * Shows selected group of accessories
     * @param chosenGroup The number corresponding to the chosen group
     */
    public void show(int chosenGroup) {
        // Empty linear layout
        layout.removeAllViews();

        ArrayList<String> tempGroup;

        if (chosenGroup == 0) {
            tempGroup = allAccessories;
        } else if (chosenGroup == 1) {
            tempGroup = glasses;
        } else if (chosenGroup == 2) {
            tempGroup = shirts;
        } else if (chosenGroup == 3) {
            tempGroup = hats;
        } else if (chosenGroup == 4) {
            tempGroup = specials;
        } else {
            tempGroup = allAccessories;
        }

        for (int i = 0; i < tempGroup.size(); i++) {
            ArrayList<Integer> info = accessoryMap.get(tempGroup.get(i));
            int img = info.get(0);
            int icon = info.get(1);
            int layer = info.get(2);
            int iconWhite = info.get(3);

            // Create new accessory imageview
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(255, 255));
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), iconWhite));
            imageView.setTag(tempGroup.get(i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Put accessory imageview in linear layout
            layout.addView(imageView);

            // Add listener to deal with dragging and clicking accessory
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            ImageView imgView = (ImageView) v;
                            String name = imgView.getTag().toString();
                            ArrayList<Integer> info = accessoryMap.get(name);
                            int icon = info.get(3);
                            int imageWidth = ((ImageView) v).getWidth();
                            int imageHeight = ((ImageView) v).getHeight();

                            int x = (int)event.getX();
                            int y = (int)event.getY();
                            x = (int) (((float)x/imageWidth)*100);
                            y = (int) (((float)y/imageHeight)*100);
                            int pixel = BitmapFactory.decodeResource(getResources(), icon)
                                    .getPixel(x,y);

                            if (Color.alpha(pixel) != 0) {
                                dragIcon = (String) v.getTag();
                                dragBool = true;
                            }

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
    }

    /**
     * Called from save photo button
     * Saves copy of dragon and accessories to camera roll
     * @param view The view that called the method
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

        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogAppearance);

        // Chain together various setter methods to set the dialog characteristics
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

    /**
     * Called from done button
     * Saves changes to dragon (Does nothing)
     * @param view The view that called the method
     */
    public void done(View view) {

        Intent intentHome = new Intent(this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
    }

    /**
     * Called from left arrow
     * Loops through the different dragon colorings
     * @param view The view that called the method
     */
    public void changeDragonLeft(View view) {
        if (currDragon == 0) {
            currDragon = dragons.size() - 1;
            addAccessory(dragons.get(currDragon));
        } else {
            currDragon -= 1;
            addAccessory(dragons.get(currDragon));
        }
    }

    /**
     * Called from right arrow
     * Loops through the different dragon colorings
     * @param view The view that called the method
     */
    public void changeDragonRight(View view) {
        if (currDragon == dragons.size() - 1) {
            currDragon = 0;
            addAccessory(dragons.get(currDragon));
        } else {
            currDragon += 1;
            addAccessory(dragons.get(currDragon));
        }
    }

    /**
     * Called from disrobe button
     * removes all clothing from dragon
     * @param view The view that called the method
     */
    public void disrobe(View view) {
        final ArrayList<ArrayList<String>> accessories = dbHelper.selectAccessories();

        if (accessories != null && accessories.size() > 0) {
            for (int i = 0; i < accessories.size(); i++) {
                // Do NOT remove dragon layer
                if (!accessories.get(i).get(1).equals("5")) {
                    // Remove all accessories that are on
                    if (accessories.get(i).get(2).equals("1")) {
                        addAccessory(accessories.get(i).get(0));
                    }
                }
            }
        }
    }

    /**
     * Called from combo box
     * Changes what accessory list is displayed
     * @param parent The adapter view for the combo box
     * @param view The view that triggered the listener
     * @param position The position in the combobox that was selected
     * @param id The id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        show(position);
    }

    /**
     * Called from combobox
     * Makes the auto-selection to be "All"
     * @param parent The adapter view for the combo box
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    /**
     * Make dictionary for accessories
     * Maps accessory name in database to drawable ids
     */
    public void makeDictionary() {
        // Glasses
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_glasses);
        ids.add(R.drawable.sprite_glasses_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_glasses_iconw);
        accessoryMap.put("sprite_glasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_monocle);
        ids.add(R.drawable.sprite_monocle_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_monocle_iconw);
        accessoryMap.put("sprite_monocle", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_nerdglasses);
        ids.add(R.drawable.sprite_nerdglasses_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_nerdglasses_iconw);
        accessoryMap.put("sprite_nerdglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_roundglasses);
        ids.add(R.drawable.sprite_roundglasses_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_roundglasses_iconw);
        accessoryMap.put("sprite_roundglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyglasses);
        ids.add(R.drawable.sprite_fancyglasses_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_fancyglasses_iconw);
        accessoryMap.put("sprite_fancyglasses", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_grannyglasses);
        ids.add(R.drawable.sprite_grannyglasses_icon);
        ids.add(R.id.accessory_glasses);
        ids.add(R.drawable.sprite_grannyglasses_iconw);
        accessoryMap.put("sprite_grannyglasses", ids);

        // Hats
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_brownhat);
        ids.add(R.drawable.sprite_brownhat_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_brownhat_iconw);
        accessoryMap.put("sprite_brownhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball);
        ids.add(R.drawable.sprite_hat_baseball_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_hat_baseball_iconw);
        accessoryMap.put("sprite_hat_baseball", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_camo);
        ids.add(R.drawable.sprite_hat_baseball_camo_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_hat_baseball_camo_iconw);
        accessoryMap.put("sprite_hat_baseball_camo", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_hat_baseball_red);
        ids.add(R.drawable.sprite_hat_baseball_red_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_hat_baseball_red_iconw);
        accessoryMap.put("sprite_hat_baseball_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tophat);
        ids.add(R.drawable.sprite_tophat_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_tophat_iconw);
        accessoryMap.put("sprite_tophat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_ribbonhat);
        ids.add(R.drawable.sprite_ribbonhat_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_ribbonhat_iconw);
        accessoryMap.put("sprite_ribbonhat", ids);

        // Shirts
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long);
        ids.add(R.drawable.sprite_shirt_long_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_shirt_long_iconw);
        accessoryMap.put("sprite_shirt_long", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_long_green);
        ids.add(R.drawable.sprite_shirt_long_green_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_shirt_long_green_iconw);
        accessoryMap.put("sprite_shirt_long_green", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short);
        ids.add(R.drawable.sprite_shirt_short_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_shirt_short_iconw);
        accessoryMap.put("sprite_shirt_short", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_shirt_short_red);
        ids.add(R.drawable.sprite_shirt_short_red_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_shirt_short_red_iconw);
        accessoryMap.put("sprite_shirt_short_red", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_fancyshirt);
        ids.add(R.drawable.sprite_fancyshirt_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_fancyshirt_iconw);
        accessoryMap.put("sprite_fancyshirt", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_tropicalshirt);
        ids.add(R.drawable.sprite_tropicalshirt_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_tropicalshirt_iconw);
        accessoryMap.put("sprite_tropicalshirt", ids);

        // Specials
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_cane);
        ids.add(R.drawable.sprite_cane_icon);
        ids.add(R.id.accessory_handItem);
        ids.add(R.drawable.sprite_cane_iconw);
        accessoryMap.put("sprite_cane", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_partyhat);
        ids.add(R.drawable.sprite_partyhat_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_partyhat_iconw);
        accessoryMap.put("sprite_partyhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_redribbonhat);
        ids.add(R.drawable.sprite_redribbonhat_icon);
        ids.add(R.id.accessory_hat);
        ids.add(R.drawable.sprite_redribbonhat_iconw);
        accessoryMap.put("sprite_redribbonhat", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_armor);
        ids.add(R.drawable.sprite_armor_icon);
        ids.add(R.id.accessory_shirt);
        ids.add(R.drawable.sprite_armor_iconw);
        accessoryMap.put("sprite_armor", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_sword);
        ids.add(R.drawable.sprite_sword_icon);
        ids.add(R.id.accessory_handItem);
        ids.add(R.drawable.sprite_sword_iconw);
        accessoryMap.put("sprite_sword", ids);
        ids = new ArrayList<Integer>();
        ids.add(R.drawable.sprite_treasure);
        ids.add(R.drawable.sprite_treasure_icon);
        ids.add(R.id.accessory_wingItem);
        ids.add(R.drawable.sprite_treasure_iconw);
        accessoryMap.put("sprite_treasure", ids);

        // Dragons
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
