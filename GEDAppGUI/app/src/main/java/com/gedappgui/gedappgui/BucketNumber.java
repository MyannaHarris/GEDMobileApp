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
 * Last Edit: 11-29-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class BucketNumber {

    //Text falling
    private String text;
    private int strLegth;

    //coordinates
    private int x;
    private int y;

    //motion speed of the character
    private int speed = 0;

    //screen boundaries
    private int minY;
    private int maxY;
    private int minX;
    private int maxX;

    private Rect detectCollision;
    private int textHeight;

    //constructor
    public BucketNumber(Context context, int screenX, int screenY, String textString, int strLen,
                        int questionHeight) {
        x = 0;
        y = 0;
        speed = 5;
        text = textString;
        strLegth = strLen;

        //calculating maxY
        maxY = screenY - 10;

        //top edge's y point is 0 so min y will always be zero
        minY = questionHeight * 2;

        //calculating maxY
        maxX = screenX - strLegth;

        // Save text height for collision calculations
        textHeight = questionHeight;

        //top edge's y point is 0 so min y will always be zero
        minX = 0;

        //generating a random coordinate to add enemy
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = generator.nextInt(maxX - strLegth);
        y = 10;

        //initializing rect object
        detectCollision =  new Rect(x, y, strLegth, 10);
    }

    //Method to update coordinate of character
    public void update(){
        //updating y coordinate
        y += speed;

        //if the enemy reaches the left edge
        if (y > maxY) {
            //adding the enemy again to the right edge
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            y = minY;
            x = generator.nextInt(maxX - strLegth);
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + strLegth;
        detectCollision.bottom = y + textHeight;
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
