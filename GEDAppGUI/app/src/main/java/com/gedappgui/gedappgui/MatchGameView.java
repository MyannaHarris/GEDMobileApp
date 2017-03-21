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
 * Last Edit: 3-19-17
 *
 */

package com.gedappgui.gedappgui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
    private ArrayList<ArrayList<String>> texts;

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

    // Count number of rounds done
    private int numRoundsDone = 0;

    // screen size variables
    private int width;
    private int height;
    private int statusBarHeight;

    // Adapter for gridview
    private TextViewAdapter textViewAdapter;

    /**
     * Constructor for game
     * @param contextp Context of the activity
     * @param activity Reference to the current activity
     * @param textsp List of data for the game
     * @param answersp List fo answers for the game
     * @param conceptIDp ID of the current concept
     * @param lessonIDp ID of the current lesson
     * @param nextActivityp Number indicating what the activity after the game should be
     * @param widthp Width of the screen in pixels
     * @param heightp Height of screen in pixels
     */
    public MatchGameView(Context contextp, Activity activity,
                         ArrayList<ArrayList<String>> textsp, final ArrayList<Integer> answersp,
                         int conceptIDp, int lessonIDp, int nextActivityp,
                         int widthp, int heightp) {
        super(contextp);

        // Set context
        context = contextp;

        choice1TextView = new TextView(context);
        choice2TextView = new TextView(context);
        start = new TextView(context);

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        // Save the data for game globally
        texts = textsp;
        answers = answersp;

        // Screen size
        width = widthp;
        height = heightp;

        // Get status bar height to deal with on phones that have the status bar showing
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;

        // Fill gridview with texts
        // Take first group of texts (3 pairs)
        gridview = new GridView(context);
        textViewAdapter = new TextViewAdapter(context,
                texts.get(0).toArray(new String[texts.get(0).size()]),
                width, height, statusBarHeight);
        gridview.setAdapter(textViewAdapter);

        // Set other gridview formatting
        gridview.setNumColumns(2);
        gridview.setColumnWidth((int)width / 2);
        gridview.setHorizontalSpacing(10);
        gridview.setVerticalSpacing(10);
        gridview.setBackgroundColor(ContextCompat.getColor(context, R.color.bucketGameBG));

        // Add margins around gridview
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, height - statusBarHeight);
        linearLayout.setMargins(10, 5, 10, 10);
        gridview.setLayoutParams(linearLayout);

        // Set background color of page
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.bucketGameBG));

        // Set listener on layout
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int position = 0;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save what position in the gridlayout was clicked
                        position = gridview.pointToPosition((int)event.getX(),
                                (int)event.getY());
                        TextView vw = (TextView) gridview.getChildAt(position);

                        // Save start in case of drag
                        if (vw != null && vw.getText() != null && !vw.getText().equals("")) {
                            start = vw;
                        }

                        // Check if this is the first or second card
                        if (vw != null  && vw.getText() != null && !vw.getText().equals("")
                                && newMatch && secondChoiceDone) {

                            // If this is the first card selected, save it

                            choice1TextView = (TextView)vw;
                            if (choice1TextView.getText() != null &&
                                    !choice1TextView.getText().equals("")) {
                                choice1 = position;
                                newMatch = false;

                                // Change background of card to reflect it is selected
                                // Different for different SDK versions
                                if (Build.VERSION.SDK_INT < 16) {
                                    // Sets Drawable as background on older API
                                    choice1TextView.setBackgroundDrawable(ContextCompat.getDrawable(context,
                                            R.drawable.match_game_selected));
                                } else {
                                    choice1TextView.setBackground(ContextCompat.getDrawable(context,
                                            R.drawable.match_game_selected));
                                }
                            }

                            // Pause to make sure glitches aren't caused by concurrent call to listener
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                        }  else if (vw != null  && vw.getText() != null && !vw.getText().equals("")
                                && secondChoiceDone &&
                                choice1TextView.getText() != null &&
                                !choice1TextView.getText().equals("")) {

                            // If this is the second card selected, check answer

                            choice2TextView = (TextView) vw;
                            if (choice2TextView.getText() != null &&
                                    !choice2TextView.getText().equals("")) {

                                secondChoiceDone = false;
                                choice2 = position;

                                // Change background of card to reflect it is selected
                                // Different for different SDK versions
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

                                            // Change background of card to reflect it is correct
                                            // Different for different SDK versions
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

                                                    // Change background of card to reflect it is
                                                    //      done being used
                                                    // Different for different SDK versions
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

                                                    // Check if round is done
                                                    if (numMatches >= texts.get(0).size() / 2) {

                                                        numRoundsDone++;

                                                        // Check if game is done
                                                        if (numRoundsDone >= texts.size()) {

                                                            if (nextActivity == 1) {
                                                                // Infinite play in Play area

                                                                // Go to GameEnd page if done with game

                                                                Context context = getContext();
                                                                Intent intent = new Intent(context, GameEnd.class);
                                                                intent.putExtra("next_activity", nextActivity);
                                                                intent.putExtra("conceptID", conceptID);
                                                                intent.putExtra("lessonID", lessonID);
                                                                context.startActivity(intent);

                                                            } else {

                                                                // Go to GameEnd page if done with game

                                                                Context context = getContext();
                                                                Intent intent = new Intent(context, GameEnd.class);
                                                                intent.putExtra("next_activity", nextActivity);
                                                                intent.putExtra("conceptID", conceptID);
                                                                intent.putExtra("lessonID", lessonID);
                                                                context.startActivity(intent);
                                                            }
                                                        } else {

                                                            // Clear all the cards
                                                            textViewAdapter.clear();
                                                            // Add new cards for next round
                                                            textViewAdapter.setTexts(
                                                                    texts.get(numRoundsDone).toArray(
                                                                            new String[texts.get(numRoundsDone).size()])
                                                            );
                                                            textViewAdapter.notifyDataSetChanged();

                                                            // Reset textviews and variables for next
                                                            //      pair
                                                            numMatches = 0;
                                                            newMatch = true;
                                                            secondChoiceDone = true;

                                                            choice1TextView = new TextView(context);
                                                            choice2TextView = new TextView(context);
                                                            start = new TextView(context);
                                                        }
                                                    }

                                                    // Reset textviews and variables for next
                                                    //      pair
                                                    choice1TextView = new TextView(context);
                                                    choice2TextView = new TextView(context);
                                                    start = new TextView(context);

                                                    newMatch = true;
                                                    secondChoiceDone = true;
                                                }
                                            }, 300);
                                        } else {
                                            // Return BGs to normal if answer is wrong

                                            // Change background of card to reflect it is incorrect
                                            // Different for different SDK versions
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

                                                    // Change background of card to reflect it is
                                                    //      not selected
                                                    // Different for different SDK versions
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

                                                    // Reset textviews and variables for next
                                                    //      pair
                                                    choice1TextView = new TextView(context);
                                                    choice2TextView = new TextView(context);
                                                    start = new TextView(context);

                                                    newMatch = true;
                                                    secondChoiceDone = true;
                                                }
                                            }, 300);
                                        }
                                    }
                                }, 300);
                            }

                            // Pause to make sure glitches aren't caused by concurrent call to listener
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        position = gridview.pointToPosition((int)event.getX(),
                                (int)event.getY());
                        TextView v = (TextView) gridview.getChildAt(position);

                        // Lots of checks to make sure null pointer exceptions can't happen
                        if (start != null && start != v  && choice1TextView != null &&
                                choice1TextView.getText() != null &&
                                !choice1TextView.getText().equals("") &&
                                start.getText() != null && !start.getText().equals("") &&
                                v != null &&
                                v.getText() != null && !v.getText().equals("")) {

                            if (v != null && secondChoiceDone) {
                                // If this is the second card selected, check answer

                                choice2TextView = (TextView) v;
                                if (choice2TextView.getText() != null &&
                                        !choice2TextView.getText().equals("")) {

                                    secondChoiceDone = false;
                                    choice2 = position;

                                    // Change background of card to reflect it is selected
                                    // Different for different SDK versions
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

                                                // Change background of card to reflect it is correct
                                                // Different for different SDK versions
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

                                                        // Change background of card to reflect it is
                                                        //      done being used
                                                        // Different for different SDK versions
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

                                                        // Check if round is done
                                                        if (numMatches >= texts.get(0).size() / 2) {

                                                            numRoundsDone++;

                                                            // Check if game is done
                                                            if (numRoundsDone >= texts.size()) {

                                                                if (nextActivity == 1) {
                                                                    // Infinite play in Play area

                                                                    // Go to GameEnd page if done with game

                                                                    Context context = getContext();
                                                                    Intent intent = new Intent(context, GameEnd.class);
                                                                    intent.putExtra("next_activity", nextActivity);
                                                                    intent.putExtra("conceptID", conceptID);
                                                                    intent.putExtra("lessonID", lessonID);
                                                                    context.startActivity(intent);

                                                                } else {

                                                                    // Go to GameEnd page if done with game

                                                                    Context context = getContext();
                                                                    Intent intent = new Intent(context, GameEnd.class);
                                                                    intent.putExtra("next_activity", nextActivity);
                                                                    intent.putExtra("conceptID", conceptID);
                                                                    intent.putExtra("lessonID", lessonID);
                                                                    context.startActivity(intent);
                                                                }
                                                            } else {

                                                                // Clear all the cards
                                                                textViewAdapter.clear();
                                                                // Add new cards for next round
                                                                textViewAdapter.setTexts(
                                                                        texts.get(numRoundsDone).toArray(
                                                                                new String[texts.get(numRoundsDone).size()])
                                                                );
                                                                textViewAdapter.notifyDataSetChanged();

                                                                // Reset textviews and variables for next
                                                                //      pair
                                                                numMatches = 0;
                                                                newMatch = true;
                                                                secondChoiceDone = true;

                                                                choice1TextView = new TextView(context);
                                                                choice2TextView = new TextView(context);
                                                                start = new TextView(context);
                                                            }
                                                        }

                                                        // Reset textviews and variables for next
                                                        //      pair
                                                        choice1TextView = new TextView(context);
                                                        choice2TextView = new TextView(context);
                                                        start = new TextView(context);

                                                        newMatch = true;
                                                        secondChoiceDone = true;
                                                    }
                                                }, 300);
                                            } else {
                                                // Return BGs to normal if answer is wrong

                                                // Change background of card to reflect it is
                                                //      incorrect
                                                // Different for different SDK versions
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

                                                        // Change background of card to reflect it is
                                                        //      not selected
                                                        // Different for different SDK versions
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

                                                        // Reset textviews and variables for next
                                                        //      pair
                                                        choice1TextView = new TextView(context);
                                                        choice2TextView = new TextView(context);
                                                        start = new TextView(context);

                                                        newMatch = true;
                                                        secondChoiceDone = true;
                                                    }
                                                }, 300);
                                            }
                                        }
                                    }, 300);
                                }

                                // Pause to make sure glitches aren't caused by concurrent call to listener
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });

        // Add gridview to layout
        this.addView(gridview);
    }


}
