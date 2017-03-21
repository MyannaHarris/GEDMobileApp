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
 * Last Edit: 3-19-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private boolean hideContentToShowAnswer = false;
    private boolean showQuestionAtBeginning = true;
    private int waitToStartNextGame;
    private RectF startButton;
    private String startText;

    /**
     * Constructor for game
     * @param contextp Context of the activity
     * @param widthp Width of the screen in pixels
     * @param heightp Height of screen in pixels
     * @param gameQuestionsp List of data for the game
     * @param conceptIDp ID of the current concept
     * @param lessonIDp ID of the current lesson
     * @param nextActivityp Number indicating what the activity after the game should be
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
        paint.setColor(ContextCompat.getColor(context, R.color.bucketGameText));
        paint.setTextSize((float)height / 17);

        // Get question text height
        Rect bounds = new Rect();
        paint.getTextBounds(question, 0, question.length(), bounds);
        questionHeight = bounds.height();

        // Pause to show equation
        waitToStartNextGame = 0;
        showFinishedGameTimer = 40;
        startText = "Tap to start";
        startButton = new RectF(
                (int)((width / 2) - (paint.measureText(startText) / 2)
                    - questionHeight),
                (int)((height / 2) - (questionHeight * 2)),
                (int)((width / 2) + (paint.measureText(startText) / 2)
                    + questionHeight),
                (int)((height / 2) + (questionHeight * 2)));

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

    /**
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

    /**
     * Called during game loop
     * Updates where stuff is
     * Checks if answer is correct
     * Changes question if previous answer answered correctly
     * Moves on to next intent for GameEnd if done
     */
    private void update() {
        bucket.update();

        // Count down the next round timer to pause and show the user the new question
        if (waitToStartNextGame > 0) {
            waitToStartNextGame -= 1;
        }

        // Count down the finished game timer to pause and show the user their correct answers
        if (correctAnswers >= numAnswers && showFinishedGameTimer > 0) {
            showFinishedGameTimer -= 1;
            hideContentToShowAnswer = true;
        }

        // Updating the numbers' coordinates
        for(int i=0; i<numberCount; i++){

            // Only update numbers if the game is not paused to show the question or answer
            if(showFinishedGameTimer == 40 && !showQuestionAtBeginning
                    && waitToStartNextGame == 0) {
                numbers[i].update();
            }

            // If collision occurs with player
            if (Rect.intersects(bucket.getDetectCollision(), numbers[i].getDetectCollision())) {
                // Moving enemy outside the left edge
                numbers[i].setX(-200);

                // Check if it is a correct answer
                if (answers.contains(numbers[i].getText())) {

                    // Remove number from answers if correct so only the
                    //      correct answers not yet gotten count from now on
                    answers.remove(numbers[i].getText());

                    // Writes caught answer to question at the top
                    if(question.contains("_")) {
                        question = question.replaceFirst("[_]", numbers[i].getText());
                        correctAnswer = true;
                    }
                    else if(lessonID == 8){
                        // This lesson does not have items to replace
                        //      as it is summing things in the question
                        correctAnswer = true;
                    }
                    else{
                        question = question.replaceAll("[x]", "("+numbers[i].getText()+")");
                        correctAnswer = true;
                    }

                    correctAnswers += 1;
                }

                // Show if user got answer correct or incorrect
                showResult = true;
            }

            // If the question was gotten correct
            if (correctAnswers >= numAnswers) {

                // Don't show the numbers so user can see their correct answers
                hideContentToShowAnswer = true;

                // Wait so user can see their answers
                if (showFinishedGameTimer == 0) {
                    // Go to next question if there is another question left to go to
                    if (currentQuestion + 3 <= gameQuestions.size() - 1) {

                        // Increment question by 2 since every 2 lists in the master list
                        //      are for a single question
                        currentQuestion += 2;
                        // Reset number of correct answers for this round to 0
                        correctAnswers = 0;

                        // The lists of information from the master list
                        // Evens are numbers to drop
                        // Odds are the question and answers
                        ArrayList<String> texts = gameQuestions.get(currentQuestion);
                        ArrayList<String> answersStr = gameQuestions.get(currentQuestion + 1);

                        // Get next question information
                        numberCount = texts.size(); // Number of falling numbers
                        question = answersStr.get(0); // Question to put at top
                        answersStr.remove(0); // Remove question from list
                        answers = answersStr; // Save all of the answers in a global list
                        numAnswers = answers.size(); // Number of answers for the question

                        //initializing drawing objects
                        surfaceHolder = getHolder();
                        paint.setColor(ContextCompat.getColor(context, R.color.bucketGameText));

                        //Get question text height
                        Rect bounds = new Rect();
                        paint.getTextBounds(question, 0, question.length(), bounds);
                        questionHeight = bounds.height();

                        // Reset timers and booleans that pause between rounds
                        // Number of loops to wait to show what answer the user got right to finish the round
                        showFinishedGameTimer = 40;
                        hideContentToShowAnswer = false; // Whether to show the numbers
                        waitToStartNextGame = 30; // Number of loops to wait before dropping numbers

                        // Reset answer variables
                        showResult = false; // Whether to show the "Correct" or "Incorrect"
                        correctAnswer = false; // Whether the user has gotten a correct answer
                        showResultTimer = 10; // Number of loops to show the result word

                        // Initializing number object array
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
                }
            }
        }
    }

    /**
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

            // Draw bitmap to bacjground of game
            Bitmap dragonBG = BitmapFactory.decodeResource(
                    getResources(),R.drawable.game_goldchest);
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(95);
            canvas.drawBitmap(dragonBG, width / 2 - dragonBG.getWidth() / 2,
                    height / 2 - dragonBG.getHeight() / 2, alphaPaint);

            // Draw question to screen
            canvas.drawText(
                    question,
                    (width / 2 - (int)paint.measureText(question) / 2),
                    questionHeight,
                    paint);

            // Draw numbers only if in game and not during breaks between rounds
            if(!hideContentToShowAnswer && !showQuestionAtBeginning
                    && waitToStartNextGame == 0) {
                // Drawing the falling numbers
                for (int i = 0; i < numberCount; i++) {
                    Bitmap coinImg = BitmapFactory.decodeResource(getResources(),
                            R.drawable.game_goldcoin);
                    coinImg = Bitmap.createScaledBitmap(coinImg,
                            (int) ((float)height / 12),
                            (int) ((float)height / 12), false);
                    int x = numbers[i].getText().length();

                    // Draw the coin image
                    if (x > 1) {
                        // Draw double character answer
                        canvas.drawBitmap(
                                coinImg,
                                numbers[i].getX() - ((int) paint.measureText(numbers[i].getText()) / 6),
                                numbers[i].getY() - ((float)height / 16),
                                paint);
                    } else {
                        // Draw single character answer
                        canvas.drawBitmap(
                                coinImg,
                                numbers[i].getX() - (int) (paint.measureText(
                                        numbers[i].getText()) * 0.7),
                                numbers[i].getY() - ((float)height / 16),
                                paint);
                    }
                    // Draw the text
                    canvas.drawText(
                            numbers[i].getText(),
                            numbers[i].getX(),
                            numbers[i].getY(),
                            paint
                    );
                }
            }

            // Tell user whether they caught correct number
            if ((showResult || hideContentToShowAnswer) && waitToStartNextGame == 0) {

                if (correctAnswer || hideContentToShowAnswer) {
                    // Show if user catches a correct number or
                    //      if user finishes question correctly
                    paint.setColor(ContextCompat.getColor(context, R.color.gameCorrect));
                    canvas.drawText(
                            "CORRECT",
                            (width / 2 - (int) paint.measureText("CORRECT") / 2),
                            height / 2 - dragonBG.getHeight() / 2 - 20,
                            paint
                    );
                    // Return paint color to black for text falling
                    paint.setColor(ContextCompat.getColor(context, R.color.bucketGameText));
                } else if(!hideContentToShowAnswer) {
                    // Show if user catches incorrect number
                    paint.setColor(ContextCompat.getColor(context, R.color.gameIncorrect));
                    canvas.drawText(
                            "INCORRECT",
                            (width / 2 - (int) paint.measureText("INCORRECT") / 2),
                            height / 2 - dragonBG.getHeight() / 2 - 20,
                            paint
                    );
                    // Return paint color to black for text falling
                    paint.setColor(ContextCompat.getColor(context, R.color.bucketGameText));
                }
                // Keep "Correct" or "Incorrect" on screen long enough to read
                showResultTimer -= 1;
                if (showResultTimer == 0) {
                    showResult = false;
                    showResultTimer = 10;
                    correctAnswer = false;
                }
            }

            // Drawing the player (bucket)
            canvas.drawBitmap(
                    bucket.getBitmap(),
                    bucket.getX(),
                    bucket.getY(),
                    paint);

            // Waits for user to tap button before starting first round
            // Draws only the question, bucket, and button
            if (showQuestionAtBeginning) {
                // Set special settings for drawing the button
                Paint rectAlphaPaint = new Paint();
                rectAlphaPaint.setStyle(Paint.Style.FILL);
                rectAlphaPaint.setAlpha(80);
                rectAlphaPaint.setColor(ContextCompat.getColor(context, R.color.bucketGameStartButton));

                // Draw the start button
                canvas.drawRoundRect(startButton, 30, 30, rectAlphaPaint);
                paint.setColor(ContextCompat.getColor(context, R.color.bucketGameStartText));
                canvas.drawText(
                        startText,
                        startButton.centerX() - (paint.measureText(startText) / 2),
                        startButton.centerY() + (questionHeight / 4),
                        paint);
                paint.setColor(ContextCompat.getColor(context, R.color.bucketGameText));
            }

            // Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Thread control
     */
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
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

    /**
     * Resumes game/thread when page is returned to
     */
    public void resume() {
        // When the game is resumed
        // Starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Reads touch events to move bucket
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        final int action = MotionEventCompat.getActionMasked(motionEvent);
        switch (action) {
            case MotionEvent.ACTION_UP:
                // Waits for user to tap button before starting first round
                if (showQuestionAtBeginning) {
                    if (startButton.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                        showQuestionAtBeginning = false;
                    }
                }
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
