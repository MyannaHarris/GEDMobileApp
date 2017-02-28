/*
 * OrderingGameView.java
 *
 * Ordering game
 *
 * View runs the Ordering game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-24-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderingGameView extends LinearLayout {

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Current activity context
    private Context context;

    // Save current question info
    private ArrayList<ArrayList<String>>texts;
    private int currQuestion = 0;
    private ArrayList<String> questionTexts;
    private ArrayList<String> answerTexts;

    // Save list of textViews
    private ArrayList<TextView> items;

    // Text views for top and bottom titles
    private TextView topLabel;
    private TextView bottomLabel;

    // Layout for all textviews
    private LinearLayout.LayoutParams linearParams;

    // Screen info
    private int height;

    // Save last touched textview
    private TextView lastTextView;

    // Text view to drag
    private TextView dragTextView;

    // Bool to check if something is moving
    private boolean dragging = false;

    public OrderingGameView(Context contextp, ArrayList<ArrayList<String>> textsp,
                            int conceptIDp, int lessonIDp, int nextActivityp,
                            int width, int heightp) {
        super(contextp);

        context = contextp;

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        // Save screen info
        height = heightp;

        // Get texts
        texts = textsp;

        // Set up text views so no null exception happens
        lastTextView = new TextView(context);
        dragTextView = new TextView(context);

        // Set background color of page
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameBG));

        // Set orientation to make a vertical list
        this.setOrientation(LinearLayout.VERTICAL);

        // Create textViews
        topLabel = new TextView(context);
        bottomLabel = new TextView(context);
        items = new ArrayList<TextView>();

        // Layout variable to set layout of textviews
        linearParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearParams.setMargins(10, 2, 10, 3);

        // Set up topLabel
        topLabel.setLayoutParams(linearParams);
        topLabel.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        topLabel.setTextColor(ContextCompat.getColor(context, R.color.orderingGameText));
        topLabel.setTextSize(convertPixelsToDp(height / 17, context));
        topLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameTopColor));

        // Set up bottomLabel
        bottomLabel.setLayoutParams(linearParams);
        bottomLabel.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        bottomLabel.setTextColor(ContextCompat.getColor(context, R.color.orderingGameText));
        bottomLabel.setTextSize(convertPixelsToDp(height / 17, context));
        bottomLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameBottomColor));

        // Set up the current question
        setUp();

        // Special drag textview parameters
        LinearLayout.LayoutParams dragParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, (height - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
        dragTextView.setLayoutParams(dragParams);
        dragTextView.setTextColor(ContextCompat.getColor(context, R.color.orderingGameText));
        dragTextView.setTextSize(convertPixelsToDp(height / 17, context));
        dragTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        // Set up listener for dragging and checking answer
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                LinearLayout linearLayout = (LinearLayout) v;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save selected textView if a textview is selected to drag
                        for (int i=1; i<getChildCount() - 1; i++){
                            TextView child = (TextView) linearLayout.getChildAt(i);
                            if(x > child.getLeft() && x < child.getRight() &&
                                    y > child.getTop() && y < child.getBottom()){

                                //touch is within this child
                                lastTextView = child;
                                dragTextView.setText((String) child.getText().toString());
                                dragging = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (dragging) {
                            lastTextView.setText("");

                            if (dragTextView.getParent() != linearLayout) {
                                dragTextView.setVisibility(View.VISIBLE);
                                linearLayout.addView(dragTextView);
                            }

                            dragTextView.setX(event.getRawX() - dragTextView.getWidth() / 2);
                            dragTextView.setY(event.getRawY() - dragTextView.getHeight() / 2);

                            // Make it continuously show changes in order
                            for (int i = 1; i < getChildCount() - 2; i++) {
                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (x > child.getLeft() && x < child.getRight() &&
                                        y > child.getTop() && y < child.getBottom()) {

                                    //touch is within this child
                                    lastTextView.setText(child.getText());
                                    child.setText("");
                                    lastTextView = child;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (dragging && getChildCount() > (answerTexts.size() + 2)) {
                            // Swicth last position of order
                            boolean inTextView = false;
                            for (int i = 0; i < getChildCount(); i++) {
                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (x > child.getLeft() && x < child.getRight() &&
                                        y > child.getTop() && y < child.getBottom()) {

                                    if (i == 0) {
                                        child = (TextView) linearLayout.getChildAt(1);
                                    } else if (i > getChildCount() - 3) {
                                        child = (TextView) linearLayout.getChildAt(getChildCount() - 3);
                                    }

                                    //touch is within this child
                                    lastTextView.setText(child.getText());
                                    child.setText(dragTextView.getText());

                                    inTextView = true;
                                }
                            }

                            // Return number to last textview if dragged off of textViews completely
                            if (!inTextView) {
                                lastTextView.setText(dragTextView.getText());
                            }

                            // Check if order is right
                            boolean questionDone = true;
                            for (int i = 1; i < getChildCount() - 2; i++) {
                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (!((String) child.getText().toString()).equals(answerTexts.get(i - 1))) {
                                    questionDone = false;
                                }
                            }

                            if (questionDone) {
                                // Pause
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        // Set up next question
                                        currQuestion += 1;
                                        setUp();
                                    }
                                }, 500);
                            }

                            // Delete moving textView
                            dragging = false;
                            dragTextView.setVisibility(View.GONE);
                            linearLayout.removeView(dragTextView);
                        }
                        dragging = false;
                        break;
                }
                return true;
            }
        });
    }

    /*
     * Set up question and answer text
     */
    private void setUp() {
        if (currQuestion * 2 < texts.size()) {
            // Set up item texts
            questionTexts = texts.get(currQuestion * 2);
            answerTexts = texts.get(currQuestion * 2 + 1);
            //System.out.println(answerTexts);

            // Set up label heights
            topLabel.setHeight((height - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
            bottomLabel.setHeight((height - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));

            // Set up current labels
            topLabel.setText(questionTexts.get(0));
            bottomLabel.setText(questionTexts.get(1));

            // Remove label texts
            questionTexts.remove(0);
            questionTexts.remove(0);

            // Nothing dragging at start
            dragging = false;

            // Delete item textviews
            items.clear();

            // Delete items in Layout
            this.removeAllViews();

            // Set up items to order
            for (int i = 0; i < questionTexts.size(); i++) {
                TextView newTextView = new TextView(context);
                newTextView.setText(questionTexts.get(i));
                newTextView.setLayoutParams(linearParams);
                newTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                newTextView.setHeight((height - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
                newTextView.setTextColor(ContextCompat.getColor(context, R.color.orderingGameText));
                newTextView.setTextSize(convertPixelsToDp(height / 17, context));
                newTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameMiddleColor));

                items.add(newTextView);
            }

            this.addView(topLabel);
            for (int i = 0; i < items.size(); i++) {
                this.addView(items.get(i));
            }
            this.addView(bottomLabel);

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
