/*
 * BucketNumber.java
 *
 * Number to catch with bucket
 *
 * Bucket number
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

import android.graphics.Rect;

import java.util.Random;

public class BucketNumber {

    // Text falling
    private String text;

    // Coordinates
    private int x;
    private int y;

    // Motion speed of the falling text
    private int speed = 0;
    private int changeY = 0;

    // Screen boundaries
    private int minY;
    private int maxY;
    private int maxX;

    // Rectangle to check if a collision happened
    private Rect detectCollision;

    // Screen size
    private int width;
    private int height;

    // Height of question text
    private int questionHeight;

    // End endless play button size
    private int endButtonSize = 0;

    /**
     * Constructor for number object that student catches in bucket
     * @param widthp Width of the screen in pixels
     * @param heightp Height of screen in pixels
     * @param textString Number text of the number
     * @param questionHeightp Measured height of text from Paint object
     * @param endButtonSizep The size of the end button if it exists (for the Play page)
     */
    public BucketNumber(int widthp, int heightp, String textString, int questionHeightp,
                        int endButtonSizep) {

        // Basic variables
        // Screen size
        width = widthp;
        height = heightp;

        endButtonSize = endButtonSizep;

        // Height of question text
        questionHeight = questionHeightp;

        // Originally set coordinates to 0 before calculations
        x = 0;
        y = 0;

        // Change in y based on screen size to help keep speed reasonable
        changeY = (height) / (17 * 6);

        // Set random speed
        Random generator = new Random();
        speed = (int) (changeY * (generator.nextInt(10) / 10.0 + 0.5));

        // Text to have falling
        text = textString;

        // Calculating maxY
        maxY = height - (height / 17);

        // Min y is below the question text
        minY = questionHeight * 3;

        // Calculating maxY
        maxX = width - (int)((float)height / 12);

        // Generating a random x coordinate to add enemy at
        x = generator.nextInt(maxX - ((int)((float)height / 12 / 2) + 1)) + (int)((float)height / 12 / 2);
        y = minY + endButtonSize;

        // Initializing rect object for detecting collisions
        detectCollision =  new Rect(y - (int) ((float)height / 20),
                x - (int) ((float)height / 60),
                y + (int) ((float)height / 60),
                x + (int) ((float)height / 18));
    }

    /**
     * Update coordinates
     */
    public void update(){
        // Updating y coordinate
        y += speed;

        // If the enemy reaches the bottom edge
        if (y > maxY) {
            // Adding the enemy again to the top edge
            Random generator = new Random();
            speed = (int) (changeY * (generator.nextInt(10) / 10.0 + 0.5));
            y = minY + endButtonSize;
            x = generator.nextInt(maxX - ((int)((float)height / 12 / 2) + 1)) + (int)((float)height / 12 / 2);
        }

        // Adding the top, left, bottom and right to the rect object
        // For when the it moves
        detectCollision.left = x - (int) ((float)height / 60);
        detectCollision.top = y - (int) ((float)height / 20);
        detectCollision.right = x + (int) ((float)height / 18);
        detectCollision.bottom = y + (int) ((float)height / 60);
    }

    /**
     * Setter
     * Sets the x coordinate
     * Helps with changing x-coordinate after collision
     * @param x New x to set x to
     */
    public void setX(int x){

        this.x = x;
        detectCollision.left = x - (int) ((float)height / 60);
        detectCollision.top = y - (int) ((float)height / 20);
        detectCollision.right = x + (int) ((float)height / 18);
        detectCollision.bottom = y + (int) ((float)height / 60);
    }

    /**
     * Setter
     * Sets the speed
     * Helps with separating coins
     * @param newSpeed New speed to set speed to
     */
    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }

    /**
     * Getter
     * Gets the rectangle to check for collisions
     * @return detectCollision - The rectangle around the number
     */
    public Rect getDetectCollision() {
        return detectCollision;
    }

    /**
     * Getter
     * Gets the text used
     * @return text - The number
     */
    public String getText() {
        return text;
    }

    /**
     * Getter
     * Gets the speed
     * @return speed - The number's speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Getter
     * Gets the x-coordinate
     * @return x - The x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     * Gets the y-coordinate
     * @return y - The y coordinate
     */
    public int getY() {
        return y;
    }
}
