/*
 * ChemistryGameView.java
 *
 * Chemistry game
 *
 * View runs the Chemistry game
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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChemistryGameView extends RelativeLayout {

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Current activity context
    private Context context;

    // Game views
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView answer4;

    private TextView cauldron;

    // Game texts
    private ArrayList<ArrayList<String>>texts;
    private int currQuestion = 0;
    private ArrayList<String> questionTexts;
    private ArrayList<String> answerTexts;

    // Answer tracking
    private int numAnswers = 0;
    private int numCorrectAnswers = 0;

    // TextView to drag
    private TextView dragTextView;
    private boolean draggingAnswer = false;

    // Chosen child index
    private int chosenChildIdx;

    // Chosen child's text
    private String chosenChildStr;

    // Screen size for calulating sizes
    private int height;

    // textviews that are done
    private ArrayList<Integer> doneTextViews;

    // Saved answer texts
    private ArrayList<String> savedAnswerTexts;

    /**
     * Constructor for game
     * @param contextp Context of the activity
     * @param textsp List of data for the game
     * @param conceptIDp ID of the current concept
     * @param lessonIDp ID of the current lesson
     * @param nextActivityp Number indicating what the activity after the game should be
     * @param width Width of the screen in pixels
     * @param heightp Height of screen in pixels
     */
    public ChemistryGameView(Context contextp, ArrayList<ArrayList<String>> textsp,
                             int conceptIDp, int lessonIDp, int nextActivityp,
                             int width, int heightp) {
        super(contextp);

        context = contextp;

        // Screen size
        height = heightp;

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        // List of textViews that have already been used
        doneTextViews = new ArrayList<Integer>();

        if (Build.VERSION.SDK_INT < 19) {
            // Moves bucket up higher
            //      since older phones will not hide the bottom navigation bar
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = height - resources.getDimensionPixelSize(resourceId);
            }
        }

        // Get texts
        texts = textsp;

        // Set background color of page
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.chemistryGameBG));

        // Create textviews
        answer1 = new TextView(context);
        answer2 = new TextView(context);
        answer3 = new TextView(context);
        answer4 = new TextView(context);
        cauldron = new TextView(context);

        // Set up list of answers to be saved for resetting the question
        savedAnswerTexts = new ArrayList<String>();

        // Set up text
        setUp();

        RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set up drag textview
        dragTextView = new TextView(context);
        dragTextView.setTextSize(convertPixelsToDp(height / 17, context));
        dragTextView.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Top left textview
        answer1.setTextSize(convertPixelsToDp(height / 17, context));
        answer1.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Older versions have ids set differently
        if (Build.VERSION.SDK_INT < 17) {
            answer1.setId(R.id.chemistryGameTextView1);
        } else {
            answer1.setId(View.generateViewId());
        }

        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        answer1.setLayoutParams(relativeLay);
        answer1.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        answer1.setHeight((height - 70) / 3 - 10);
        answer1.setWidth((width - 40) / 2);

        // Draw potion
        answer1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion1, 0, 0);

        // Top right textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        answer2.setTextSize(convertPixelsToDp(height / 17, context));
        answer2.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Older versions have ids set differently
        if (Build.VERSION.SDK_INT < 17) {
            answer2.setId(R.id.chemistryGameTextView2);
        } else {
            answer2.setId(View.generateViewId());
        }

        answer2.setLayoutParams(relativeLay);
        answer2.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        answer2.setHeight((height - 70) / 3 - 10);
        answer2.setWidth((width - 40) / 2);

        // Draw potion
        answer2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion2, 0, 0);

        // Bottom left textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        answer3.setTextSize(convertPixelsToDp(height / 17, context));
        answer3.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Older versions have ids set differently
        if (Build.VERSION.SDK_INT < 17) {
            answer3.setId(R.id.chemistryGameTextView3);
        } else {
            answer3.setId(View.generateViewId());
        }

        answer3.setLayoutParams(relativeLay);
        answer3.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        answer3.setHeight((height - 70) / 3 - 10);
        answer3.setWidth((width - 40) / 2);

        // Draw potion
        answer3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion3, 0, 0);

        // Bottom right textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        answer4.setTextSize(convertPixelsToDp(height / 17, context));
        answer4.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Older versions have ids set differently
        if (Build.VERSION.SDK_INT < 17) {
            answer4.setId(R.id.chemistryGameTextView4);
        } else {
            answer4.setId(View.generateViewId());
        }

        answer4.setLayoutParams(relativeLay);
        answer4.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        answer4.setHeight((height - 70) / 3 - 10);
        answer4.setWidth((width - 40) / 2);

        // Draw potion
        answer4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion4, 0, 0);

        // Cauldron
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.CENTER_IN_PARENT);

        cauldron.setTextSize(convertPixelsToDp(height / 17, context));
        cauldron.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        // Older versions have ids set differently
        if (Build.VERSION.SDK_INT < 17) {
            cauldron.setId(R.id.chemistryGameTextViewAnswer);
        } else {
            cauldron.setId(View.generateViewId());
        }

        cauldron.setLayoutParams(relativeLay);
        cauldron.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        cauldron.setHeight((height - 70) / 3 + 40);

        // Make sure cauldron is the correct size
        // Draw cauldron
        Drawable cauldronDraw = ContextCompat.getDrawable(context, R.drawable.chem_cauldron);
        cauldronDraw.setBounds(new Rect(0, 0, (height - 70) * 2 / 8 + 40,
                (height - 70) * 2 / 8 + 40));
        cauldron.setCompoundDrawables(null, cauldronDraw, null, null);

        // Set touch listener on this layout
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                RelativeLayout r = (RelativeLayout) v;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Check what child if any was touched
                        for (int i=0; i<getChildCount(); i++){
                            TextView child = (TextView) r.getChildAt(i);
                            if(child != cauldron &&
                                    x > child.getLeft() && x < child.getRight() &&
                                    y > child.getTop() && y < child.getBottom()){

                                // Save that index to know what answer was selected
                                chosenChildIdx = i;

                                //touch is within this child
                                if (!doneTextViews.contains((Integer)chosenChildIdx)) {
                                    // Remove potion from that textview
                                    child.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                    // Set that textview's potion to the dragging textview
                                    if (chosenChildIdx == 0) {
                                        // Top left
                                        dragTextView.setCompoundDrawablesWithIntrinsicBounds(
                                                0, R.drawable.chem_potion1, 0, 0);
                                    } else if (chosenChildIdx == 1) {
                                        // Top right
                                        dragTextView.setCompoundDrawablesWithIntrinsicBounds(
                                                0, R.drawable.chem_potion2, 0, 0);
                                    } else if (chosenChildIdx == 2) {
                                        // Bottom left
                                        dragTextView.setCompoundDrawablesWithIntrinsicBounds(
                                                0, R.drawable.chem_potion3, 0, 0);
                                    } else if (chosenChildIdx == 3) {
                                        // Bottom right
                                        dragTextView.setCompoundDrawablesWithIntrinsicBounds(
                                                0, R.drawable.chem_potion4, 0, 0);
                                    }

                                    // Save the chosen answer's text
                                    chosenChildStr = child.getText().toString();
                                    // Begin dragging
                                    draggingAnswer = true;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (draggingAnswer) {
                            if (dragTextView.getParent() != r) {
                                // Add dragging textview to the layout if it is not there already
                                dragTextView.setVisibility(View.VISIBLE);
                                r.addView(dragTextView);
                            }

                            // Move dragging textview
                            dragTextView.setX(event.getRawX() - dragTextView.getWidth() / 2);
                            dragTextView.setY(event.getRawY() - dragTextView.getHeight() / 2);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (draggingAnswer) {
                            // Check if potion was dragged onto cauldron
                            if (x > cauldron.getLeft() && x < cauldron.getRight() &&
                                    y > cauldron.getTop() && y < cauldron.getBottom()) {

                                // Check if the answer is correct
                                if (answerTexts.contains(chosenChildStr)) {

                                    // Remove answer string if correct so it wont be
                                    //      recognized as an answer again
                                    answerTexts.remove(chosenChildStr);

                                    // Add the textview to the done list so it can't be
                                    //      dragged anymore
                                    doneTextViews.add(chosenChildIdx);

                                    // Writes caught answer to question at the top
                                    String newText = (String) cauldron.getText();
                                    if (newText.contains("_")) {
                                        newText = newText.replaceFirst("[_]", chosenChildStr);
                                        if (newText.length() > 20) {
                                            cauldron.setTextSize(
                                                    convertPixelsToDp(height / 20, context));
                                        }
                                        cauldron.setText(newText);
                                    }

                                    numCorrectAnswers += 1;
                                    // Checks if round is completed
                                    if (numCorrectAnswers == numAnswers) {
                                        // Pause
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                // Start next question
                                                currQuestion += 1;
                                                setUp();
                                            }
                                        }, 800);
                                    }
                                } else {

                                    cauldron.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameIncorrect));
                                    cauldron.setText("Incorrect");

                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            cauldron.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

                                            // Reset question
                                            cauldron.setText(questionTexts.get(0));

                                            // Reset answers
                                            answerTexts.clear();
                                            for (String text : savedAnswerTexts) {
                                                answerTexts.add(text);
                                            }
                                            numAnswers = savedAnswerTexts.size();
                                            doneTextViews.clear();

                                            // Top left
                                            answer1.setCompoundDrawablesWithIntrinsicBounds(
                                                    0, R.drawable.chem_potion1, 0, 0);
                                            // Top right
                                            answer2.setCompoundDrawablesWithIntrinsicBounds(
                                                    0, R.drawable.chem_potion2, 0, 0);
                                            // Bottom left
                                            answer3.setCompoundDrawablesWithIntrinsicBounds(
                                                    0, R.drawable.chem_potion3, 0, 0);
                                            // Bottom right
                                            answer4.setCompoundDrawablesWithIntrinsicBounds(
                                                    0, R.drawable.chem_potion4, 0, 0);

                                            numCorrectAnswers = 0;
                                        }
                                    }, 800);
                                }
                            } else {
                                // Put potion back if not dropped on the cauldron
                                TextView child = (TextView) r.getChildAt(chosenChildIdx);

                                if (chosenChildIdx == 0) {
                                    // Top left
                                    child.setCompoundDrawablesWithIntrinsicBounds(
                                            0, R.drawable.chem_potion1, 0, 0);
                                } else if (chosenChildIdx == 1) {
                                    // Top right
                                    child.setCompoundDrawablesWithIntrinsicBounds(
                                            0, R.drawable.chem_potion2, 0, 0);
                                } else if (chosenChildIdx == 2) {
                                    // Bottom left
                                    child.setCompoundDrawablesWithIntrinsicBounds(
                                            0, R.drawable.chem_potion3, 0, 0);
                                } else if (chosenChildIdx == 3) {
                                    // Bottom right
                                    child.setCompoundDrawablesWithIntrinsicBounds(
                                            0, R.drawable.chem_potion4, 0, 0);
                                }
                            }
                            // Stop dragging the chosen potion
                            // Remove the dragging textview from the layout
                            draggingAnswer = false;
                            dragTextView.setVisibility(View.GONE);
                            r.removeView(dragTextView);
                        }
                        break;
                }
                return true;
            }
        });

        // Add the textviews to the layout
        this.addView(answer1);
        this.addView(answer2);
        this.addView(answer3);
        this.addView(answer4);
        this.addView(cauldron);
    }

    /**
     * Set up question and answer text
     */
    private void setUp() {
        if (currQuestion * 2 < texts.size()) {
            // Set up current question
            questionTexts = texts.get(currQuestion * 2);
            answerTexts = texts.get(currQuestion * 2 + 1);
            savedAnswerTexts.clear();
            for (String text : answerTexts) {
                savedAnswerTexts.add(text);
            }

            // Save number of answers
            numAnswers = answerTexts.size();
            numCorrectAnswers = 0;

            // Clear the list of textviews that have already been used and
            //      can't be used as an answer again
            doneTextViews.clear();

            // Set question text
            cauldron.setText(questionTexts.get(0));
            // Set text size based on how long the question is
            if (questionTexts.get(0).length() < 20) {
                cauldron.setTextSize(convertPixelsToDp(height / 17, context));
            } else {
                cauldron.setTextSize(convertPixelsToDp(height / 20, context));
            }

            // Set answer texts and potion images
            answer1.setText(questionTexts.get(1));
            answer1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion1, 0, 0);
            answer2.setText(questionTexts.get(2));
            answer2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion2, 0, 0);
            answer3.setText(questionTexts.get(3));
            answer3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion3, 0, 0);
            answer4.setText(questionTexts.get(4));
            answer4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chem_potion4, 0, 0);
        } else {
            // End the game if all questions have been answered correctly
            endGame();
        }
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
