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
 * Last Edit: 2-6-17
 *
 */

package com.gedappgui.gedappgui;

import android.graphics.Rect;

import java.util.Random;

public class BucketNumber {

    //Text falling
    private String text;
    private int strLength;

    //coordinates
    private int x;
    private int y;

    //motion speed of the character
    private int speed = 0;
    private int changeY = 0;

    //screen boundaries
    private int minY;
    private int maxY;
    private int maxX;

    private Rect detectCollision;

    private int width;
    private int height;

    //constructor
    public BucketNumber(int widthp, int heightp, String textString, int strLen,
                        int questionHeight) {

        // Basic variables
        width = widthp;
        height = heightp;
        x = 0;
        y = 0;
        changeY = (height) / (17 * 6);
        Random generator = new Random();
        speed = (int) (changeY * (generator.nextInt(10) / 10.0 + 0.5));
        text = textString;
        strLength = strLen;

        //calculating maxY
        maxY = height - (height / 17);

        //top edge's y point is 0 so min y will always be zero
        minY = questionHeight * 3;

        //calculating maxY
        maxX = width - strLength;

        //generating a random coordinate to add enemy
        x = generator.nextInt(maxX - strLength);
        y = minY;

        //initializing rect object
        detectCollision =  new Rect(x, y, strLength, 10);
    }

    //Method to update coordinate of character
    public void update(){
        //updating y coordinate
        y += speed;

        //if the enemy reaches the bottom edge
        if (y > maxY) {
            //adding the enemy again to the top edge
            Random generator = new Random();
            speed = (int) (changeY * (generator.nextInt(10) / 10.0 + 0.5));
            y = minY;
            x = generator.nextInt(maxX - strLength);
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + strLength;
        detectCollision.bottom = y + 30;
    }

    //adding a setter to x coordinate so that we can change it after collision
    public void setX(int x){
        this.x = x;
    }

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    /*
    * These are getters
    * */
    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
