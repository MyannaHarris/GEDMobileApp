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
 * Last Edit: 3-20-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;

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

    // Button to end the endless game play
    private Button endButton;

    // Size of button to end
    private int endButtonSize = 0;

    // List of textviews that are locked in
    private ArrayList<Integer> lockedTextViews;

    // For Haptic Feedback
    private Vibrator myVib;

    public OrderingGameView(Context contextp, ArrayList<ArrayList<String>> textsp,
                            int conceptIDp, int lessonIDp, final int nextActivityp,
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

        // Set up vibrator service
        myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        // Set up text views so no null exception happens
        lastTextView = new TextView(context);
        dragTextView = new TextView(context);

        // Set up list of textviews that are locked in
        lockedTextViews = new ArrayList<Integer>();

        // Set background color of page
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameBG));

        // Set orientation to make a vertical list
        this.setOrientation(LinearLayout.VERTICAL);

        // Button to end the endless game play
        if (nextActivity == 1) {

            LinearLayout.LayoutParams linearLayoutButton = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    (height - (30 + 10 * 5)) / 8);

            linearLayoutButton.setMargins(0, 5, 0, 5);

            endButton = new Button(context);
            endButton.setLayoutParams(linearLayoutButton);
            endButton.setTextSize(convertPixelsToDp(height / 17, context));
            endButton.setTextColor(ContextCompat.getColor(context, R.color.orderingGameLockedColor));
            endButton.setText("End Game");
            endButton.setHeight((height - (30 + 10 * 5)) / 8);

            endButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    endGame();
                }
            });

            endButtonSize = (height - (30 + 10 * 5)) / 8 + 10;
        }

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
        topLabel.setTextColor(ContextCompat.getColor(context, R.color.orderingGameLockedColor));
        topLabel.setTextSize(convertPixelsToDp(height / 17, context));
        topLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameTopColor));

        // Set up bottomLabel
        bottomLabel.setLayoutParams(linearParams);
        bottomLabel.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        bottomLabel.setTextColor(ContextCompat.getColor(context, R.color.orderingGameLockedColor));
        bottomLabel.setTextSize(convertPixelsToDp(height / 17, context));
        bottomLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameBottomColor));

        // Set up the current question
        setUp();

        // Special drag textview parameters
        LinearLayout.LayoutParams dragParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, (height - endButtonSize - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
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

                // For changing loops when the end game button exists
                int start = 1;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save selected textView if a textview is selected to drag
                        start = 1;
                        if (nextActivity == 1) {
                            start = 2;
                        }
                        for (int i=start; i<getChildCount() - 1; i++){
                            TextView child = (TextView) linearLayout.getChildAt(i);
                            if(x > child.getLeft() && x < child.getRight() &&
                                    y > child.getTop() && y < child.getBottom() &&
                                    !lockedTextViews.contains(i)){

                                // Touch is within this child
                                // Set up dragging textviews text and start dragging
                                lastTextView = child;
                                dragTextView.setText(child.getText());
                                dragTextView.setTag(child.getTag());
                                dragging = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (dragging) {
                            // Blank out textviews that you pass over
                            lastTextView.setText("");

                            // If dragging textview is not in layout, put it in layout
                            if (dragTextView.getParent() != linearLayout) {
                                dragTextView.setVisibility(View.VISIBLE);
                                linearLayout.addView(dragTextView);
                            }

                            // Move dragging textview by changing its coordinates
                            dragTextView.setX(event.getRawX() - dragTextView.getWidth() / 2);
                            dragTextView.setY(event.getRawY() - dragTextView.getHeight() / 2);

                            // Make it continuously show changes in order
                            start = 1;
                            if (nextActivity == 1) {
                                start = 2;
                            }
                            for (int i = start; i < getChildCount() - 2; i++) {
                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (x > child.getLeft() && x < child.getRight() &&
                                        y > child.getTop() && y < child.getBottom() &&
                                        !lockedTextViews.contains(i)) {

                                    // Touch is within this child
                                    // Swap texts in textviews that user is passing
                                    //      over with dragging textview
                                    lastTextView.setText(child.getText());
                                    lastTextView.setTag(child.getTag());
                                    child.setText("");
                                    lastTextView = child;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (dragging && getChildCount() > (answerTexts.size() + 2)) {
                            // Check if dropped in an actual position
                            boolean inTextView = false;
                            for (int i = 0; i < getChildCount(); i++) {
                                // Switch dragged item with item in position dropped on

                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (x > child.getLeft() && x < child.getRight() &&
                                        y > child.getTop() && y < child.getBottom()) {

                                    if (!lockedTextViews.contains(i)) {

                                        int chosenI = 0;

                                        if (i == 0) {
                                            if (nextActivity == 1) {
                                                child = (TextView) linearLayout.getChildAt(2);
                                                chosenI = 2;
                                            } else {
                                                child = (TextView) linearLayout.getChildAt(1);
                                                chosenI = 1;
                                            }
                                        } else if (nextActivity == 1 && i == 1) {
                                            child = (TextView) linearLayout.getChildAt(2);
                                            chosenI = 2;
                                        } else if (i > getChildCount() - 3) {
                                            child = (TextView) linearLayout.getChildAt(getChildCount() - 3);
                                            chosenI = getChildCount() - 3;
                                        }

                                        if (lockedTextViews.contains(chosenI)) {
                                            lastTextView.setText(dragTextView.getText());
                                            lastTextView.setTag(dragTextView.getTag());
                                        } else {

                                            // Touch is within this child
                                            // Set new textview values to reflect change in order
                                            lastTextView.setText(child.getText());
                                            lastTextView.setTag(child.getTag());
                                            child.setText(dragTextView.getText());
                                            child.setTag(dragTextView.getTag());
                                        }

                                    } else {
                                        lastTextView.setText(dragTextView.getText());
                                        lastTextView.setTag(dragTextView.getTag());
                                    }

                                    inTextView = true;
                                }
                            }

                            // Return number to last textview if dragged off of textViews completely
                            if (!inTextView) {
                                lastTextView.setText(dragTextView.getText());
                                lastTextView.setTag(dragTextView.getTag());
                            }

                            // Check if order is right
                            boolean questionDone = true;
                            start = 1;
                            if (nextActivity == 1) {
                                start = 2;
                            }
                            for (int i = start; i < getChildCount() - 2; i++) {
                                TextView child = (TextView) linearLayout.getChildAt(i);
                                if (!((String) child.getTag().toString()).equals(answerTexts.get(i - start))) {
                                    questionDone = false;

                                    // incorrect vibrate
                                    long[] incorrectBuzz = {0,55,40,55};
                                    myVib.vibrate(incorrectBuzz, -1); // vibrate

                                } else {

                                    // vibrate when correct
                                    myVib.vibrate(150);

                                    lockedTextViews.add(i);
                                    child.setTextColor(ContextCompat.getColor(context,
                                            R.color.orderingGameLockedColor));
                                }
                            }

                            // Move on to next question if ordered correctly
                            if (questionDone) {
                                // Pause if correct so user can see answer
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        // Set up next question
                                        currQuestion += 1;
                                        setUp();
                                    }
                                }, 800);
                            }

                            // Delete moving textView from layout
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

    /**
     * Set up question and answer text
     */
    private void setUp() {
        if (currQuestion * 2 < texts.size() || nextActivity == 1) {

            if (nextActivity == 1 && currQuestion * 2 >= texts.size()) {
                currQuestion = 0;
            }

            // Clear locked textviews for next round
            lockedTextViews.clear();

            // If there are more questions left

            // Set up item texts
            questionTexts = texts.get(currQuestion * 2);
            answerTexts = texts.get(currQuestion * 2 + 1);

            // Set up label heights
            topLabel.setHeight((height - endButtonSize - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
            bottomLabel.setHeight((height - endButtonSize - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));

            // Set up current labels
            topLabel.setText("Greatest");
            bottomLabel.setText("Least");

            // Nothing dragging at start
            dragging = false;

            // Delete item textviews to load new ones
            items.clear();

            // Delete items in Layout
            this.removeAllViews();

            // Set up items to order
            for (int i = 0; i < questionTexts.size(); i++) {
                TextView newTextView = new TextView(context);
                newTextView.setTag(questionTexts.get(i));
                newTextView.setText(toHTML(questionTexts.get(i)));
                newTextView.setLayoutParams(linearParams);
                newTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                newTextView.setHeight((height - endButtonSize - (30 + 10 * answerTexts.size())) / (answerTexts.size() + 2));
                newTextView.setTextColor(ContextCompat.getColor(context, R.color.orderingGameText));
                newTextView.setTextSize(convertPixelsToDp(height / 17, context));
                newTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.orderingGameMiddleColor));

                items.add(newTextView);
            }

            // Add views to layout
            if (nextActivity == 1) {
                this.addView(endButton);
            }
            this.addView(topLabel);
            for (int i = 0; i < items.size(); i++) {
                this.addView(items.get(i));
            }
            this.addView(bottomLabel);

        } else {
            // If done with all rounds, end game
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
