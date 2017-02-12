/*
 * BucketGameView.java
 *
 * Bucket game
 *
 * View runs the bucket game
 *
 * https://www.simplifiedcoding.net/android-game-development-tutorial-1/
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-6-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class BucketGameView extends SurfaceView implements Runnable  {

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // boolean variable to track if the game is playing or not
    volatile boolean playing;

    // The game thread
    private Thread gameThread = null;

    // Character bucket that the user moves back and forth
    private Bucket bucket;

    // These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    // Array of numbers to fall from the top
    private BucketNumber[] numbers;

    // Number of numbers to have falling from the top
    private int numberCount = 5;

    // Information for the game
    // Question, answers, options, etc.
    private ArrayList<String> answers;
    private int correctAnswers = 0;
    private int numAnswers = 0;
    private String question;
    private int width;
    private int height;
    private int questionHeight;
    private boolean showResult = false;
    private boolean correctAnswer = false;
    private int showResultTimer = 10;

    // Limit FPS
    private long startTime;
    private long endTime;

    // Current question tracking
    private ArrayList<ArrayList<String>> gameQuestions;
    private int currentQuestion = 0;

    // Screen info
    private Context context;

    // Pause to show equation
    private int showFinishedGameTimer;

    /*
     * Constructor
     */
    public BucketGameView(Context contextp, int widthp, int heightp,
                          ArrayList<ArrayList<String>> gameQuestionsp, int conceptIDp,
                          int lessonIDp, int nextActivityp) {
        super(contextp);

        // Save context
        context = contextp;

        // gameQuestions has all the info for the game
        // 0 - texts
        // 1 - answers (0 - question)
        gameQuestions = gameQuestionsp;

        // The text to fall from the sky
        ArrayList<String> texts = gameQuestions.get(currentQuestion);
        // The question and answers
        ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

        // Info for dynamically making size of game parts
        numberCount = texts.size();
        width = widthp;
        height = heightp;

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        // Save the question
        question = answersStr.get(0);
        answersStr.remove(0);

        // Save the answers list
        answers = answersStr;
        numAnswers = answers.size();

        // Initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize((float)height / 17);

        // Get question text height
        Rect bounds = new Rect();
        paint.getTextBounds(question, 0, question.length(), bounds);
        questionHeight = bounds.height();

        // Pause to show equation
        showFinishedGameTimer = 10;

        // Initializing player object
        //      this time also passing screen size to player constructor
        bucket = new Bucket(context, width, height, questionHeight);

        // Initializing number object array
        numbers = new BucketNumber[numberCount];
        for(int i=0; i<numberCount; i++){
            numbers[i] = new BucketNumber(width, height, texts.get(i),
                    questionHeight);
        }

        // Save start time to limit fps
        startTime = System.currentTimeMillis();
    }

    /*
     * Game loop
     * Calls the update and such
     * Limits the FPS
     */
    @Override
    public void run() {
        while (playing) {
            // Limit fps
            endTime = System.currentTimeMillis();
            long dt = endTime - startTime;
            if (dt < 40)
                try {
                    Thread.sleep(40 - dt);
                } catch (InterruptedException e) {
                }
            startTime = System.currentTimeMillis();


            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }

    /*
     * Called during game loop
     * Updates where stuff is
     * Checks if answer is correct
     * Changes question if previous answer answered correctly
     * Moves on to next intent for GameEnd if done
     */
    private void update() {
        bucket.update();

        // Updating the enemy coordinate with respect to player speed
        for(int i=0; i<numberCount; i++){
            numbers[i].update();

            // If collision occurs with player
            if (Rect.intersects(bucket.getDetectCollision(), numbers[i].getDetectCollision())) {
                // Moving enemy outside the left edge
                numbers[i].setX(-200);
                if (answers.contains(numbers[i].getText())) {

                    answers.remove(numbers[i].getText());

                    // Writes caught answer to question at the top
                    if(question.contains("_")) {
                        question = question.replaceFirst("[_]", numbers[i].getText());
                        correctAnswer = true;
                    }
                    else if(lessonID == 8){
                        correctAnswer = true;
                    }
                    else{
                        question = question.replaceAll("[x]", "("+numbers[i].getText()+")");
                        correctAnswer = true;
                    }

                    correctAnswers += 1;
                }

                showResult = true;
            }

            // If the question was gotten correct
            if (correctAnswers >= numAnswers) {
                if (showFinishedGameTimer == 0) {
                    if (currentQuestion + 3 <= gameQuestions.size() - 1) {
                        // Go to next question if there is another question left to go to

                        currentQuestion += 2;
                        correctAnswers = 0;

                        ArrayList<String> texts = gameQuestions.get(currentQuestion);
                        ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

                        numberCount = texts.size();
                        question = answersStr.get(0);
                        answersStr.remove(0);
                        answers = answersStr;
                        numAnswers = answers.size();

                        //initializing drawing objects
                        surfaceHolder = getHolder();
                        paint.setColor(Color.BLACK);

                        //Get question text height
                        Rect bounds = new Rect();
                        paint.getTextBounds(question, 0, question.length(), bounds);
                        questionHeight = bounds.height();
                        showFinishedGameTimer = 10;

                        //initializing number object array
                        numbers = new BucketNumber[numberCount];
                        for (int x = 0; x < numberCount; x++) {
                            numbers[x] = new BucketNumber(width, height,
                                    texts.get(x), questionHeight);
                        }

                        // Save start time to limit fps
                        startTime = System.currentTimeMillis();
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
                    showFinishedGameTimer -= 1;
                }
            }
        }
    }

    /*
     * Called during game loop
     * Draws all of the game items
     */
    private void draw() {
        // Checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            // Locking the canvas
            canvas = surfaceHolder.lockCanvas();

            // Drawing a background color for canvas
            canvas.drawColor(ContextCompat.getColor(context, R.color.bucketGameBG));
            Bitmap dragonBG = BitmapFactory.decodeResource(
                    getResources(),R.drawable.game_goldchest);
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(95);
            canvas.drawBitmap(dragonBG, width / 2 - dragonBG.getWidth() / 2,
                    height / 2 - dragonBG.getHeight() / 2, alphaPaint);

            // Write question to screen
            canvas.drawText(
                    question,
                    (width / 2 - (int)paint.measureText(question) / 2),
                    questionHeight,
                    paint);

            //drawing the falling numbers
            for (int i = 0; i < numberCount; i++) {
                Bitmap coinImg = BitmapFactory.decodeResource(getResources(),
                        R.drawable.game_goldcoin);
                coinImg = Bitmap.createScaledBitmap(coinImg,
                        (int)(questionHeight * 1.8),
                        (int)(questionHeight * 1.8), false);
                int x = numbers[i].getText().length();
                if (x > 1) {
                    // Draw double character answer
                    canvas.drawBitmap(
                            coinImg,
                            numbers[i].getX() - ((int)paint.measureText(numbers[i].getText()) / 6),
                            numbers[i].getY() - (int)(questionHeight * 1.3),
                            paint);
                } else {
                    // Draw single character answer
                    canvas.drawBitmap(
                            coinImg,
                            numbers[i].getX() - (int)(paint.measureText(
                                    numbers[i].getText()) * 0.8),
                            numbers[i].getY() - (int)(questionHeight * 1.3),
                            paint);
                }
                canvas.drawText(
                        numbers[i].getText(),
                        numbers[i].getX(),
                        numbers[i].getY(),
                        paint
                );
            }

            // Tell user whether they caught correct number
            if (showResult) {

                if (correctAnswer) {
                    paint.setColor(ContextCompat.getColor(context, R.color.gameCorrect));
                    canvas.drawText(
                            "CORRECT",
                            (width / 2 - (int)paint.measureText("CORRECT") / 2),
                            height / 2 - dragonBG.getHeight() / 2 - 20,
                            paint
                    );
                    paint.setColor(Color.BLACK);
                } else {
                    paint.setColor(ContextCompat.getColor(context, R.color.gameIncorrect));
                    canvas.drawText(
                            "INCORRECT",
                            (width / 2 - (int)paint.measureText("INCORRECT") / 2),
                            height / 2 - dragonBG.getHeight() / 2 - 20,
                            paint
                    );
                    paint.setColor(Color.BLACK);
                }
                // Keep "Correct" or "Incorrect" on screen long enough to read
                showResultTimer -= 1;
                if (showResultTimer == 0) {
                    showResult = false;
                    showResultTimer = 10;
                    correctAnswer = false;
                }
            }

            // Drawing the player
            canvas.drawBitmap(
                    bucket.getBitmap(),
                    bucket.getX(),
                    bucket.getY(),
                    paint);

            // Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /*
     * Thread control
     */
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Pauses game/thread when page is left
     */
    public void pause() {
        // When the game is paused
        // Setting the variable to false
        playing = false;
        try {
            // Stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    /*
     * Resumes game/thread when page is returned to
     */
    public void resume() {
        // When the game is resumed
        // Starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /*
     * Reads touch events to move bucket
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        final int action = MotionEventCompat.getActionMasked(motionEvent);
        switch (action) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                // Move bucket
                bucket.startMoveBucket((int)motionEvent.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                // Move bucket
                bucket.stopMoveBucket((int)motionEvent.getX());
                break;
        }
        return true;
    }
}
