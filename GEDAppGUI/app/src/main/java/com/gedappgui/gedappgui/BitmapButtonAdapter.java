/*
 * BitmapButtonAdapter.java
 *
 * Bitmap Button Adapter
 *
 * Adapter to fill a gridView with imageViews that are bitmaps and buttons
 *
 * http://stackoverflow.com/questions/16103344/android-text-size-on-canvas-differ-from-text-size-in-textview
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring, person on stackoverflow
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
 * Last Edit: 1-25-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BitmapButtonAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;

    // Hold the bitmaps of the images in assets
    private Bitmap[] images;

    //width and height of the
    private int width;
    private int height;

    /**
     * Constructor
     * Gets the context, width, height and bitmaps for use later
     * @param c the context
     * @param buttonNames the bitmap array of each achievement earned
     * @param w the width of the screen
     * @param h the height of the screen
     */
    public BitmapButtonAdapter(Context c, Bitmap[] buttonNames, int w, int h){
        mContext = c;
        images = buttonNames;
        width = w;
        height = h;
    }

    /**
     * Gets the number of items to put in the view
     * @return the number of achievements earned (imaged in the gridview)
     */
    @Override
    public int getCount() {
        return images.length;
    }

    /**
     * Does not do anything but needed to implement BaseAdapter
     * @param position the position of the item
     * @return null
     */
    @Override
    public Object getItem(int position) {
        return null;
    }


    /**
     * Does not do anything but needed to implement BaseAdapter
     * @param position of the item
     * @return position sent to the method
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Creates the components for the adapter
     * @param position the position of the item
     * @param convertView the view
     * @param parent view group of the parent
     * @return an imagevView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            //creates an imageview as a grid
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(
              new GridView.LayoutParams((int)(width/4.5),(int)(height/7.5)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        } else {
            imageView = (ImageView) convertView;
        }

        //sets the image in the view to be the bitmap corresponding to the position
        imageView.setImageBitmap(images[position]);
        return imageView;
    }
}

