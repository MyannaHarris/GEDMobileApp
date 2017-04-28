/*
 * LessonSteps.java
 *
 * Lesson Steps page activity
 *
 * Gives an step-by-step walk through of the lesson content
 *
 * Youtube video code from
 *      http://stackoverflow.com/questions/5817805/
 *      how-can-we-play-youtube-embeded-code-in-an-android-application-using-webview
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-26-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

public class LessonSteps extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int conceptID;
    private int lessonID;

    // Deal with video
    private WebView webView;

    // Lesson image variables
    private String lessonImg;
    private Bitmap lesson_img;

    // ImageView for achievement
    private ImageView lesson_imageView;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_steps);
        dbHelper = new DatabaseHelper(this);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);

        //System.out.println("test 1");
        // Allow homeAsUpIndicator (back arrow) to desplay on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //System.out.println("test 2");
        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //System.out.println("test 3");
        // Play youtube video from lesson
        String videoURL = dbHelper.selectVideoURL(lessonID);
        webView = (WebView) findViewById(R.id.example_web_view);
        //System.out.println("test 4");
        webView.getSettings().setJavaScriptEnabled(true);
       // System.out.println("test 5");
        webView.setWebChromeClient(new WebChromeClient());
        //System.out.println("test 6");
        String playVideo= "<html><body><iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"400\" src=\"http://www.youtube.com/embed/" + videoURL + "/?vq=small\" frameborder=\"0\"></body></html>";
       // System.out.println("test 7");
        webView.loadData(playVideo, "text/html", "utf-8");

       // System.out.println("test 8");
        // Set image to correct image
        lessonImg = dbHelper.selectPictureName(lessonID);
        lesson_img = getBitmapFromAsset(lessonImg);

        lesson_imageView = (ImageView) findViewById(R.id.example_image_view);
        if (lesson_img != null) {
            lesson_imageView.setImageBitmap(lesson_img);
        }

        // Set text to correct text
        String lessonAdvice = dbHelper.selectLessonAdvice(lessonID);
        TextView advice = (TextView) findViewById(R.id.advice_text);
        advice.setText(toHTML(lessonAdvice));

        //System.out.println("test 9");
        //get screen dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //System.out.println("test 10");
        Button nextbtn = (Button) findViewById(R.id.lessonExample);
        TextView title = (TextView) findViewById(R.id.stepsTitle);
        //set dynamic text size
        advice.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        nextbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/20));

        //  Make current lesson this one...because we're on it now
        if (dbHelper.selectCurrentLessonID() != lessonID) {
            dbHelper.updateCurrentLessonID(lessonID);
        }

    }


    /**
     * method for retrieving bitmap of lesson pictures
     * @param strName the path where the picture are located
     * @return bitmap for accessing lesson pictures
     */
    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("lesson_pics/" + strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    /**
     * Gets access to the assets folder
     * @return Access to the assets
     */
    @Override
    public AssetManager getAssets()
    {
        return getResources().getAssets();
    }

    /**
     * hides bottom navigation bar
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        webView.onResume();
    }

    /**
     * Pauses this activity when it is left by a new activity being put on top of it in the stack
     */
    @Override
    protected void onPause() {
        super.onPause();

        webView.onPause();
    }

    /**
     * When the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        lesson_imageView.setImageBitmap(null);

        if(lesson_img!=null) {
            lesson_img.recycle();
            lesson_img=null;
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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    /**
     * Sets what menu will be in the action bar
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

    /**
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Called when the move on button is clicked
     * Opens the Lesson example page
     * @param view the button that was pressed
     */
    public void gotToLessonExample(View view) {
        Intent intent = new Intent(this, LessonExample.class);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        startActivity(intent);
    }

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }

    }
}
