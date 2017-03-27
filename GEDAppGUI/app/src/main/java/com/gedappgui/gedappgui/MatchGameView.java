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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MatchGameView extends LinearLayout{

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Array to hold options for match cards
    private ArrayList<ArrayList<String>> texts;
    private ArrayList<String> textsInf;
    private ArrayList<String> currTextsInf;

    // Map to hold correct matches
    private Map<String, ArrayList<String>> answers;

    // Gridview to hold cards
    private GridView gridview;

    // Variables to hold current matches
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

    // Randomly ordered cards
    private ArrayList<String> randomizedCards;

    // Button to end the endless game play
    private Button endButton;

    // Size of button to end
    private int endButtonSize = 0;

    /**
     * Constructor for game
     * @param contextp Context of the activity
     * @param activity Reference to the current activity
     * @param textsp List of data for the game
     * @param conceptIDp ID of the current concept
     * @param lessonIDp ID of the current lesson
     * @param nextActivityp Number indicating what the activity after the game should be
     * @param widthp Width of the screen in pixels
     * @param heightp Height of screen in pixels
     */
    public MatchGameView(Context contextp, Activity activity,
                         ArrayList<ArrayList<String>> textsp,
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

        // Screen size
        width = widthp;
        height = heightp;

        // Get status bar height to deal with on phones that have the status bar showing
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;

        // Set orientation to make a vertical list
        this.setOrientation(LinearLayout.VERTICAL);

        // Save the data for game globally
        if (nextActivity == 1) {
            textsInf = textsp.get(0);

            LinearLayout.LayoutParams linearLayoutButton = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, (height - statusBarHeight - 15) / 8 - 20);

            linearLayoutButton.setMargins(0, 5, 0, 5);

            endButton = new Button(context);
            endButton.setLayoutParams(linearLayoutButton);
            endButton.setTextSize(convertPixelsToDp(height / 20, context));
            endButton.setTextColor(ContextCompat.getColor(context, R.color.matchGameText));
            endButton.setText("End Game");
            endButton.setGravity(Gravity.CENTER_HORIZONTAL);
            endButton.setHeight((height - statusBarHeight - 15) / 8 - 20);

            endButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    endGame();
                }
            });

            endButtonSize = (height - statusBarHeight - 15) / 8 - 20 + 10;
        } else {
            texts = textsp;
        }

        answers = new HashMap<String, ArrayList<String>>();
        if (nextActivity == 1) {
            for (int i = 0; i < textsInf.size() / 2; i++) {
                if (answers.containsKey(textsInf.get(i))) {
                    ArrayList<String> oldTemp = new ArrayList<String>();
                    oldTemp = answers.get(textsInf.get(i));
                    oldTemp.add(textsInf.get(i + 20));
                    answers.put(textsInf.get(i), oldTemp);
                } else {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(textsInf.get(i + 20));
                    answers.put(textsInf.get(i), temp);
                }
            }
            for (int i = textsInf.size() / 2; i < textsInf.size(); i++) {
                if (answers.containsKey(textsInf.get(i))) {
                    ArrayList<String> oldTemp = new ArrayList<String>();
                    oldTemp = answers.get(textsInf.get(i));
                    oldTemp.add(textsInf.get(i - 20));
                    answers.put(textsInf.get(i), oldTemp);
                } else {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(textsInf.get(i - 20));
                    answers.put(textsInf.get(i), temp);
                }
            }
        } else {
            for (int i = 0; i < texts.size(); i++) {
                ArrayList<String> temp = texts.get(i);
                for (int k = 0; k < temp.size() / 2; k++) {
                    if (answers.containsKey(temp.get(k))) {
                        ArrayList<String> oldTemp = new ArrayList<String>();
                        oldTemp = answers.get(temp.get(k));
                        oldTemp.add(temp.get(k + 3));
                        answers.put(temp.get(k), oldTemp);
                    } else {
                        ArrayList<String> newTemp = new ArrayList<String>();
                        newTemp.add(temp.get(k + 3));
                        answers.put(temp.get(k), newTemp);
                    }
                }
                for (int k = temp.size() / 2; k < temp.size(); k++) {
                    if (answers.containsKey(temp.get(k))) {
                        ArrayList<String> oldTemp = new ArrayList<String>();
                        oldTemp = answers.get(temp.get(k));
                        oldTemp.add(temp.get(k - 3));
                        answers.put(temp.get(k), oldTemp);
                    } else {
                        ArrayList<String> newTemp = new ArrayList<String>();
                        newTemp.add(temp.get(k - 3));
                        answers.put(temp.get(k), newTemp);
                    }
                }
            }
        }

        // Set up randomized cards
        randomizedCards = new ArrayList<String>();
        currTextsInf = new ArrayList<String>();

        if (nextActivity == 1) {
            currTextsInf.clear();
            ArrayList<String> randQ = new ArrayList<String>();
            ArrayList<String> randA = new ArrayList<String>();
            ArrayList<String> allQAndAs = new ArrayList<String>();

            for (String text : textsInf) {
                allQAndAs.add(text);
            }

            //choose 3 random questions of the 20 to give to the user in the game
            for(int r = 0; r < 3; r++) {
                //randomly generate 1s and zeroes
                double rand = Math.abs(Math.round(Math.random() * 19-r));
                randQ.add(allQAndAs.remove((int) rand));
                randA.add(allQAndAs.remove((int)(rand + (19-r))));
            }

            randQ.addAll(randA);

            for (String text : randQ) {
                currTextsInf.add(text);
                randomizedCards.add(text);
            }

        } else {
            for (String text : texts.get(0)) {
                randomizedCards.add(text);
            }
        }

        Collections.shuffle(randomizedCards);

        // Fill gridview with texts
        // Take first group of texts (3 pairs)
        gridview = new GridView(context);
        textViewAdapter = new TextViewAdapter(context,
                randomizedCards.toArray(new String[randomizedCards.size()]),
                width, height, statusBarHeight + endButtonSize);
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
                                        String textChoice1 = choice1TextView.getTag().toString();

                                        if (answers.get(textChoice1).contains(
                                                choice2TextView.getTag().toString())) {
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
                                                    if (numMatches >= 3) {

                                                        numRoundsDone++;

                                                        // Check if game is done
                                                        if (nextActivity != 1 &&
                                                                numRoundsDone >= texts.size()) {

                                                            // Go to GameEnd page if done with game
                                                            endGame();
                                                        } else {

                                                            // Set up randomized cards
                                                            randomizedCards.clear();
                                                            if (nextActivity == 1) {
                                                                currTextsInf.clear();
                                                                ArrayList<String> randQ = new ArrayList<String>();
                                                                ArrayList<String> randA = new ArrayList<String>();
                                                                ArrayList<String> allQAndAs = new ArrayList<String>();

                                                                for (String text : textsInf) {
                                                                    allQAndAs.add(text);
                                                                }

                                                                //choose 3 random questions of the 20 to give to the user in the game
                                                                for(int r = 0; r < 3; r++) {
                                                                    //randomly generate 1s and zeroes
                                                                    double rand = Math.abs(Math.round(Math.random() * 19-r));
                                                                    randQ.add(allQAndAs.remove((int) rand));
                                                                    randA.add(allQAndAs.remove((int)(rand + (19-r))));
                                                                }

                                                                randQ.addAll(randA);

                                                                for (String text : randQ) {
                                                                    currTextsInf.add(text);
                                                                    randomizedCards.add(text);
                                                                }

                                                            } else {
                                                                for (String text : texts.get(0)) {
                                                                    randomizedCards.add(text);
                                                                }
                                                            }
                                                            Collections.shuffle(randomizedCards);

                                                            // Clear all the cards
                                                            textViewAdapter.clear();
                                                            // Add new cards for next round
                                                            textViewAdapter.setTexts(
                                                                    randomizedCards.toArray(
                                                                            new String[randomizedCards.size()])
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
                                            String textChoice1 = choice1TextView.getTag().toString();

                                            if (answers.get(textChoice1).contains(
                                                    choice2TextView.getTag().toString())) {
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
                                                        if (numMatches >= 3) {

                                                            numRoundsDone++;

                                                            // Check if game is done
                                                            if (nextActivity != 1 &&
                                                                    numRoundsDone >= texts.size()) {

                                                                // Go to GameEnd page if done with game
                                                                endGame();
                                                            } else {

                                                                // Set up randomized cards
                                                                randomizedCards.clear();
                                                                if (nextActivity == 1) {
                                                                    currTextsInf.clear();
                                                                    ArrayList<String> randQ = new ArrayList<String>();
                                                                    ArrayList<String> randA = new ArrayList<String>();
                                                                    ArrayList<String> allQAndAs = new ArrayList<String>();

                                                                    for (String text : textsInf) {
                                                                        allQAndAs.add(text);
                                                                    }

                                                                    //choose 3 random questions of the 20 to give to the user in the game
                                                                    for(int r = 0; r < 3; r++) {
                                                                        //randomly generate 1s and zeroes
                                                                        double rand = Math.abs(Math.round(Math.random() * 19-r));
                                                                        randQ.add(allQAndAs.remove((int) rand));
                                                                        randA.add(allQAndAs.remove((int)(rand + (19-r))));
                                                                    }

                                                                    randQ.addAll(randA);

                                                                    for (String text : randQ) {
                                                                        currTextsInf.add(text);
                                                                        randomizedCards.add(text);
                                                                    }

                                                                } else {
                                                                    for (String text : texts.get(0)) {
                                                                        randomizedCards.add(text);
                                                                    }
                                                                }
                                                                Collections.shuffle(randomizedCards);

                                                                // Clear all the cards
                                                                textViewAdapter.clear();
                                                                // Add new cards for next round
                                                                textViewAdapter.setTexts(
                                                                        randomizedCards.toArray(
                                                                                new String[randomizedCards.size()])
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
        if (nextActivity == 1) {
            this.addView(endButton);
        }
        this.addView(gridview);
    }

    /**
     * Move on to game end page
     */
    private void endGame() {
        Intent intent = new Intent(context, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID", conceptID);
        intent.putExtra("lessonID", lessonID);
        context.startActivity(intent);
    }

    /**
     * Change pixel measurement into dp measurement
     * @param px The pixels
     * @param context The context of the activity
     * @return dp - The measurement in dp
     */
    public static float convertPixelsToDp(float px,Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }
}
