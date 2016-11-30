/*
 * Question.java
 *
 * Question page activity
 *
 * Hosts the questions that the user has to answer to unlock the lesson
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-6-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class Question extends AppCompatActivity {

    private int selectedAnswer = 0;
    private DatabaseHelper dbHelper;
    private int correctAnswers = 0;
    private int numQuestion = 0;
    private String correctAnswerStr = "";

    private int lessonID = 1;
    int conceptID;
    ArrayList<Integer> tempRandomArray = new ArrayList<Integer>();
    ArrayList<String> questionTexts;
    ArrayList<ArrayList<String>> questionAnswers;

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get question group info
        dbHelper = new DatabaseHelper(this);
        questionTexts = dbHelper.selectQuestionTexts(lessonID);
        questionAnswers = dbHelper.selectQuestionAnswers(lessonID);

        // Randomly order questions
        for (int i = 0; i < questionTexts.size(); i++) {
            tempRandomArray.add(i);
        }
        Collections.shuffle(tempRandomArray);

        // Put new question text up
        String text = questionTexts.get(tempRandomArray.get(numQuestion));
        TextView questionTextView = (TextView) findViewById(R.id.question_textView);
        questionTextView.setText(text);
        questionTextView.setTextSize(20);

        // set radio buttons
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            String textAnswer = questionAnswers.get(tempRandomArray.get(numQuestion)).get(i);
            ((RadioButton) radioGroup.getChildAt(i)).setText(textAnswer);
            ((RadioButton) radioGroup.getChildAt(i)).setTextSize(20);
        }

        // Save what the new correct answer should be
        int lastIdx = questionAnswers.get(tempRandomArray.get(numQuestion)).size() - 1;
        correctAnswerStr = questionAnswers.get(tempRandomArray.get(numQuestion)).get(lastIdx);
    }

    /*
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
    }

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
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

    /*
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /*
     * Called when enough questions were answered correctly
     * Opens the Success page
     */
    public void goToSuccess(View view) {
        Intent intent = new Intent(this, Success.class);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * Called when not enough questions were answered correctly
     * Opens the Redo page
     */
    public void goToRedo(View view) {
        Intent intent = new Intent(this, Redo.class);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
     * Called when user submits answer
     * Shows whether answer was correct
     */
    public void evaluateAnswer(View view) {
        Button submitButton = (Button) view;

        if (selectedAnswer > 0 && submitButton.getText().equals("Submit")) {
            // If an swer has been selected, submit and check it

            // disable radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
            }

            // Change submit button's text
            submitButton.setText("Continue");

            // Get selected answer for comparison
            String selectedString =
                    questionAnswers.get(tempRandomArray.get(numQuestion)).get(selectedAnswer-1);

            // Check if answer is correct
            if (selectedString.equals(correctAnswerStr)) {
                correctAnswers += 1;
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(Color.GREEN);
            } else {
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(Color.RED);
            }
            System.out.println(correctAnswers);

            // Clear out selected answer
            selectedAnswer = 0;

            // Check if user has tried all questions
            // Fail if tried all and still not passed
            if (numQuestion == questionTexts.size()-1) {
                Intent intent = new Intent(this, Redo.class);
                intent.putExtra("conceptID",conceptID);
                intent.putExtra("lessonID",lessonID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                numQuestion += 1;
            }

        } else if (submitButton.getText().equals("Continue")) {
            // If button is used to continue

            // If 3 questions were gotten right, pass and move on to success
            if (correctAnswers == 3) {
                Intent intent = new Intent(this, Success.class);
                intent.putExtra("lessonID", lessonID);
                intent.putExtra("conceptID", conceptID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            // else
            // get new question

            // Put new question text up
            String text = questionTexts.get(tempRandomArray.get(numQuestion));
            TextView questionTextView = (TextView) findViewById(R.id.question_textView);
            questionTextView.setText(text);

            // Set radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                String textAnswer = questionAnswers.get(tempRandomArray.get(numQuestion)).get(i);
                ((RadioButton) radioGroup.getChildAt(i)).setText(textAnswer);
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
                ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(Color.parseColor("#cccccc"));
            }

            // Save what the new correct answer should be
            int lastIdx = questionAnswers.get(tempRandomArray.get(numQuestion)).size() - 1;
            correctAnswerStr = questionAnswers.get(tempRandomArray.get(numQuestion)).get(lastIdx);

            submitButton.setText("Submit");


        }
    }

    /*
     * Called when selects an answer
     * Saves what answer was selected
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.question_answer1:
                if (checked)
                    selectedAnswer = 1;
                    break;
            case R.id.question_answer2:
                if (checked)
                    selectedAnswer = 2;
                    break;
            case R.id.question_answer3:
                if (checked)
                    selectedAnswer = 3;
                    break;
            case R.id.question_answer4:
                if (checked)
                    selectedAnswer = 4;
                    break;
            default:
                break;
        }
    }
}
