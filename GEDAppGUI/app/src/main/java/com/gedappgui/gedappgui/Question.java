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
 * Last Edit: 3-20-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Question extends AppCompatActivity {

    // Globals to keep track of how the user is doing answering questions
    private int selectedAnswer = 0;
    private DatabaseHelper dbHelper;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int currentLevel = 2;
    private int numQuestion = 0;
    private int totalCorrect = 0;
    private String correctAnswerStr = "";
    private int correctAnswerIdx = 0;
    private int totalRetries = 0;

    private int lessonID = 1;
    private int conceptID;
    private int redo;
    private ArrayList<String> questionText;
    private ArrayList<ArrayList<ArrayList<String>>> allQuestions;

    // These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    // Save context for surface holder
    private Context context;

    // Save coordinates for drawing
    ArrayList<Path> pathList;
    private Path path;

    // for playing sound
    MediaPlayer mediaPlayer;

    /**
     * Starts the activity and shows corresponding view on screen
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // set up sound
        mediaPlayer = MediaPlayer.create(this, R.raw.short_success);

        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        totalRetries = mIntent.getIntExtra("totalRetries", 0);
        redo = mIntent.getIntExtra("redoComplete", 0);

        // Create buttons of dynamic size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Button submitbtn = (Button)findViewById(R.id.submit_answer_button);
        submitbtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        // Set heights for button
        //ViewGroup.LayoutParams params = submitbtn.getLayoutParams();
        //params.height = (height/17);

        //submitbtn.setLayoutParams(params);

        // Set dynamic size for clear button
        Button clearButton = (Button)findViewById(R.id.clearCanvas_button);
        clearButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(height/30));

        // Set heights for button
        //params = clearButton.getLayoutParams();
        //params.height = (height/17);

        //clearButton.setLayoutParams(params);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get all questions
        dbHelper = new DatabaseHelper(this);
        allQuestions = dbHelper.getAllQuestions(lessonID);

        // Save context for surface holder
        context = this;

        // Put new question text up
        String question = "";
        questionText = allQuestions.get(currentLevel-1).get(0);
        String[] template = questionText.get(0).split("%NUM%");
        String[] text =  questionText.get(1).split(";");
        int j=0;
        while (j<template.length || j<text.length) {
            if (j<template.length) {
                question += template[j];
            }
            if (j<text.length) {
                question += text[j];
            }
            j++;
        }
        TextView questionTextView = (TextView) findViewById(R.id.question_textView);
        questionTextView.setText(toHTML(question));
        questionTextView.setTextSize(20);

        // Set radio buttons
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            String textAnswer = questionText.get(i+2);
            ((RadioButton) radioGroup.getChildAt(i)).setText(toHTML(textAnswer));
            if (textAnswer.equals(questionText.get(6))) {
                correctAnswerIdx = i;
            }
        }

        // Save what the new correct answer should be
        correctAnswerStr = questionText.get(6);
        allQuestions.get(currentLevel-1).remove(0);

        // Set up surface view
        SurfaceView drawingView = (SurfaceView) findViewById(R.id.drawing_view);

        // Save holder for drawing to canvas
        surfaceHolder = drawingView.getHolder();

        // Coordinates that user has drawn in
        pathList = new ArrayList<Path>();

        // Set up paint
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.canvasForeground));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setTextSize(height/17);

        // Set up canvas in surfaceview
        drawingView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Do some drawing when surface is ready
                canvas = holder.lockCanvas();
                canvas.drawColor(ContextCompat.getColor(context, R.color.canvasBackground));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("Draw here", canvas.getWidth() / 2 - paint.measureText("Draw here") / 2,
                        canvas.getHeight() / 2, paint);
                paint.setStyle(Paint.Style.STROKE);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        // Set up touch listener to capture drawing
        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (surfaceHolder.getSurface().isValid()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            canvas = surfaceHolder.lockCanvas();
                            canvas.drawColor(ContextCompat.getColor(context, R.color.canvasBackground));
                            path = new Path();
                            path.moveTo(event.getX(), event.getY());
                            canvas.drawPath(path, paint);
                            for (Path p : pathList) {
                                canvas.drawPath(p, paint);
                            }
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            canvas = surfaceHolder.lockCanvas();
                            canvas.drawColor(ContextCompat.getColor(context, R.color.canvasBackground));
                            path.lineTo(event.getX(), event.getY());
                            canvas.drawPath(path, paint);
                            for (Path p : pathList) {
                                canvas.drawPath(p, paint);
                            }
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            break;
                        case MotionEvent.ACTION_UP:
                            canvas = surfaceHolder.lockCanvas();
                            canvas.drawColor(ContextCompat.getColor(context, R.color.canvasBackground));
                            path.lineTo(event.getX(), event.getY());
                            pathList.add(path);
                            for (Path p : pathList) {
                                canvas.drawPath(p, paint);
                            }
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            break;
                    }
                }
                return true;
            }
        });

        // Set text size for page
        TextView textView = (TextView)findViewById(R.id.question_textView);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        RadioButton radioButton = (RadioButton)findViewById(R.id.question_answer1);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        radioButton = (RadioButton)findViewById(R.id.question_answer2);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        radioButton = (RadioButton)findViewById(R.id.question_answer3);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
        radioButton = (RadioButton)findViewById(R.id.question_answer4);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
    }

    /**
     * Hides bottom navigation bar
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
     * Listens for the back button on the bottom navigation bar
     * Stops app from allowing the back button to do anything
     */
    @Override
    public void onBackPressed() {
        // Do nothing when back pressed from home screen
    }

    /**
     * Sets what menu will be in the action bar
     * @param menu The options menu in which we place the items.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // If lesson was previously completed, the user can go to main page or settings
        if (dbHelper.isLessonAlreadyDone(lessonID)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.homeonlymenu, menu);
        }

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
     * Determines whether or not an answer was correct and shows the correct answer if it was not
     * Called when the user submits an answer
     * @param view current view
     */
    public void evaluateAnswer(View view) {
        Button submitButton = (Button) view;

        // Make sure an answer is selected and submit button says Submit
        if (selectedAnswer > 0 && submitButton.getText().equals("Submit")) {
            // If answer has been selected, submit and check it

            // Disable radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
            }

            // Change submit button's text
            submitButton.setText("Continue");

            // Get selected answer for comparison
            String selectedString =
                    questionText.get(selectedAnswer+1);

            // Check if answer is correct
            if (selectedString.equals(correctAnswerStr)) {

                mediaPlayer.start();
                correctAnswers += 1;
                totalCorrect += 1;
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(this, R.color.questionCorrect)
                );
            } else {
                incorrectAnswers += 1;
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(this, R.color.questionIncorrect)
                );

                // Show correct answer
                TextView questionTextView = (TextView) findViewById(R.id.question_textView);
                questionTextView.append(toHTML(" <br />Correct answer: " + correctAnswerStr));
            }

            // Clear out selected answer
            selectedAnswer = 0;

            numQuestion += 1;

        }
        // If button is used to continue
        else if (submitButton.getText().equals("Continue")) {

            // If user gets two incorrect answers, difficulty decreases or they are given more help,
            //      depending on what their level is
            if (incorrectAnswers > 1) {
                if (currentLevel < 2) {
                    Intent intent = new Intent(this, Redo.class);
                    intent.putExtra("conceptID",conceptID);
                    intent.putExtra("lessonID",lessonID);
                    intent.putExtra("totalRetries",totalRetries);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    currentLevel -= 1;
                    incorrectAnswers = 0;
                    correctAnswers = 0;
                }
            }

            // If user gets two correct answers, difficulty increases or they are done, depending
            //      on what their level is
            if (correctAnswers > 1) {
                if (currentLevel > 2) {
                    Intent intent = new Intent(this, Success.class);
                    intent.putExtra("lessonID", lessonID);
                    intent.putExtra("conceptID", conceptID);
                    intent.putExtra("totalQuestions", numQuestion);
                    intent.putExtra("totalCorrect", totalCorrect);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    currentLevel += 1;
                    incorrectAnswers = 0;
                    correctAnswers = 0;
                }
            }

            // If user has answered twenty questions and still hasn't passed the lesson, send them
            //      to Redo page
            if (numQuestion > 19) {
                Intent intent = new Intent(this, Redo.class);
                intent.putExtra("conceptID",conceptID);
                intent.putExtra("lessonID",lessonID);
                intent.putExtra("totalRetries",totalRetries);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            // Get new question
            if (allQuestions.get(currentLevel-1).size() < 1) {
                allQuestions = dbHelper.getAllQuestions(lessonID);
            }
            questionText = allQuestions.get(currentLevel-1).get(0);

            // Put new question text up
            String question = "";
            String[] template = questionText.get(0).split("%NUM%");
            String[] text =  questionText.get(1).split(";");
            int j=0;
            while (j<template.length || j<text.length) {
                if (j<template.length) {
                    question += template[j];
                }
                if (j<text.length) {
                    question += text[j];
                }
                j++;
            }
            TextView questionTextView = (TextView) findViewById(R.id.question_textView);
            questionTextView.setText(toHTML(question));

            // Set radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.question_answer_group);
            radioGroup.clearCheck();
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                String textAnswer = questionText.get(i+2);
                ((RadioButton) radioGroup.getChildAt(i)).setText(toHTML(textAnswer));
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(
                        ContextCompat.getColor(this, R.color.colorBodyText));
            }

            // Save what the new correct answer should be
            correctAnswerStr = questionText.get(6);

            submitButton.setText("Submit");

            allQuestions.get(currentLevel-1).remove(0);

        }
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

    /**
     * Saves the answer a user selects
     * Called when the user selects an answer
     * @param view current view
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

    /**
     * Clears canvas when clicked
     * Called when the user clicks the clear button
     * @param view current view
     */
    public void clearCanvas(View view) {
        // Delete all paths
        pathList.clear();

        // Draw blank canvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(ContextCompat.getColor(context, R.color.canvasBackground));
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
