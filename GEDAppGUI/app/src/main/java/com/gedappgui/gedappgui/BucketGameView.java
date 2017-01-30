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
 * Last Edit: 11-28-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class BucketGameView extends SurfaceView implements Runnable  {

    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    //character bucket
    private Bucket bucket;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Adding number object array
    private BucketNumber[] numbers;

    //Adding this number of numbers
    private int numberCount = 5;

    //Answers
    private ArrayList<String> answers;
    private int correctAnswers = 0;
    private int numAnswers = 0;
    private String question;
    private int screenXVar;
    private int screenYVar;
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

    //Class constructor
    public BucketGameView(Context contextp, int screenX, int screenY,
                          ArrayList<ArrayList<String>> gameQuestionsp, int conceptIDp, int lessonIDp,
                          int nextActivityp) {
        super(contextp);

        context = contextp;

        // gameQuestions
        // 0 - texts
        // 1 - answers (0 - question)

        gameQuestions = gameQuestionsp;

        ArrayList<String> texts = gameQuestions.get(currentQuestion);
        ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

        numberCount = texts.size();
        screenXVar = screenX;
        screenYVar = screenY;

        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;
        question = answersStr.get(0);
        answersStr.remove(0);
        answers = answersStr;
        numAnswers = answers.size();

        //initializing player object
        //this time also passing screen size to player constructor
        bucket = new Bucket(context, screenX, screenY);

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);

        //Get question text height
        Rect bounds = new Rect();
        paint.getTextBounds(question, 0, question.length(), bounds);
        questionHeight = bounds.height();

        //initializing number object array
        numbers = new BucketNumber[numberCount];
        for(int i=0; i<numberCount; i++){
            numbers[i] = new BucketNumber(context, screenX, screenY, texts.get(i),
                    (int)paint.measureText(texts.get(i)), questionHeight);
        }

        // Save start time to limit fps
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (playing) {
            // limit fps
            endTime = System.currentTimeMillis();
            long dt = endTime - startTime;
            if (dt < 50)
                try {
                    Thread.sleep(50 - dt);
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


    private void update() {
        bucket.update();

        //updating the enemy coordinate with respect to player speed
        for(int i=0; i<numberCount; i++){
            numbers[i].update();

            //if collision occurrs with player
            if (Rect.intersects(bucket.getDetectCollision(), numbers[i].getDetectCollision())) {
                //moving enemy outside the left edge
                numbers[i].setX(-200);
                if (answers.contains(numbers[i].getText())) {

                    answers.remove(numbers[i].getText());

                    question = question.replaceFirst("[_]", numbers[i].getText());
                    correctAnswer = true;

                    correctAnswers += 1;
                    if (correctAnswers >= numAnswers) {
                        if (currentQuestion + 3 <= gameQuestions.size() - 1) {
                            currentQuestion += 2;

                            ArrayList<String> texts = gameQuestions.get(currentQuestion);
                            ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

                            numberCount = texts.size();
                            numAnswers = answers.size() - 1;
                            question = answersStr.get(0);
                            answersStr.remove(0);
                            answers = answersStr;

                            //initializing drawing objects
                            surfaceHolder = getHolder();
                            paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setTextSize(100);

                            //Get question text height
                            Rect bounds = new Rect();
                            paint.getTextBounds(question, 0, question.length(), bounds);
                            questionHeight = bounds.height();

                            //initializing number object array
                            numbers = new BucketNumber[numberCount];
                            for(int x=0; x<numberCount; x++){
                                numbers[x] = new BucketNumber(context, screenXVar, screenYVar,
                                        texts.get(x), (int)paint.measureText(texts.get(x)),
                                        questionHeight);
                            }

                            // Save start time to limit fps
                            startTime = System.currentTimeMillis();
                        } else {
                            Context context = getContext();
                            Intent intent = new Intent(context, GameEnd.class);
                            intent.putExtra("next_activity", nextActivity);
                            intent.putExtra("conceptID", conceptID);
                            intent.putExtra("lessonID", lessonID);
                            context.startActivity(intent);
                        }
                    }
                }

                showResult = true;
            }
        }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();

            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            // Write question to screen
            canvas.drawText(
                    question,
                    (screenXVar / 2 - (int)paint.measureText(question) / 2),
                    questionHeight,
                    paint);

            //drawing the falling numbers
            for (int i = 0; i < numberCount; i++) {
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
                    paint.setColor(Color.GREEN);
                    canvas.drawText(
                            "CORRECT",
                            (screenXVar / 2 - (int)paint.measureText("CORRECT") / 2),
                            (screenYVar / 2),
                            paint
                    );
                    paint.setColor(Color.WHITE);
                } else {
                    paint.setColor(Color.RED);
                    canvas.drawText(
                            "INCORRECT",
                            (screenXVar / 2 - (int)paint.measureText("INCORRECT") / 2),
                            (screenYVar / 2),
                            paint
                    );
                    paint.setColor(Color.WHITE);
                }
                showResultTimer -= 1;
                if (showResultTimer == 0) {
                    showResult = false;
                    showResultTimer = 10;
                    correctAnswer = false;
                }
            }

            //Drawing the player
            canvas.drawBitmap(
                    bucket.getBitmap(),
                    bucket.getX(),
                    bucket.getY(),
                    paint);

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        final int action = MotionEventCompat.getActionMasked(motionEvent);
        switch (action) {
            case MotionEvent.ACTION_UP:
                //stopping the boosting when screen is released
                break;
            case MotionEvent.ACTION_DOWN:
                //boosting the space jet when screen is pressed
                bucket.startMoveBucket((int)motionEvent.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                //boosting the space jet when screen is pressed
                bucket.stopMoveBucket((int)motionEvent.getX());
                break;
        }
        return true;
    }
}
