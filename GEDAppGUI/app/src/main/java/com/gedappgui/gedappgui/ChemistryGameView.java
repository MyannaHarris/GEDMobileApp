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
 * Last Edit: 2-19-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
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

    /*
     * Instantiate Chemistry game
     */
    public ChemistryGameView(Context contextp, ArrayList<ArrayList<String>> textsp,
                             int conceptIDp, int lessonIDp, int nextActivityp,
                             int width, int height) {
        super(contextp);

        context = contextp;

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

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

        if (Build.VERSION.SDK_INT < 17) {
            answer1.setId(R.id.chemistryGameTextView1);
        } else {
            answer1.setId(View.generateViewId());
        }

        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        answer1.setLayoutParams(relativeLay);
        answer1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        answer1.setHeight((height - 70) / 3);
        answer1.setWidth((width - 40) / 2);

        // Top right textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        answer2.setTextSize(convertPixelsToDp(height / 17, context));
        answer2.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        if (Build.VERSION.SDK_INT < 17) {
            answer2.setId(R.id.chemistryGameTextView2);
        } else {
            answer2.setId(View.generateViewId());
        }

        answer2.setLayoutParams(relativeLay);
        answer2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        answer2.setHeight((height - 70) / 3);
        answer2.setWidth((width - 40) / 2);

        // Bottom left textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        answer3.setTextSize(convertPixelsToDp(height / 17, context));
        answer3.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        if (Build.VERSION.SDK_INT < 17) {
            answer3.setId(R.id.chemistryGameTextView3);
        } else {
            answer3.setId(View.generateViewId());
        }

        answer3.setLayoutParams(relativeLay);
        answer3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        answer3.setHeight((height - 70) / 3);
        answer3.setWidth((width - 40) / 2);

        // Bottom right textview
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        answer4.setTextSize(convertPixelsToDp(height / 17, context));
        answer4.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        if (Build.VERSION.SDK_INT < 17) {
            answer4.setId(R.id.chemistryGameTextView4);
        } else {
            answer4.setId(View.generateViewId());
        }

        answer4.setLayoutParams(relativeLay);
        answer4.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        answer4.setHeight((height - 70) / 3);
        answer4.setWidth((width - 40) / 2);

        // Cauldron
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.CENTER_IN_PARENT);

        cauldron.setTextSize(convertPixelsToDp(height / 17, context));
        cauldron.setTextColor(ContextCompat.getColor(context, R.color.chemistryGameText));

        if (Build.VERSION.SDK_INT < 17) {
            cauldron.setId(R.id.chemistryGameTextViewAnswer);
        } else {
            cauldron.setId(View.generateViewId());
        }

        cauldron.setLayoutParams(relativeLay);
        cauldron.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cauldron.setHeight((height - 70) / 3);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                RelativeLayout r = (RelativeLayout) v;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        for (int i=0; i<getChildCount(); i++){
                            TextView child = (TextView) r.getChildAt(i);
                            if(child != cauldron &&
                                    x > child.getLeft() && x < child.getRight() &&
                                    y > child.getTop() && y < child.getBottom()){

                                //touch is within this child
                                dragTextView.setText(child.getText());
                                draggingAnswer = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (draggingAnswer) {
                            dragTextView.setVisibility(View.GONE);
                            r.removeView(dragTextView);
                            dragTextView.setVisibility(View.VISIBLE);
                            r.addView(dragTextView);
                            dragTextView.setX(event.getRawX() - dragTextView.getWidth() / 2);
                            dragTextView.setY(event.getRawY() - dragTextView.getHeight() / 2);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (draggingAnswer) {
                            if (x > cauldron.getLeft() && x < cauldron.getRight() &&
                                    y > cauldron.getTop() && y < cauldron.getBottom()) {
                                if (answerTexts.contains(dragTextView.getText())) {
                                    answerTexts.remove(dragTextView.getText());

                                    // Writes caught answer to question at the top
                                    String newText = (String) cauldron.getText();
                                    if (newText.contains("_")) {
                                        newText = newText.replaceFirst("[_]", (String) dragTextView.getText());
                                        cauldron.setText(newText);
                                    }

                                    numCorrectAnswers += 1;
                                    if (numCorrectAnswers == numAnswers) {
                                        // Pause
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                // Start next question
                                                currQuestion += 1;
                                                setUp();
                                            }
                                        }, 500);
                                    }
                                }
                            }
                            draggingAnswer = false;
                            dragTextView.setVisibility(View.GONE);
                            r.removeView(dragTextView);
                        }
                        break;
                }
                return true;
            }
        });

        this.addView(answer1);
        this.addView(answer2);
        this.addView(answer3);
        this.addView(answer4);
        this.addView(cauldron);
    }

    /*
     * Set up question and answer text
     */
    private void setUp() {
        if (currQuestion * 2 < texts.size()) {
            // Set up current question
            questionTexts = texts.get(currQuestion * 2);
            answerTexts = texts.get(currQuestion * 2 + 1);
            //System.out.println(answerTexts);
            numAnswers = answerTexts.size();
            numCorrectAnswers = 0;

            cauldron.setText(questionTexts.get(0));
            answer1.setText(questionTexts.get(1));
            answer2.setText(questionTexts.get(2));
            answer3.setText(questionTexts.get(3));
            answer4.setText(questionTexts.get(4));
        } else {
            endGame();
        }
    }

    /*
     * Move on to game end page
     */
    private void endGame() {
        Intent intent = new Intent(context, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID", conceptID);
        intent.putExtra("lessonID", lessonID);
        context.startActivity(intent);
    }

    /*
     * Change pixel measurement into dp measurement
     */
    public static float convertPixelsToDp(float px,Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }
}
