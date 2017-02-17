/*
 * MatchGameView.java
 *
 * Match game
 *
 * View runs the match game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-12-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchGameView extends LinearLayout{

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Array to hold options for match cards
    private ArrayList<String> texts;

    // Array to hold correct matches
    private ArrayList<Integer> answers;

    // Gridview to hold cards
    private GridView gridview;

    // Variables to hold current matches
    private int choice1 = 0;
    private int choice2 = 0;
    private TextView choice1TextView;
    private TextView choice2TextView;
    private TextView start;

    // Variable to check if new choices are starting
    private boolean newMatch = true;
    private boolean secondChoiceDone = true;

    // Variable to track the number of matches
    private int numMatches = 0;

    // Context of game
    private Context context;

    // Width to change flipping card
    private int changeWidth;

    /*
     * Constructor
     */
    public MatchGameView(Context contextp, ArrayList<String> textsp, final ArrayList<Integer> answersp,
                         int conceptIDp, int lessonIDp, int nextActivityp,
                         int width, int height) {
        super(contextp);

        // Set context
        context = contextp;

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        texts = textsp;
        answers = answersp;

        // Fill gridview with texts
        gridview = new GridView(context);
        gridview.setAdapter(new TextViewAdapter(context, texts.toArray(new String[texts.size()]),
                width, height));

        // Set other gridview formatting
        gridview.setNumColumns(2);
        gridview.setColumnWidth((int)width / 2);
        gridview.setHorizontalSpacing(10);
        gridview.setVerticalSpacing(10);
        gridview.setBackgroundColor(ContextCompat.getColor(context, R.color.bucketGameBG));

        // Add margins around gridview
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setMargins(10, 10, 10, 10);
        gridview.setLayoutParams(linearLayout);

        // Set background color of page
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.bucketGameBG));

        // Set listener
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int position = 0;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        position = gridview.pointToPosition((int)event.getX(),
                                (int)event.getY());
                        TextView vw = (TextView) gridview.getChildAt(position);

                        if (vw != null) {
                            start = vw;
                        }

                        if (vw != null && newMatch && secondChoiceDone) {
                            // If this is the first card selected, save it

                            choice1TextView = (TextView)vw;
                            if (choice1TextView.getText() != null &&
                                    !choice1TextView.getText().equals("")) {
                                choice1 = position;
                                newMatch = false;
                                if (Build.VERSION.SDK_INT < 16) {
                                    // Sets Drawable as background on older API
                                    choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(context,
                                            R.drawable.match_game_selected));
                                } else {
                                    choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                            R.drawable.match_game_selected));
                                }
                            }

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }  else if (vw != null && secondChoiceDone) {
                            // If this is the second card selected, check answer

                            choice2TextView = (TextView) vw;
                            if (choice2TextView.getText() != null &&
                                    !choice2TextView.getText().equals("")) {

                                secondChoiceDone = false;
                                choice2 = position;

                                if (Build.VERSION.SDK_INT < 16) {
                                    // Sets Drawable as background on older API
                                    choice2TextView.setBackgroundDrawable(
                                            ContextCompat.getDrawable(context,
                                                    R.drawable.match_game_selected));
                                } else {
                                    choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                            R.drawable.match_game_selected));
                                }

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (answers.get(choice1) == choice2) {
                                            // Check if answer is right

                                            if (Build.VERSION.SDK_INT < 16) {
                                                // Sets Drawable as background on older API

                                                choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                        context, R.drawable.match_game_green));
                                                choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                        context, R.drawable.match_game_green));
                                            } else {
                                                choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                        R.drawable.match_game_green));
                                                choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                        R.drawable.match_game_green));
                                            }

                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {
                                                    numMatches += 1;
                                                    choice2TextView.setText("");

                                                    choice1TextView.setText("");

                                                    // Change BGs
                                                    if (Build.VERSION.SDK_INT < 16) {
                                                        // Sets Drawable as background on older API

                                                        choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                context, R.drawable.match_game_correct));
                                                        choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                context, R.drawable.match_game_correct));
                                                    } else {
                                                        choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                                R.drawable.match_game_correct));
                                                        choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                                R.drawable.match_game_correct));
                                                    }

                                                    // Check if game is done
                                                    if (numMatches >= texts.size() / 2) {
                                                        // Go to GameEnd page if done with game

                                                        Context context = getContext();
                                                        Intent intent = new Intent(context, GameEnd.class);
                                                        intent.putExtra("next_activity", nextActivity);
                                                        intent.putExtra("conceptID", conceptID);
                                                        intent.putExtra("lessonID", lessonID);
                                                        context.startActivity(intent);
                                                    }

                                                    newMatch = true;
                                                    secondChoiceDone = true;
                                                }
                                            }, 500);
                                        } else {
                                            // Return BGs to normal if answer is wrong

                                            if (Build.VERSION.SDK_INT < 16) {
                                                // Sets Drawable as background on older API

                                                choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                        context, R.drawable.match_game_red));
                                                choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                        context, R.drawable.match_game_red));
                                            } else {
                                                choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                        R.drawable.match_game_red));
                                                choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                        R.drawable.match_game_red));
                                            }

                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {

                                                    if (Build.VERSION.SDK_INT < 16) {
                                                        // Sets Drawable as background on older API
                                                        choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                context, R.drawable.match_game_unselected));
                                                        choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                context, R.drawable.match_game_unselected));
                                                    } else {
                                                        choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                                R.drawable.match_game_unselected));
                                                        choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                                R.drawable.match_game_unselected));
                                                    }

                                                    newMatch = true;
                                                    secondChoiceDone = true;
                                                }
                                            }, 500);
                                        }
                                    }
                                }, 500);
                            }

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        position = gridview.pointToPosition((int)event.getX(),
                                (int)event.getY());
                        TextView v = (TextView) gridview.getChildAt(position);

                        if (start != v) {

                            if (v != null && secondChoiceDone) {
                                // If this is the second card selected, check answer

                                choice2TextView = (TextView) v;
                                if (choice2TextView.getText() != null &&
                                        !choice2TextView.getText().equals("")) {

                                    secondChoiceDone = false;
                                    choice2 = position;

                                    if (Build.VERSION.SDK_INT < 16) {
                                        // Sets Drawable as background on older API
                                        choice2TextView.setBackgroundDrawable(
                                                ContextCompat.getDrawable(context,
                                                        R.drawable.match_game_selected));
                                    } else {
                                        choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                R.drawable.match_game_selected));
                                    }

                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            if (answers.get(choice1) == choice2) {
                                                // Check if answer is right

                                                if (Build.VERSION.SDK_INT < 16) {
                                                    // Sets Drawable as background on older API

                                                    choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                            context, R.drawable.match_game_green));
                                                    choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                            context, R.drawable.match_game_green));
                                                } else {
                                                    choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                            R.drawable.match_game_green));
                                                    choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                            R.drawable.match_game_green));
                                                }

                                                new Handler().postDelayed(new Runnable() {
                                                    public void run() {
                                                        numMatches += 1;
                                                        choice2TextView.setText("");

                                                        choice1TextView.setText("");

                                                        // Change BGs
                                                        if (Build.VERSION.SDK_INT < 16) {
                                                            // Sets Drawable as background on older API

                                                            choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                    context, R.drawable.match_game_correct));
                                                            choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                    context, R.drawable.match_game_correct));
                                                        } else {
                                                            choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                                    R.drawable.match_game_correct));
                                                            choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                                    R.drawable.match_game_correct));
                                                        }

                                                        // Check if game is done
                                                        if (numMatches >= texts.size() / 2) {
                                                            // Go to GameEnd page if done with game

                                                            Context context = getContext();
                                                            Intent intent = new Intent(context, GameEnd.class);
                                                            intent.putExtra("next_activity", nextActivity);
                                                            intent.putExtra("conceptID", conceptID);
                                                            intent.putExtra("lessonID", lessonID);
                                                            context.startActivity(intent);
                                                        }

                                                        newMatch = true;
                                                        secondChoiceDone = true;
                                                    }
                                                }, 500);
                                            } else {
                                                // Return BGs to normal if answer is wrong

                                                if (Build.VERSION.SDK_INT < 16) {
                                                    // Sets Drawable as background on older API

                                                    choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                            context, R.drawable.match_game_red));
                                                    choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                            context, R.drawable.match_game_red));
                                                } else {
                                                    choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                            R.drawable.match_game_red));
                                                    choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                            R.drawable.match_game_red));
                                                }

                                                new Handler().postDelayed(new Runnable() {
                                                    public void run() {

                                                        if (Build.VERSION.SDK_INT < 16) {
                                                            // Sets Drawable as background on older API
                                                            choice2TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                    context, R.drawable.match_game_unselected));
                                                            choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(
                                                                    context, R.drawable.match_game_unselected));
                                                        } else {
                                                            choice2TextView.setBackground(ContextCompat.getDrawable(context,
                                                                    R.drawable.match_game_unselected));
                                                            choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                                                    R.drawable.match_game_unselected));
                                                        }

                                                        newMatch = true;
                                                        secondChoiceDone = true;
                                                    }
                                                }, 500);
                                            }
                                        }
                                    }, 500);
                                }

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });

        this.addView(gridview);
    }
}
