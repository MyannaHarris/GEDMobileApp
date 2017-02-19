/*
 * ButonAdapter.java
 *
 * Buton Adapter
 *
 * Adapter to fill gridview with buttons
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-17-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

public class ButtonAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;
    // Hold the ids of the images from the drawable folder
    private String[] buttonNames;
    private ImageView changer;
    private String[] answers;
    private TextView statement;
    private int cur = 0;
    private int pictureindex = 0;
    private int[] pictures;
    private TextView resulter;
    private int lesson;
    private int concept;
    private int nextActivity;

    /*
     * Constructor for PictureGameView buttons
     * Gets the variables from PictureGameView to use in create the buttons and events
     */
    public ButtonAdapter(Context c, String[] buttonNamesp, ImageView change, String splitter,
                         TextView state, int[] pics, TextView result, int lessonID,
                         int conceptID, int nextAct) {

        mContext = c;
        buttonNames = buttonNamesp;
        changer = change;
        answers = splitter.split(",");
        statement = state;
        pictures = pics;
        resulter = result;
        lesson = lessonID;
        nextActivity = nextAct;
        concept = conceptID;

    }

    //Deafult constructor for just putting buttons in gridview
    public ButtonAdapter(Context c, String[] buttonNamesp){
        mContext = c;
        buttonNames = buttonNamesp;
    }


    /*
     * Gets the number of itams to put in the view
     *
     * returns imageIds.length
     */
    @Override
    public int getCount() {
        return buttonNames.length;
    }

    /*
     * Does not do anything but needed to implement BaseAdapter
     * returns null
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /*
     * Does not do anything but needed to implement BaseAdapter
     * returns the position sent to the method
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Creates the components for the adapter
     * returns an imageview
     */

    //credit: radhoo, StackOverflow
    //Helper function for animating the picture change in PictureGameView.java
    private static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    //Used for making truth and false buttons in PictureGameView.java
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Button button;
        if (convertView == null) {
             //if it's not recycled, initialize some attributes
            button = new Button(mContext);
            button.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }

        button.setText(buttonNames[position]);
        if (button.getText().equals("TRUE")){
            //set true color to green
            button.getBackground().setColorFilter(0xFF23c438, PorterDuff.Mode.MULTIPLY);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (answers[cur + 1].equals("t")){
                        //circular queue for statements
                        cur = (cur + 2) % 20;
                        statement.setText(answers[cur]);
                        resulter.setText("Correct!");
                        Runnable r = new Runnable(){
                            @Override
                            public void run(){
                                ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                            }
                        };
                        Handler h = new Handler();
                        //Delay picture change by .75 secs
                        h.postDelayed(r,750);
                        pictureindex++;
                        //once 5 statements are answered correctly
                        if (pictureindex > 4){
                            button.setEnabled(false);
                            statement.setText("Congratulations!");
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    Context context = mContext;
                                    Intent intent = new Intent(context, GameEnd.class);
                                    intent.putExtra("next_activity", nextActivity);
                                    intent.putExtra("conceptID", concept);
                                    intent.putExtra("lessonID", lesson);
                                    context.startActivity(intent);
                                }
                            };
                            Handler h2 = new Handler();
                            //Delay transition to game end by 2.5 secs
                            h2.postDelayed(r2,2500);
                        }

                    }
                    else{
                        resulter.setText("Incorrect! Try again");
                        cur = (cur + 2) % 20;
                        statement.setText(answers[cur]);
                    }
                }
            });
        }
        else if (button.getText().equals("FALSE")){
            //set false color to red
            button.getBackground().setColorFilter(0xFFce4257, PorterDuff.Mode.MULTIPLY);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (answers[cur + 1].equals("f")){
                        cur = (cur + 2) % 20;
                        statement.setText(answers[cur]);
                        resulter.setText("Correct!");
                        Runnable r = new Runnable(){
                            @Override
                            public void run(){
                                ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                            }
                        };
                        Handler h = new Handler();
                        //Delay picture change by .75 secs
                        h.postDelayed(r,750);
                        pictureindex++;
                        //once 5 statements are answered correctly
                        if (pictureindex > 4){
                            statement.setText("Congratulations!");
                            button.setEnabled(false);
                            Runnable r2 = new Runnable() {
                                @Override
                                public void run() {
                                    Context context = mContext;
                                    Intent intent = new Intent(context, GameEnd.class);
                                    intent.putExtra("next_activity", nextActivity);
                                    intent.putExtra("conceptID", concept);
                                    intent.putExtra("lessonID", lesson);
                                    context.startActivity(intent);
                                }
                            };
                            Handler h2 = new Handler();
                            //Delay transition to game end by 2.5 secs
                            h2.postDelayed(r2,2500);
                        }

                    }
                    else{
                        resulter.setText("Incorrect! Try again");
                        cur = (cur + 2) % 20;
                        statement.setText(answers[cur]);

                    }
                }
            });
        }
        button.setTextColor(Color.WHITE);

        return button;
    }


}
