/*
 * Bucket.java
 *
 * Bucket character for student to move
 *
 * Bucket character
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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;

public class Bucket {

    // Bitmap to get character from image
    private Bitmap bitmap;

    // Coordinates
    private int x;
    private int y;

    // Screen boundaries
    private int minY;
    private int maxY;
    private int minX;
    private int maxX;

    // For moving
    private int startX;
    private int dx;

    // Rectangle to check if a collision happened
    private Rect detectCollision;

    // Screen size
    private int width;
    private int height;

    /*
     * Constructor
     */
    public Bucket(Context context, int widthp, int heightp, int questionHeight) {
        // Set base values for coordinates
        x = 0;
        y = 0;

        // Save screen size
        width = widthp;
        height = heightp;

        // Make bitmap for the bucket (uses the bucket image)
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_bucket);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(questionHeight * 2.2),
                (int)(questionHeight * 2.2), false);

        // Calculating maxY
        maxY = height - bitmap.getHeight();
        if (Build.VERSION.SDK_INT < 19) {
            // Moves bucket up higher
            //      since older phones will not hide the bottom navigation bar
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                maxY = maxY - resources.getDimensionPixelSize(resourceId);
            }
        }

        // Top edge's y point is 0 so min y will always be zero
        minY = 0;

        // Calculating maxX
        maxX = width - bitmap.getWidth();

        // Left edge's x point is 0 so min x will always be zero
        minX = 0;

        // Puts bucket in the center of the screen-ish to start
        x = maxX / 2;
        y = maxY;

        // Initializing rect object for detecting collisions
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    /*
     * Setter
     * Set start X coordinate
     */
    public void startMoveBucket(int newX) {
        startX = newX;
    }

    /*
     * Setter
     * Set change in x coordinate
     */
    public void stopMoveBucket(int newX) {
        dx = startX - newX;
    }

    /*
     * Update coordinate
     */
    public void update(){
        // Updating x coordinate
        if (x - dx > maxX) {
            x = maxX;
            dx = 0;
            startX = x;
        } else if (x - dx < minX) {
            x = minX;
            dx = 0;
            startX = x;
        } else {
            x = x - dx;
            dx = 0;
            startX = x;
        }

        // Adding top, left, bottom and right to the rect object
        // For when the it moves
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    /*
     * Getter
     * Gets the rectangle to check for collisions
     */
    public Rect getDetectCollision() {
        return detectCollision;
    }

    /*
     * Getter
     * Gets the bitmap used
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /*
     * Getter
     * Gets the x-coordinate
     */
    public int getX() {
        return x;
    }

    /*
     * Getter
     * Gets the y-coordinate
     */
    public int getY() {
        return y;
    }
}
