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
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring, person on simplifiedcoding
 *
 * This file is part of DragonAcademy.
 *
 * DragonAcademy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License. All redistributions
 * of the app or modifications of the app are to remain free in accordance
 * with the GNU General Public License.
 *
 * DragonAcademy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DragonAcademy.  If not, see <http://www.gnu.org/licenses/>.
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
    private int questionHeight;

    /**
     * Constructor for bucket object that student uses to catch falling numbers
     * @param context Context of the activity using the bucket
     * @param widthp Width of the screen in pixels
     * @param heightp Height of screen in pixels
     * @param questionHeightp Measured height of text from Paint object
     */
    public Bucket(Context context, int widthp, int heightp, int questionHeightp) {
        // Set base values for coordinates
        x = 0;
        y = 0;

        // Save screen size
        width = widthp;
        height = heightp;
        questionHeight = questionHeightp;

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
        detectCollision =  new Rect(y, x, y + 20, x + bitmap.getWidth());
    }

    /**
     * Setter
     * Sets start X coordinate
     * @param newX The new x coordinate to start at
     */
    public void startMoveBucket(int newX) {
        startX = newX;
    }

    /**
     * Setter
     * Sets change in X coordinate
     * @param newX The new x coordinate moved to
     */
    public void stopMoveBucket(int newX) {
        dx = (int)(startX - newX + questionHeight * 1.1);
    }

    /**
     * Update x coordinate
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
        if (bitmap != null) {
            detectCollision.right = x + bitmap.getWidth();
        } else {
            detectCollision.right = x;
        }
        detectCollision.bottom = y + 20;
    }

    /**
     * Getter
     * Gets the rectangle to check for collisions
     * @return detectCollision - The rectangle around the bucket
     */
    public Rect getDetectCollision() {
        return detectCollision;
    }

    /**
     * Getter
     * Gets the bitmap used
     * @return bitmap - The bitmap of the bucket
     */
    public Bitmap getBitmap() { return bitmap; }

    /**
     * Setter
     * Sets the bitmap used
     * @param bitmapNew - The new bitmap value
     */
    public void setBitmap(Bitmap bitmapNew) { this.bitmap = bitmapNew; }

    /**
     * Getter
     * Gets the x-coordinate
     * @return x - The x coordinate of the bucket
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     * Gets the y-coordinate
     * @return y - The y coordinate of the bucket
     */
    public int getY() {
        return y;
    }
}
