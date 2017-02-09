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

    //Class constructor
    public BucketGameView(Context contextp, int widthp, int heightp,
                          ArrayList<ArrayList<String>> gameQuestionsp, int conceptIDp,
                          int lessonIDp, int nextActivityp) {
        super(contextp);

        context = contextp;

        // gameQuestions
        // 0 - texts
        // 1 - answers (0 - question)

        gameQuestions = gameQuestionsp;

        ArrayList<String> texts = gameQuestions.get(currentQuestion);
        ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

        numberCount = texts.size();
        width = widthp;
        height = heightp;

        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;
        question = answersStr.get(0);
        answersStr.remove(0);
        answers = answersStr;
        numAnswers = answers.size();

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize((float)height / 17);

        //Get question text height
        Rect bounds = new Rect();
        paint.getTextBounds(question, 0, question.length(), bounds);
        questionHeight = bounds.height();

        // Pause to show equation
        showFinishedGameTimer = 10;

        //initializing player object
        //this time also passing screen size to player constructor
        bucket = new Bucket(context, width, height, questionHeight);

        //initializing number object array
        numbers = new BucketNumber[numberCount];
        for(int i=0; i<numberCount; i++){
            numbers[i] = new BucketNumber(width, height, texts.get(i),
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


    private void update() {
        bucket.update();

        //updating the enemy coordinate with respect to player speed
        for(int i=0; i<numberCount; i++){
            numbers[i].update();

            //if collision occurs with player
            if (Rect.intersects(bucket.getDetectCollision(), numbers[i].getDetectCollision())) {
                //moving enemy outside the left edge
                numbers[i].setX(-200);
                if (answers.contains(numbers[i].getText())) {

                    answers.remove(numbers[i].getText());

                    question = question.replaceFirst("[_]", numbers[i].getText());
                    correctAnswer = true;

                    correctAnswers += 1;
                }

                showResult = true;
            }

            if (correctAnswers >= numAnswers) {
                if (showFinishedGameTimer == 0) {
                    if (currentQuestion + 3 <= gameQuestions.size() - 1) {
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
                                    texts.get(x), (int) paint.measureText(texts.get(x)),
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
                } else {
                    showFinishedGameTimer -= 1;
                }
            }
        }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();

            //drawing a background color for canvas
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
                    canvas.drawBitmap(
                            coinImg,
                            numbers[i].getX() - ((int)paint.measureText(numbers[i].getText()) / 6),
                            numbers[i].getY() - (int)(questionHeight * 1.3),
                            paint);
                } else {
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
